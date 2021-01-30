package com.web.dream11.service;

import com.web.dream11.exception.Dream11Exception;
import com.web.dream11.models.ValidationResponse;
import com.web.dream11.models.dtos.UserDTO;
import com.web.dream11.models.dtos.UserDetailsDTO;
import com.web.dream11.models.entities.User;
import com.web.dream11.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * The redirect service impl class
 */
@Service
public class UserServiceImp implements UserService{

    @Autowired
    private UserRepository userRepository;

    /**
     * Add user
     * @param userDetailsDTO
     * @return response
     */
    public ResponseEntity<String> addUser(UserDetailsDTO userDetailsDTO){
        Boolean isValid = validateUsername(userDetailsDTO.getUserMail());
        if(isValid) {
            User user = new User(userDetailsDTO.getName(),userDetailsDTO.getPassword(), userDetailsDTO.getUserMail(), userDetailsDTO.getPhoneNo());
            userRepository.save(user);
            return new ResponseEntity<>(String.valueOf(user.getId()), HttpStatus.OK);
        }else{
            throw new Dream11Exception("Duplicate username", HttpStatus.BAD_REQUEST);
        }
    }

    private Boolean validateUsername(String userMail) {
        return userRepository.findByEmail(userMail) == null;
    }

    /**
     * Gives success response
     * @param message response message
     * @param httpStatus response http status
     * @return ResponseEntity response details
     */
    public ResponseEntity successResponse(String message, HttpStatus httpStatus) {
        ValidationResponse response = new ValidationResponse();
        response.setMessage(message);
        return ResponseEntity
                .status(httpStatus)
                .body(response);
    }
    /**
     * Validate user
     * @param userDTO the user dto
     * @return validated user
     */
    public UserDTO validateUser(UserDTO userDTO) {
        User user = userRepository.findByEmailAndPassword(userDTO.getEmail(), userDTO.getPassword());
        if (user != null) {
            UserDTO userDto = new UserDTO();
            userDto.setEmail(user.getEmail());
            userDto.setId(user.getId());
            return userDto;
        }
        return  null;
    }

    /**
     * Get user details
     * @param id the user id
     * @return userDetailsDTO user details
     */
    public ResponseEntity<UserDetailsDTO> getUserData (int id) {
        User userDetails = userRepository.findById(id);
        if(userDetails != null) {
            UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
            if(userDetails != null) {
                userDetailsDTO.setId(userDetails.getId());
                userDetailsDTO.setName(userDetails.getName());
                userDetailsDTO.setUserMail(userDetails.getEmail());
                userDetailsDTO.setPhoneNo(userDetails.getPhone());
            }
            return new ResponseEntity<>(userDetailsDTO, HttpStatus.OK);
        }else{
            throw new Dream11Exception("no user found", HttpStatus.BAD_REQUEST);
        }
    }
}


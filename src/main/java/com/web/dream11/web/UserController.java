package com.web.dream11.web;

import com.web.dream11.models.dtos.UserDTO;
import com.web.dream11.models.dtos.UserDetailsDTO;
import com.web.dream11.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    /**
     * Validate user login
     * @param userDTO the user dto
     * @return user id
     */
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> validateUser(@RequestBody UserDTO userDTO) {
        UserDTO user = userServiceImp.validateUser(userDTO);
        if (user == null) {
            return new ResponseEntity<>("Not a valid user", HttpStatus.OK);
        }
        return new ResponseEntity<>(String.valueOf(user.getId()), HttpStatus.OK);
    }

    /**
     * Get user details
     * @param userId the user id
     * @return user details
     */
    @GetMapping("/userData")
    public ResponseEntity<UserDetailsDTO> getUserdata(@RequestParam int userId) {
        return userServiceImp.getUserData(userId);
    }

    /**
     * register user
     * @param userDetailsDTO the user dto
     * @return user data
     */
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody UserDetailsDTO userDetailsDTO) {
        ResponseEntity<String> responseEntity;
        responseEntity = userServiceImp.addUser(userDetailsDTO);
        return responseEntity;
    }

}
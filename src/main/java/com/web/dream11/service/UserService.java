package com.web.dream11.service;

import com.web.dream11.exception.Dream11Exception;
import com.web.dream11.models.dtos.UserDTO;
import com.web.dream11.models.dtos.UserDetailsDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    /**
     * Method add User
     * @param userDetailsDTO the userDetailsDTO
     * @return user
     */
    ResponseEntity addUser(UserDetailsDTO userDetailsDTO) throws Dream11Exception;

    /**
     * Validate user dto
     * @param userDTO the user dto
     * @return user
     */
    UserDTO validateUser(UserDTO userDTO);

    /**
     * Get user data
     * @param userId the user id
     * @return user details
     */
    ResponseEntity getUserData(int userId);

}


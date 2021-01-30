package com.web.dream11.web;

import com.web.dream11.models.dtos.UserDTO;
import com.web.dream11.models.dtos.UserDetailsDTO;
import com.web.dream11.models.entities.ConfirmationToken;
import com.web.dream11.models.entities.User;
import com.web.dream11.repository.ConfirmationTokenRepository;
import com.web.dream11.service.EmailSenderService;
import com.web.dream11.service.UserServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    private UserServiceImp userServiceImp;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

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

    /**
     * Receive email of the user, create token and send it via email to the user
     */
    @GetMapping(value="/forgotPassword")
    public ResponseEntity<String> forgotUserPassword(@RequestParam String email) {
        User existingUser = userServiceImp.validateUserByEmail(email);
        if (existingUser == null) {
            return new ResponseEntity<>("Not a valid user", HttpStatus.OK);
        }
        // create token
        ConfirmationToken confirmationToken = new ConfirmationToken(existingUser);

        // save it
        confirmationTokenRepository.save(confirmationToken);

        // create the email
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(existingUser.getEmail());
        mailMessage.setSubject("Complete Password Reset!");
        mailMessage.setFrom("edream1864@gmail.com");
        mailMessage.setText("To complete the password reset process, please click here: "
                +"https://dream-el.azurewebsites.net/confirm-reset?token="+confirmationToken.getConfirmationToken());

        emailSenderService.sendEmail(mailMessage);
        return new ResponseEntity<>("email sent", HttpStatus.OK);
    }

    @RequestMapping(value="/confirm-reset", method= {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView validateResetToken(@RequestParam("token")String confirmationToken)
    {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        ModelAndView modelAndView = new ModelAndView();
        if(token != null) {
            User user = userServiceImp.validateUserByEmail(token.getUser().getEmail());
            userServiceImp.save(user);
            modelAndView.addObject("user", user);
            modelAndView.addObject("email", user.getEmail());
            modelAndView.setViewName("resetPassword");
        } else {
            modelAndView.addObject("message", "The link is invalid or broken!");
            modelAndView.setViewName("error");
        }

        return modelAndView;
    }

    /**
     * Receive the token from the link sent via email and display form to reset password
     */
    @RequestMapping(value = "/reset-password", method = RequestMethod.POST)
    public ModelAndView resetUserPassword(User user) {
        // ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        ModelAndView modelAndView = new ModelAndView();
        if(user.getEmail() != null) {
            // use email to find user
            User tokenUser = userServiceImp.validateUserByEmail(user.getEmail());
            tokenUser.setPassword(user.getPassword());
            // System.out.println(tokenUser.getPassword());
            userServiceImp.save(tokenUser);
            modelAndView.addObject("message", "Password successfully reset. You can now log in with the new credentials.");
            modelAndView.setViewName("successResetPassword");
        } else {
            modelAndView.addObject("message","The link is invalid or broken!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }
}
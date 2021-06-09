package rs.ac.uns.ftn.bsep.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
import rs.ac.uns.ftn.bsep.domain.dto.LoginResponseDTO;
import rs.ac.uns.ftn.bsep.domain.dto.RegisterUserDTO;
import rs.ac.uns.ftn.bsep.domain.dto.ResetPasswordDTO;
import rs.ac.uns.ftn.bsep.domain.users.User;
import rs.ac.uns.ftn.bsep.service.UserService;
import rs.ac.uns.ftn.bsep.service.impl.LoggerService;


import java.io.IOException;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private LoggerService loggerService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@RequestBody LoginDTO dto) throws IOException {
        try {
            userService.login(dto);
            loggerService.logger.log(Level.INFO,"Successfully login");
            return new ResponseEntity<>(userService.login(dto), HttpStatus.OK);

        }catch (Exception e){
            loggerService.logger.log(Level.WARNING,"Bad login request");
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/forgotPassword")
    public HttpStatus forgotPassword(@RequestBody String email) {
            if(userService.sendResetPasswordRequest(email)){
                loggerService.logger.info("Sent mail for password recover");
                return HttpStatus.OK;
            }
        loggerService.logger.info("Bad request for password recovery");
        return HttpStatus.BAD_REQUEST;
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto){
        if(userService.resetPassword(dto)){
            loggerService.logger.info("Password successfully changed");
            return new ResponseEntity(HttpStatus.OK);
        }
        loggerService.logger.log(Level.WARNING,"Bad reset password request");
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/checkRequest/{id}")
    public ResponseEntity<?> checkRequest(@PathVariable("id")String id){
        if(userService.checkRequest(UUID.fromString(id))!=null){
            loggerService.logger.log(Level.INFO,"Valid reset password request");
            return new ResponseEntity(HttpStatus.OK);
        }
        loggerService.logger.log(Level.WARNING,"Invalid reset password request");
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<?> activateAccount(@PathVariable("id")String id){
        if(userService.activateAccount(UUID.fromString(id))){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO dto){
        User user= userService.register(dto);
        if(user!=null){
            loggerService.logger.log(Level.INFO,"Successfully registered");
            return new ResponseEntity<User>(user,HttpStatus.OK);
        }
        loggerService.logger.log(Level.WARNING,"Bad register request");
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }


}

package rs.ac.uns.ftn.bsep.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

      private static final Logger log = LoggerFactory.getLogger(UserController.class);


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@RequestBody LoginDTO dto, HttpServletRequest request) throws IOException {
        try {
            userService.login(dto);
            log.info("Successfully logged in "+ dto.getUsername());
            return new ResponseEntity<>(userService.login(dto), HttpStatus.OK);

        }catch (Exception e){
            log.warn("Failed to log in "+ dto.getUsername() + ", from: " + request.getHeader("Origin"));
        }
        return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
    }

    @PostMapping("/forgotPassword")
    public HttpStatus forgotPassword(@RequestBody String email) {
            if(userService.sendResetPasswordRequest(email)){
                log.info("Sent mail for password recover - " +email);
                return HttpStatus.OK;
            }
        log.warn("Bad request for password recovery - "+email );
        return HttpStatus.BAD_REQUEST;
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordDTO dto){
        if(userService.resetPassword(dto)){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @GetMapping("/checkRequest/{id}")
    public ResponseEntity<?> checkRequest(@PathVariable("id")String id){
        if(userService.checkRequest(UUID.fromString(id))!=null){
            log.info("Valid reset password request | " + id);
            return new ResponseEntity(HttpStatus.OK);
        }
        log.warn("Invalid reset password request | " +id);
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
            log.info("Successfully registered user : " + user.getUsername());
            return new ResponseEntity<User>(user,HttpStatus.OK);
        }
        log.warn("Bad register request user: "+dto.getEmail());
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }


}

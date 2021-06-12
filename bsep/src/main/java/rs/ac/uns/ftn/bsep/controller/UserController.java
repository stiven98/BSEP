package rs.ac.uns.ftn.bsep.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bsep.domain.dto.*;
import rs.ac.uns.ftn.bsep.domain.users.User;
import rs.ac.uns.ftn.bsep.service.UserService;
import rs.ac.uns.ftn.bsep.service.impl.TotpManager;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TotpManager totpManager;

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

    @PostMapping("/verifyCode")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeDTO dto){
        log.info(dto.getCode());
        if(userService.verifyCode(dto.getUsername(),dto.getCode())){
            //log.info("Valid reset password request | " + id);
            return new ResponseEntity(HttpStatus.OK);
        }
       // log.warn("Invalid reset password request | " +id);
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
    public ResponseEntity<RegisterUserResponse> register(@RequestBody RegisterUserDTO dto){
        User user= userService.register(dto);
        if(user!=null){
            log.info("Successfully registered user : " + user.getUsername());
            RegisterUserResponse response=new RegisterUserResponse(user.getUsername(),totpManager.getUriForImage(user.getSecret()));
            return new ResponseEntity<RegisterUserResponse>(response,HttpStatus.OK);
        }
        log.warn("Bad register request user: "+dto.getEmail());
        return new ResponseEntity<>(null,HttpStatus.BAD_REQUEST);
    }


}

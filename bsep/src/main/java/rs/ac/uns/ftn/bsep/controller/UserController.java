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


import java.util.UUID;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@RequestBody LoginDTO dto) {
        return new ResponseEntity<>(userService.login(dto), HttpStatus.OK);
    }

    @PostMapping("/forgotPassword")
    public HttpStatus forgotPassword(@RequestBody String email) {
            userService.sendResetPasswordRequest(email);
        return HttpStatus.OK;
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
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterUserDTO dto){
        return userService.register(dto);
    }
}

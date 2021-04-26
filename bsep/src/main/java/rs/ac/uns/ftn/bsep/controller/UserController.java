package rs.ac.uns.ftn.bsep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
import rs.ac.uns.ftn.bsep.domain.dto.LoginResponseDTO;
import rs.ac.uns.ftn.bsep.domain.dto.RegisterUserDTO;
import rs.ac.uns.ftn.bsep.domain.dto.ResetPasswordDTO;
import rs.ac.uns.ftn.bsep.domain.users.Admin;
import rs.ac.uns.ftn.bsep.domain.users.Authority;
import rs.ac.uns.ftn.bsep.domain.users.User;
import rs.ac.uns.ftn.bsep.email.EmailSender;
import rs.ac.uns.ftn.bsep.security.TokenUtils;
import rs.ac.uns.ftn.bsep.service.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> createAuthenticationToken(@RequestBody LoginDTO dto, HttpServletResponse response) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),
                        dto.getPassword()));
        // Ubaci korisnika u trenutni security kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        List<Authority> authorities = new ArrayList<>();
        user.getAuthorities().stream().forEach(a -> authorities.add((Authority) a));
        // Vrati token kao odgovor na uspesnu autentifikaciju
        LoginResponseDTO responseDTO= new LoginResponseDTO(user.getUsername(),jwt,authorities);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
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

    @PostMapping("/activate/{id}")
    public ResponseEntity<?> activateAccount(@PathVariable("id")String id){
        if(userService.activateAccount(UUID.fromString(id))){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterUserDTO dto){
        return userService.register(dto);
    }


}

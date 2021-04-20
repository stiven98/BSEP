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
import rs.ac.uns.ftn.bsep.domain.users.Authority;
import rs.ac.uns.ftn.bsep.domain.users.User;
import rs.ac.uns.ftn.bsep.security.TokenUtils;
import rs.ac.uns.ftn.bsep.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<User> createAuthenticationToken(@RequestBody LoginDTO dto, HttpServletResponse response) {
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
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

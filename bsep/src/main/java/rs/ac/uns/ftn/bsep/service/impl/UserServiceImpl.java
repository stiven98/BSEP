package rs.ac.uns.ftn.bsep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.ResetPasswordRequest;
import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
import rs.ac.uns.ftn.bsep.domain.dto.LoginResponseDTO;
import rs.ac.uns.ftn.bsep.domain.dto.RegisterUserDTO;
import rs.ac.uns.ftn.bsep.domain.dto.ResetPasswordDTO;
import rs.ac.uns.ftn.bsep.domain.users.Admin;
import rs.ac.uns.ftn.bsep.domain.users.Role;
import rs.ac.uns.ftn.bsep.domain.users.EndEntity;
import rs.ac.uns.ftn.bsep.domain.users.Intermediate;
import rs.ac.uns.ftn.bsep.domain.users.User;
import rs.ac.uns.ftn.bsep.email.EmailSender;
import rs.ac.uns.ftn.bsep.repository.dbrepository.AuthorityRepository;
import rs.ac.uns.ftn.bsep.repository.dbrepository.ResetPasswordRequestRepository;
import rs.ac.uns.ftn.bsep.repository.dbrepository.UserRepository;
import rs.ac.uns.ftn.bsep.security.TokenUtils;
import rs.ac.uns.ftn.bsep.service.UserService;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResetPasswordRequestRepository passwordRequestRepository;
    @Autowired
    private EmailSender emailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public LoginResponseDTO login(LoginDTO dto){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),
                        dto.getPassword()));
        // Ubaci korisnika u trenutni security kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        String jwt = tokenUtils.generateToken(user.getUsername());
        int expiresIn = tokenUtils.getExpiredIn();
        List<String> authorities = new ArrayList<>();
        user.getAuthorities().stream().forEach(a -> authorities.add(a.getAuthority()));
        // Vrati token kao odgovor na uspesnu autentifikaciju
        LoginResponseDTO responseDTO= new LoginResponseDTO(user.getUsername(),jwt,authorities);
        return responseDTO;
    }

    @Override
    public boolean sendResetPasswordRequest(String email) {
        User user= userRepository.findUserByEmail(email);
        if(user==null){
            return false;
        }
        ResetPasswordRequest request=new ResetPasswordRequest();
        request.setEmail(email);
        request.setId(UUID.randomUUID());
        request.setUsed(false);
        Date date=new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 1);
        request.setValidTo(cal.getTime());
        passwordRequestRepository.save(request);
        try {
            emailSender.sendForgotPasswordEmail(request.getId().toString(),email);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean resetPassword(ResetPasswordDTO dto) {
        if(!dto.getPassword().equals(dto.getPassword2())){
            return false;
        }
        ResetPasswordRequest request=checkRequest(dto.getRequestId());
        if(request!=null){
            User user= userRepository.findUserByEmail(request.getEmail());
            if(user!=null){
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                userRepository.save(user);
                request.setUsed(true);
                passwordRequestRepository.save(request);
                try {
                    emailSender.sendResetPasswordEmail(request.getEmail());
                }catch (Exception e){
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ResetPasswordRequest checkRequest(UUID id) {
        ResetPasswordRequest request= passwordRequestRepository.findById(id).orElse(null);
        if(request!=null){
            if(!request.isUsed() && !request.getValidTo().before(new Date())) {
                return  request;
            }
        }
        return null;
    }

    @Override
    public User register(RegisterUserDTO dto) {
        User user= null;
        if(!dto.getPass().equals(dto.getPass2())){
            return null;
        }
        UUID activationId=UUID.randomUUID();
        Role authorityUser= authorityRepository.findByRole("user");
        Role authorityAdmin= authorityRepository.findByRole("admin");
        switch(dto.getRole()){
            case admin:
                Admin admin=new Admin();
                admin.setPassword(passwordEncoder.encode(dto.getPass()));
                admin.setUsername(dto.getEmail());
                admin.setCommonName(dto.getCommonName());
                admin.setActivationId(activationId);
                List<Role> authorities=new ArrayList<>();
                authorities.add(authorityAdmin);
                admin.setRoles(authorities);
                user= userRepository.save(admin);
                break;
            case intermediate:
                Intermediate intermediate=new Intermediate();
                intermediate.setPassword(passwordEncoder.encode(dto.getPass()));
                intermediate.setUsername(dto.getEmail());
                intermediate.setCommonName(dto.getCommonName());
                intermediate.setActivationId(activationId);
                List<Role> authoritiesIntermediate=new ArrayList<>();
                authoritiesIntermediate.add(authorityUser);
                intermediate.setRoles(authoritiesIntermediate);
                user = userRepository.save(intermediate);
                break;
            case user:
                EndEntity endEntity=new EndEntity();
                endEntity.setPassword(passwordEncoder.encode(dto.getPass()));
                endEntity.setUsername(dto.getEmail());
                endEntity.setCommonName(dto.getCommonName());
                endEntity.setActivationId(activationId);
                List<Role> authorities1=new ArrayList<>();
                authorities1.add(authorityUser);
                endEntity.setRoles(authorities1);
                user= userRepository.save(endEntity);
                break;
        }
        if(user!=null){
            emailSender.sendActivationEmail(user.getUsername(),user.getActivationId().toString());
        }
        return user;

    }

    @Override
    public boolean activateAccount(UUID id) {
        User user= userRepository.findByActivationId(id);
        if(user!=null){
            user.setActive(true);
            userRepository.save(user);
            return  true;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }
}

package rs.ac.uns.ftn.bsep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.ResetPasswordRequest;
import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
import rs.ac.uns.ftn.bsep.domain.dto.RegisterUserDTO;
import rs.ac.uns.ftn.bsep.domain.dto.ResetPasswordDTO;
import rs.ac.uns.ftn.bsep.domain.users.Admin;
import rs.ac.uns.ftn.bsep.domain.users.EndEntity;
import rs.ac.uns.ftn.bsep.domain.users.Intermediate;
import rs.ac.uns.ftn.bsep.domain.users.User;
import rs.ac.uns.ftn.bsep.email.EmailSender;
import rs.ac.uns.ftn.bsep.repository.dbrepository.ResetPasswordRequestRepository;
import rs.ac.uns.ftn.bsep.repository.dbrepository.UserRepository;
import rs.ac.uns.ftn.bsep.service.UserService;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

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

    @Override
    public User login(LoginDTO dto){
        for(User u: userRepository.findAll()){
            if(u.getUsername().equals(dto.getUsername()) && u.getPassword().equals(dto.getPassword())){
                return u;
            }
        }
        return null;
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
        }catch(Exception e){
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
        switch(dto.getRole()){
            case admin:
                Admin admin=new Admin();
                admin.setPassword(passwordEncoder.encode(dto.getPass()));
                admin.setUsername(dto.getEmail());
                admin.setCommonName(dto.getCommonName());
                admin.setActivationId(activationId);
                user= userRepository.save(admin);
                break;
            case intermediate:
                Intermediate intermediate=new Intermediate();
                intermediate.setPassword(passwordEncoder.encode(dto.getPass()));
                intermediate.setUsername(dto.getEmail());
                intermediate.setCommonName(dto.getCommonName());
                intermediate.setActivationId(activationId);
                user = userRepository.save(intermediate);
                break;
            case user:
                EndEntity endEntity=new EndEntity();
                endEntity.setPassword(passwordEncoder.encode(dto.getPass()));
                endEntity.setUsername(dto.getEmail());
                endEntity.setCommonName(dto.getCommonName());
                endEntity.setActivationId(activationId);
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

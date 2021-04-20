package rs.ac.uns.ftn.bsep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.ResetPasswordRequest;
import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
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
    public boolean resetPassword(String email) {
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }
}

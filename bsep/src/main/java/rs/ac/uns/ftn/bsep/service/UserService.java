package rs.ac.uns.ftn.bsep.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.ac.uns.ftn.bsep.domain.ResetPasswordRequest;
import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
import rs.ac.uns.ftn.bsep.domain.dto.RegisterUserDTO;
import rs.ac.uns.ftn.bsep.domain.dto.ResetPasswordDTO;
import rs.ac.uns.ftn.bsep.domain.users.User;

import java.util.UUID;

public interface UserService extends UserDetailsService {

    User login(LoginDTO dto);
    boolean sendResetPasswordRequest(String email);
    boolean resetPassword(ResetPasswordDTO dto);
    ResetPasswordRequest checkRequest(UUID id);
    User register(RegisterUserDTO dto);
    boolean activateAccount(UUID id);
}

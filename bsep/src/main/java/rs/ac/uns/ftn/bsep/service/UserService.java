package rs.ac.uns.ftn.bsep.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
import rs.ac.uns.ftn.bsep.domain.users.User;

public interface UserService extends UserDetailsService {

    User login(LoginDTO dto);
    boolean resetPassword(String email);
}

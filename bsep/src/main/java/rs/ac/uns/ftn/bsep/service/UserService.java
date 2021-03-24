package rs.ac.uns.ftn.bsep.service;

import rs.ac.uns.ftn.bsep.domain.dto.LoginDTO;
import rs.ac.uns.ftn.bsep.domain.users.User;

public interface UserService {

    User login(LoginDTO dto);
}

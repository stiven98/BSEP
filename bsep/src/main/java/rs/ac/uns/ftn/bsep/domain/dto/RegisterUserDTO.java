package rs.ac.uns.ftn.bsep.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.bsep.domain.enums.EntityType;
import rs.ac.uns.ftn.bsep.domain.enums.Role;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDTO {
    private String email;
    private String pass;
    private String pass2;
    private String commonName;
    private Role role;
}

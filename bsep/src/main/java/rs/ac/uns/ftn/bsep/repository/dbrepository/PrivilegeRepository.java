package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.users.Privilege;

import java.security.Permission;
import java.util.UUID;

public interface PrivilegeRepository extends JpaRepository<Privilege,UUID> {
    Privilege findByName(String name);
}

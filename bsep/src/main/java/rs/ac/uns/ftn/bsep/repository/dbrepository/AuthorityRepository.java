package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.users.Authority;

import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    Authority findByRole(String role);
}

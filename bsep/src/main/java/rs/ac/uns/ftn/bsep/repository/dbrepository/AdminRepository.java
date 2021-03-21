package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.users.Admin;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}

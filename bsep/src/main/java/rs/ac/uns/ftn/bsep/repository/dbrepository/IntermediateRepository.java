package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.users.Intermediate;

import java.util.UUID;

public interface IntermediateRepository extends JpaRepository<Intermediate, UUID> {
}

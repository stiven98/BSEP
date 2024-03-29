package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.ResetPasswordRequest;

import java.util.UUID;

public interface ResetPasswordRequestRepository extends JpaRepository<ResetPasswordRequest, UUID> {
}

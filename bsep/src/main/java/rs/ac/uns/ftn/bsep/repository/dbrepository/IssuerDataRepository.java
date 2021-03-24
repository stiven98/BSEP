package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.users.IssuerData;

import java.util.UUID;

public interface IssuerDataRepository extends JpaRepository<IssuerData, UUID> {
}

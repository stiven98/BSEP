package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.users.SubjectData;

import java.util.UUID;

public interface SubjectDataRepository extends JpaRepository<SubjectData, UUID> {
}

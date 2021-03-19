package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;

import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {


}

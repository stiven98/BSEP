package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

    @Query("select s from Certificate s where s.startDate <= ?1 and s.endDate >= ?2 and s.cErtificateStatus = 0 and s.certificateType <> 1")
    List<Certificate> getALLValid(Date start, Date end);
}

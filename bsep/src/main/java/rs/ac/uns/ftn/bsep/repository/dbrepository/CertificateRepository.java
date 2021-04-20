package rs.ac.uns.ftn.bsep.repository.dbrepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface CertificateRepository extends JpaRepository<Certificate, UUID> {

    @Query("select s from Certificate s where s.startDate <= ?1 and s.endDate >= ?2 and s.certificateStatus = 0 and s.certificateType <> 1")
    List<Certificate> getALLValid(Date start, Date end);

    @Query("select c from Certificate  c where c.serialNumber = ?1")
    Certificate findBySerialNumber(String serialNumber);

    @Query("select c from Certificate  c where c.issuer = ?1")
    List<Certificate> findByIssuerSerialNumber(String serialNumber);

    @Query("SELECT new rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO(c1.subject,c.startDate,c.endDate,c.subject,c.serialNumber,c.certificateType,c.issuerType,c.organization,c.organizationUnit,c.country,c.email) from Certificate c join Certificate c1 on c.issuer=c1.serialNumber")
    List<CertificateResponseDTO> getAllWithIssuer();

    @Query("SELECT new rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO(c1.subject,c.startDate,c.endDate,c.subject,c.serialNumber,c.certificateType,c.issuerType,c.organization,c.organizationUnit,c.country,c.email) from Certificate c join Certificate c1 on c.issuer=c1.serialNumber where c.email=:email")
    List<CertificateResponseDTO> getByEmailWithIssuer(String email);
}

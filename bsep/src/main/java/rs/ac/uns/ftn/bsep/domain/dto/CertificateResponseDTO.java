package rs.ac.uns.ftn.bsep.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateStatus;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.domain.enums.EntityType;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CertificateResponseDTO {
    private String issuer;
    private Date startDate;
    private Date endDate;
    private String subject;
    private String serialNumber;
    private CertificateType certificateType;
    private CertificateType issuerType;
    private String organization;
    private String organizationUnit;
    private String country;
    private String email;

}

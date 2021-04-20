package rs.ac.uns.ftn.bsep.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    private List<String> chain;

    public CertificateResponseDTO(String issuer, Date startDate, Date endDate, String subject, String serialNumber, CertificateType certificateType, CertificateType issuerType, String organization, String organizationUnit, String country, String email) {
        this.issuer = issuer;
        this.startDate = startDate;
        this.endDate = endDate;
        this.subject = subject;
        this.serialNumber = serialNumber;
        this.certificateType = certificateType;
        this.issuerType = issuerType;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.country = country;
        this.email = email;
        this.chain=new ArrayList<>();
    }

}

package rs.ac.uns.ftn.bsep.domain.certificate;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateStatus;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.domain.enums.EntityType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Certificate {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    private String issuer;
    private Date startDate;
    private Date endDate;
    private EntityType type;
    private String subject;
    private String serialNumber;
    private CertificateType certificateType;
    private CertificateStatus certificateStatus;
    private CertificateType issuerType;
    private String commonName;
    private String firstName;
    private String lastName;
    private String organization;
    private String organizationUnit;
    private String country;
    private String email;
}



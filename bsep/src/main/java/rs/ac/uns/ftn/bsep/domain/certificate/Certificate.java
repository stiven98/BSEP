package rs.ac.uns.ftn.bsep.domain.certificate;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import rs.ac.uns.ftn.bsep.domain.enums.CErtificateStatus;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.domain.enums.EntityType;

import javax.persistence.*;
import java.security.PublicKey;
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
    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "issuer_id", referencedColumnName = "id")
    private Certificate issuer;
    private Date startDate;
    private Date endDate;
    private EntityType type;
    private String subject;
    private String signatureAlgorithm;
    private String serialNumber;
    private String version;
    private String keyUsage;
    @Column(length = 300)
    private String publicKey;
    private CertificateType certificateType;
    private CErtificateStatus cErtificateStatus;
}



package rs.ac.uns.ftn.bsep.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PrivateKey;
@Getter
@Setter
@NoArgsConstructor
public class IssuerDTO {
    private PrivateKey privateKey;
    private X500Name x500name;
    private String serialNumber;
}

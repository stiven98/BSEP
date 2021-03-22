package rs.ac.uns.ftn.bsep.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CertificateDataDTO {
    private String issuerSerialNumber;
    private CertificateType issuerType;
    private CertificateType certificateType;
    private Date startDate;
    private Date endDate;
    private String commonName;
    private String firstName;
    private String lastName;
    private String organization;
    private String organizationUnit;
    private String country;
    private String email;

    public X500Name convertToX500Name(){


        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, this.commonName);
        builder.addRDN(BCStyle.SURNAME, this.lastName);
        builder.addRDN(BCStyle.GIVENNAME, this.firstName);
        builder.addRDN(BCStyle.O, this.organization);
        builder.addRDN(BCStyle.OU, this.organizationUnit);
        builder.addRDN(BCStyle.C, this.country);
        builder.addRDN(BCStyle.E, this.email);
        return builder.build();
    }


    @Override
    public String toString() {
        return "CertificateDataDTO{" +
                "issuerSerialNumber='" + issuerSerialNumber + '\'' +
                ", issuerType=" + issuerType +
                ", certificateType=" + certificateType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", commonName='" + commonName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", organization='" + organization + '\'' +
                ", organizationUnit='" + organizationUnit + '\'' +
                ", country='" + country + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

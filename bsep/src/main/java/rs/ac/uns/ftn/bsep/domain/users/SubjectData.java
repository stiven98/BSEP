package rs.ac.uns.ftn.bsep.domain.users;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.security.PublicKey;
import java.util.UUID;

@Entity
public class SubjectData {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    private PublicKey publicKey;
    private String serialNumber;
}

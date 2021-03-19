package rs.ac.uns.ftn.bsep.domain.certificate;


import org.hibernate.annotations.GenericGenerator;
import rs.ac.uns.ftn.bsep.domain.users.User;
import rs.ac.uns.ftn.bsep.domain.enums.EntityType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class Certificate {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    @OneToOne
    @JoinColumn(referencedColumnName = "user_id", name = "issuer_id")
    private User issuer;
    @OneToOne
    @JoinColumn(referencedColumnName = "user_id", name = "subject_id")
    private User subject;
    private Date startDate;
    private Date endDate;
    private EntityType type;

}



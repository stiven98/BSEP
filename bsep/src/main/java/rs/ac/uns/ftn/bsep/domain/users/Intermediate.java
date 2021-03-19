package rs.ac.uns.ftn.bsep.domain.users;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Intermediate extends User {

    @OneToOne
    private SubjectData subjectData;

    @OneToOne
    private IssuerData issuerData;
}

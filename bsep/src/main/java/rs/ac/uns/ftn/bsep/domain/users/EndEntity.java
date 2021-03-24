package rs.ac.uns.ftn.bsep.domain.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EndEntity extends User {

    @OneToOne
    private SubjectData subjectData;
    @OneToMany
    private List<Certificate> certificates;
}

package rs.ac.uns.ftn.bsep.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class DateDTO {
    public Date startDate;
    public Date endDate;
}

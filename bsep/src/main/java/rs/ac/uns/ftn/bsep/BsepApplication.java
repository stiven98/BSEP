package rs.ac.uns.ftn.bsep;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;
import rs.ac.uns.ftn.bsep.service.impl.CertificateGeneratorServiceImpl;


@SpringBootApplication
public class BsepApplication {

	public static void main(String[] args) {
		SpringApplication.run(BsepApplication.class, args);

	}
}

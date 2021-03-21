package rs.ac.uns.ftn.bsep.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;

@RestController
@RequestMapping(value = "/api/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    @Autowired
    CertificateGeneratorService certificateGeneratorService;

    @PostMapping("/jebi")
    public ResponseEntity<?> createCertificate(){
      //  return new ResponseEntity<>("cao",HttpStatus.OK);
        return new ResponseEntity<Certificate>(certificateGeneratorService.saveCertificateInDB(new Certificate()),HttpStatus.OK);
    }

}

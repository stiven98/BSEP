package rs.ac.uns.ftn.bsep.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateDataDTO;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO;
import rs.ac.uns.ftn.bsep.domain.dto.DateDTO;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.util.List;


@RestController
@RequestMapping(value = "/api/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "http://localhost:8081")
public class CertificateController {

    @Autowired
    CertificateGeneratorService certificateGeneratorService;

    @PostMapping("/create")
    public ResponseEntity<?>CreateCertificate(@RequestBody CertificateDataDTO cet) {
        if(cet.getCertificateType() == CertificateType.root){
            if(certificateGeneratorService.generateRootCertificate(cet) != null){
                return new ResponseEntity<>("Surprise!!!", HttpStatus.OK);
            }
            else return new ResponseEntity<>("Bad luck!!!", HttpStatus.OK);
        }
        if(certificateGeneratorService.generateCertificate(cet) != null){
            return new ResponseEntity<>("Surprise!!!", HttpStatus.OK);
        }
        else return new ResponseEntity<>("Bad luck!!!", HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<CertificateResponseDTO> getAll(){
        return certificateGeneratorService.getAllWithIssuer();
    }

    @PostMapping("/validIssuers")
    public List<Certificate> getAllValidIssuers(@RequestBody DateDTO date){
        return certificateGeneratorService.getAllValidCertificates(date.getStartDate(), date.getEndDate());
    }

    @PostMapping("/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestBody String serialNumber) {
        this.certificateGeneratorService.revokeCertificate(serialNumber);
        return new ResponseEntity<>("Surprise!!!", HttpStatus.OK);
    }

    @PostMapping("/download")
    public ResponseEntity<?> downloadCertificate(@RequestBody String serialNumber) throws IOException, CertificateEncodingException {
        this.certificateGeneratorService.downloadCertificate(serialNumber);
        return  new ResponseEntity<>("Surprise", HttpStatus.OK);
    }
}

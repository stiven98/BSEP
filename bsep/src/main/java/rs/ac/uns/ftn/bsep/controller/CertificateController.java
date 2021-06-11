package rs.ac.uns.ftn.bsep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateDataDTO;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO;
import rs.ac.uns.ftn.bsep.domain.dto.DateDTO;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;
import rs.ac.uns.ftn.bsep.service.CertificateService;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    @Autowired
    CertificateGeneratorService certificateGeneratorService;

    @Autowired
    CertificateService certificateService;

    private static final Logger log = LoggerFactory.getLogger(CertificateController.class);

    @PreAuthorize("hasAuthority('WRITE_ADMIN')")
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

    @PreAuthorize("hasAuthority('READ_ADMIN')")
    @GetMapping("/all")
    public List<CertificateResponseDTO> getAll() throws IOException {
        return certificateService.getAllWithIssuer();
    }

    @PreAuthorize("hasAuthority('READ_ADMIN')")
    @PostMapping("/validIssuers")
    public List<Certificate> getAllValidIssuers(@RequestBody DateDTO date){
        return certificateService.getAllValidCertificates(date.getStartDate(), date.getEndDate());
    }

    @PreAuthorize("hasAuthority('WRITE_ADMIN')")
    @PostMapping("/revoke")
    public ResponseEntity<?> revokeCertificate(@RequestBody String serialNumber) {
        if(certificateService.revokeCertificate(serialNumber)){
            log.info("Certificate "+ serialNumber +" successfully revoked");
            return new ResponseEntity<>("Surprise!!!", HttpStatus.OK);
        }
        log.info("Failed to revoke "+ serialNumber +" certificate");
        return new ResponseEntity<>("FAILED!!!", HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('READ')")
    @PostMapping("/getByMail")
    public ResponseEntity<?> getByMail(@RequestBody String email) {
        return new ResponseEntity<>(certificateService.getByEmailWithIssuer(email), HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadCertificate(@RequestParam String serialNumber) throws IOException, CertificateEncodingException {
        X509Certificate certificate = this.certificateService.getCertificateBySerialNumber(serialNumber);
        String filename = "BSEP" + serialNumber + ".cer";
        byte[] buf = certificate.getEncoded();
        ByteArrayResource resource = new ByteArrayResource(buf);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename(filename).build());

        return ResponseEntity.ok()
                .contentLength(buf.length)
                .headers(httpHeaders)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}

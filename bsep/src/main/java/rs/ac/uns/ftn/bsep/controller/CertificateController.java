package rs.ac.uns.ftn.bsep.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.DateDTO;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/certificate", produces = MediaType.APPLICATION_JSON_VALUE)
public class CertificateController {

    @Autowired
    CertificateGeneratorService certificateGeneratorService;

    @PostMapping("/proba")
    public List<Certificate> getAllValidIssuers(@RequestBody DateDTO date){
        System.out.println(date.getDate());
        return certificateGeneratorService.getAllValidateCertificates(date.getDate());
    }


}

package rs.ac.uns.ftn.bsep.service;

import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.IssuerDTO;
import rs.ac.uns.ftn.bsep.domain.dto.SubjectDTO;

import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

public interface CertificateGeneratorService {
    X509Certificate generateCertificate(SubjectDTO subjectData, IssuerDTO issuerData, String type);
    void saveCertificate(X509Certificate certificate, String type, IssuerDTO issuerData);
    Certificate saveCertificateInDB(Certificate certificate);
    List<Certificate> getAllValidateCertificates(Date startDate);
}

package rs.ac.uns.ftn.bsep.service;

import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO;

import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

public interface CertificateService {
    List<Certificate> getAllValidCertificates(Date startDate, Date endDate);
    List<Certificate> getAll();
    List<CertificateResponseDTO> getAllWithIssuer();

    List<CertificateResponseDTO> getByEmailWithIssuer(String email);

    boolean revokeCertificate(String serialNumber);

    X509Certificate getCertificateBySerialNumber(String serialNumber) throws CertificateEncodingException, IOException;
}

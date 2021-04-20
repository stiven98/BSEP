package rs.ac.uns.ftn.bsep.service;

import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateDataDTO;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

public interface CertificateGeneratorService {
    X509Certificate generateRootCertificate(CertificateDataDTO certificateData);
    X509Certificate generateCertificate(CertificateDataDTO certificateData);
    void saveCertificate(X509Certificate certificate, CertificateType type, PrivateKey privateKey);
    Certificate saveCertificateInDB(Certificate c);
}

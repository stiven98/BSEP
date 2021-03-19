package rs.ac.uns.ftn.bsep.service.impl;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.dto.IssuerDTO;
import rs.ac.uns.ftn.bsep.domain.dto.SubjectDTO;
import rs.ac.uns.ftn.bsep.repository.dbrepository.CertificateRepository;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;
import rs.ac.uns.ftn.bsep.service.FileWriterService;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class CertificateGeneratorServiceImpl implements CertificateGeneratorService {

    @Autowired
    FileWriterService fileWriterService;

    @Autowired
    CertificateRepository certificateRepository;




    @Override
    public X509Certificate generateCertificate(SubjectDTO subjectData, IssuerDTO issuerData, String type) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());
            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            X509Certificate certificate = certConverter.getCertificate(certHolder);
            this.saveCertificate(certificate,type,issuerData);
            return certificate;
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void saveCertificate(X509Certificate certificate, String type, IssuerDTO issuerData) {
        if(type.equals("ROOT")){
            String password = "Pa33w0rd-123";
            fileWriterService.loadKeyStore("root",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(),issuerData.getPrivateKey(),password.toCharArray(),certificate);
        }else if (type.equals("INTERMEDIATE")){
            String password = "31fRaT-654";
            fileWriterService.loadKeyStore("intermediate",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(),issuerData.getPrivateKey(),password.toCharArray(),certificate);
        }else {
            String password = "528-3waGeeR";
            fileWriterService.loadKeyStore("endEntity",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(),issuerData.getPrivateKey(),password.toCharArray(),certificate);
        }
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

}

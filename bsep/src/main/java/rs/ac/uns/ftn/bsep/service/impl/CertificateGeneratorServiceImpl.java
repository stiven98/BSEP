package rs.ac.uns.ftn.bsep.service.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateDataDTO;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateStatus;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.domain.enums.EntityType;
import rs.ac.uns.ftn.bsep.repository.dbrepository.CertificateRepository;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;
import rs.ac.uns.ftn.bsep.service.FileReaderService;
import rs.ac.uns.ftn.bsep.service.FileWriterService;

import javax.security.auth.x500.X500Principal;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CertificateGeneratorServiceImpl implements CertificateGeneratorService {

    @Autowired
    FileWriterService fileWriterService;

    @Autowired
    FileReaderService fileReaderService;

    @Autowired
    CertificateRepository certificateRepository;

    @Override
    public X509Certificate generateRootCertificate(CertificateDataDTO certificateData) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");
            PrivateKey issuerPrivateKey = getPrivateKey(certificateData);
            ContentSigner contentSigner = builder.build(issuerPrivateKey);
            Date now = new Date();
            Long time = now.getTime();
            Double salt = 1000000 * Math.random();
            String serNumber = time.toString() + salt.toString();
            X500Name x500Name = new JcaX509CertificateHolder((X509Certificate) getCertificate(certificateData)).getSubject();
            KeyPair subjectKP = generateKeyPair();
            //new X500Name(getCertificate(certificateData).getIssuerX500Principal().getName(X500Principal.RFC1779));
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(x500Name,
                    new BigInteger(serNumber),
                    certificateData.getStartDate(),
                    certificateData.getEndDate(),
                    certificateData.convertToX500Name(),
                    subjectKP.getPublic());
            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            X509Certificate certificate = certConverter.getCertificate(certHolder);
            this.saveCertificate(certificate,certificateData.getCertificateType(),subjectKP.getPrivate());

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
    public X509Certificate generateCertificate(CertificateDataDTO certificateData) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            builder = builder.setProvider("BC");
            PrivateKey issuerPrivateKey = getPrivateKey(certificateData);
            ContentSigner contentSigner = builder.build(issuerPrivateKey);
            Date now = new Date();
            Long time = now.getTime();
            Double salt = 1000000 * Math.random();
            String serNumber = time.toString() + salt.toString();
            X500Name x500Name = new JcaX509CertificateHolder((X509Certificate) getCertificate(certificateData)).getSubject();
            KeyPair subjectKP = generateKeyPair();
            //new X500Name(getCertificate(certificateData).getIssuerX500Principal().getName(X500Principal.RFC1779));
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(x500Name,
                    new BigInteger(serNumber),
                    certificateData.getStartDate(),
                    certificateData.getEndDate(),
                    certificateData.convertToX500Name(),
                    subjectKP.getPublic());
            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            X509Certificate certificate = certConverter.getCertificate(certHolder);
            this.saveCertificate(certificate,certificateData.getCertificateType(),subjectKP.getPrivate());

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
    public Certificate saveCertificateInDB(X509Certificate x509Certificate) {
        Certificate c = new Certificate();
        c.setCertificateType(CertificateType.root);
        c.setEndDate(new Date());
        c.setStartDate(new Date());
        c.setCErtificateStatus(CertificateStatus.activate);
        c.setKeyUsage("digital");
        c.setSerialNumber("acoaco123");
        c.setVersion("1.0");
        KeyPair jovan= generateKeyPair();
        c.setPublicKey(jovan.getPublic().toString());
        c.setSubject("Djole");
        c.setType(EntityType.service);
        c.setIssuer(c);

        return certificateRepository.save(c);

    }

    @Override
    public void saveCertificate(X509Certificate certificate, CertificateType type, PrivateKey privateKey) {
        if(type == CertificateType.root){
            String password = "Pa33w0rd-123";
            fileWriterService.loadKeyStore("root",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(), privateKey ,password.toCharArray(),certificate);
        }else if (type == CertificateType.intermediate){
            String password = "31fRaT-654";
            fileWriterService.loadKeyStore("intermediate",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(),privateKey,password.toCharArray(),certificate);
        }else {
            String password = "528-3waGeeR";
            fileWriterService.loadKeyStore("endEntity",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(),privateKey,password.toCharArray(),certificate);
        }
    }

    private PrivateKey getPrivateKey(CertificateDataDTO certificateData){
        if(certificateData.getIssuerType() == CertificateType.root){
            String password = "Pa33w0rd-123";
            PrivateKey key = fileReaderService.readPrivateKey("root", password, certificateData.getIssuerSerialNumber(), password);
            return key;
        }else if (certificateData.getIssuerType()  == CertificateType.intermediate){
            String password = "31fRaT-654";
            PrivateKey key = fileReaderService.readPrivateKey("intermediate", password, certificateData.getIssuerSerialNumber(), password);
            return key;
        }else {
            String password = "528-3waGeeR";
            PrivateKey key = fileReaderService.readPrivateKey("endEntity", password, certificateData.getIssuerSerialNumber(), password);
            return key;
        }
    }

    private X509Certificate getCertificate(CertificateDataDTO certificateData){
        if(certificateData.getIssuerType() == CertificateType.root){
            String password = "Pa33w0rd-123";
            X509Certificate certificate = (X509Certificate) fileReaderService.readCertificate("root", password, certificateData.getIssuerSerialNumber());
            return certificate;
        }else if (certificateData.getIssuerType()  == CertificateType.intermediate){
            String password = "31fRaT-654";
            X509Certificate certificate = (X509Certificate) fileReaderService.readCertificate("intermediate", password, certificateData.getIssuerSerialNumber());
            return certificate;
        }else {
            String password = "528-3waGeeR";
            X509Certificate certificate = (X509Certificate)fileReaderService.readCertificate("endEntity", password, certificateData.getIssuerSerialNumber());
            return certificate;
        }
    }

    public KeyPair generateKeyPair() {
        try {
            //treb da bude ECC algoritam i kljuc treba da bude 256bita!!!!!
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC","SunEC");
            ECGenParameterSpec ecsp;
            ecsp = new ECGenParameterSpec("secp256r1");
            keyGen.initialize(ecsp);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isValid(Certificate certificate,Date startDate){
        if(certificate.getEndDate().after(startDate) && certificate.getCErtificateStatus()== CertificateStatus.activate){
            return true;
        }
        return false;
    }

    @Override
    public List<Certificate> getAllValidateCertificates(Date startDate){
        List<Certificate> ret=new ArrayList<>();
        for (Certificate c: certificateRepository.findAll()) {
            if(c.getCertificateType()== CertificateType.root && isValid(c, startDate)){
                System.out.println(c.getSubject());
                ret.add(c);
            }else {
                Certificate issuer=c;
                while (issuer.getCertificateType() != CertificateType.root) {
                    if (c.getCertificateType() != CertificateType.endEntity && isValid(c, startDate) && isValid(issuer.getIssuer(), startDate)  && c.getIssuer().getCertificateType()== CertificateType.root ) {
                        ret.add(c);
                        System.out.println(c.getSubject());
                    }
                    issuer=issuer.getIssuer();
                }
            }
        }
        return ret;
    }


}
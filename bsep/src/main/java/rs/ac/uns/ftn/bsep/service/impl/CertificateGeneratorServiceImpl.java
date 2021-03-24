package rs.ac.uns.ftn.bsep.service.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateDataDTO;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateStatus;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.repository.dbrepository.CertificateRepository;
import rs.ac.uns.ftn.bsep.service.CertificateGeneratorService;
import rs.ac.uns.ftn.bsep.service.FileReaderService;
import rs.ac.uns.ftn.bsep.service.FileWriterService;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.*;

@Service
public class CertificateGeneratorServiceImpl implements CertificateGeneratorService {

    @Autowired
    FileWriterService fileWriterService;

    private static final String DOWNLOAD_PATH = System.getProperty("user.home") + "\\Downloads\\";

    @Autowired
    FileReaderService fileReaderService;

    @Autowired
    CertificateRepository certificateRepository;

    @Override
    public X509Certificate generateRootCertificate(CertificateDataDTO certificateData) {
        try {

            Security.addProvider(new BouncyCastleProvider());

            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithECDSA");
            builder = builder.setProvider("BC");
            KeyPair subjectKP = generateKeyPair();
            ContentSigner contentSigner = builder.build(subjectKP.getPrivate());
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(certificateData.convertToX500Name(),
                    new BigInteger(certificateData.getSubjectSerialNumber()),
                    certificateData.getStartDate(),
                    certificateData.getEndDate(),
                    certificateData.convertToX500Name(),
                    subjectKP.getPublic());
            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            X509Certificate certificate = certConverter.getCertificate(certHolder);
            this.saveCertificate(certificate,CertificateType.root,subjectKP.getPrivate());
            Certificate db = new Certificate();
            db.setIssuer(certificateData.getSubjectSerialNumber());
            db.setIssuerType(CertificateType.root);
            db.setCertificateType(certificateData.getCertificateType());
            db.setSerialNumber(certificateData.getSubjectSerialNumber());
            db.setOrganization(certificateData.getOrganization());
            db.setEmail(certificateData.getEmail());
            db.setCertificateStatus(CertificateStatus.activate);
            db.setCountry(certificateData.getCountry());
            if(certificateData.getCommonName().isEmpty()) {
                db.setSubject(certificateData.getFirstName() + " " + certificateData.getLastName());
            }else {
                db.setSubject(certificateData.getCommonName());
            }
            db.setStartDate(certificateData.getStartDate());
            db.setEndDate(certificateData.getEndDate());
            db.setOrganizationUnit(certificateData.getOrganizationUnit());
            this.saveCertificateInDB(db);
            System.out.println("\n===== Podaci o izdavacu sertifikata =====");
            System.out.println(certificate.getIssuerX500Principal().getName());
            System.out.println("\n===== Podaci o vlasniku sertifikata =====");
            System.out.println(certificate.getSubjectX500Principal().getName());
            System.out.println("\n===== Sertifikat =====");
            System.out.println("-------------------------------------------------------");
            System.out.println(certificate);
            System.out.println("-------------------------------------------------------");

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
            Security.addProvider(new BouncyCastleProvider());

            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithECDSA");
            builder = builder.setProvider("BC");
            PrivateKey issuerPrivateKey = getPrivateKey(certificateData);
            ContentSigner contentSigner = builder.build(issuerPrivateKey);
            X500Name x500Name = new JcaX509CertificateHolder((X509Certificate) getCertificate(certificateData)).getSubject();
            KeyPair subjectKP = generateKeyPair();
            X500Name subject = certificateData.convertToX500Name();
            //new X500Name(getCertificate(certificateData).getIssuerX500Principal().getName(X500Principal.RFC1779));
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(x500Name,
                    new BigInteger(certificateData.getSubjectSerialNumber()),
                    certificateData.getStartDate(),
                    certificateData.getEndDate(),
                    subject,
                    subjectKP.getPublic());
            X509CertificateHolder certHolder = certGen.build(contentSigner);
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider("BC");
            X509Certificate certificate = certConverter.getCertificate(certHolder);
            this.saveCertificate(certificate,certificateData.getCertificateType(),subjectKP.getPrivate());
            Certificate db = new Certificate();
            db.setIssuer(certificateData.getIssuerSerialNumber());
            db.setIssuerType(certificateData.getIssuerType());
            db.setCertificateType(certificateData.getCertificateType());
            db.setSerialNumber(certificateData.getSubjectSerialNumber());
            db.setOrganization(certificateData.getOrganization());
            db.setEmail(certificateData.getEmail());
            db.setCertificateStatus(CertificateStatus.activate);
            db.setCountry(certificateData.getCountry());
            if(certificateData.getCommonName().isEmpty()) {
                db.setSubject(certificateData.getFirstName() + " " + certificateData.getLastName());
            }else {
                db.setSubject(certificateData.getCommonName());
            }
            db.setStartDate(certificateData.getStartDate());
            db.setEndDate(certificateData.getEndDate());
            db.setOrganizationUnit(certificateData.getOrganizationUnit());
            this.saveCertificateInDB(db);

            System.out.println("\n===== Podaci o izdavacu sertifikata =====");
            System.out.println(certificate.getIssuerX500Principal().getName());
            System.out.println("\n===== Podaci o vlasniku sertifikata =====");
            System.out.println(certificate.getSubjectX500Principal().getName());
            System.out.println("\n===== Sertifikat =====");
            System.out.println("-------------------------------------------------------");
            System.out.println(certificate);
            System.out.println("-------------------------------------------------------");

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
    public Certificate saveCertificateInDB(Certificate c) {
        return certificateRepository.save(c);
    }

    @Override
    public void saveCertificate(X509Certificate certificate, CertificateType type, PrivateKey privateKey) {
        if(type == CertificateType.root){
            String password = "Pa33w0rd-123";
            fileWriterService.loadKeyStore("root",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(), privateKey ,password.toCharArray(),certificate);
            fileWriterService.saveKeyStore("root",password.toCharArray());
        }else if (type == CertificateType.intermediate){
            String password = "31fRaT-654";
            fileWriterService.loadKeyStore("intermediate",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(),privateKey,password.toCharArray(),certificate);
            fileWriterService.saveKeyStore("intermediate",password.toCharArray());
        }else {
            String password = "528-3waGeeR";
            fileWriterService.loadKeyStore("endEntity",password.toCharArray());
            fileWriterService.write(certificate.getSerialNumber().toString(),privateKey,password.toCharArray(),certificate);
            fileWriterService.saveKeyStore("endEntity",password.toCharArray());
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
        if(certificate.getEndDate().after(startDate) && certificate.getCertificateStatus()== CertificateStatus.activate){
            return true;
        }
        return false;
    }

    @Override
    public List<Certificate> getAllValidCertificates(Date startDate, Date endDate){
        List<Certificate> list = new ArrayList<>();
        for(Certificate c : certificateRepository.getALLValid(startDate,endDate)){
            X509Certificate issuer = readCertificate(c.getIssuer(),c.getIssuerType());
            X509Certificate subject = readCertificate(c.getSerialNumber(),c.getCertificateType());
            if(checkSignature(issuer, subject)){
                list.add(c);
            }
        }
        return list;
    }

    private boolean checkSignature(X509Certificate issuer, X509Certificate subject){
        if(issuer.getSerialNumber() == subject.getSerialNumber()){
            try {
                Security.addProvider(new BouncyCastleProvider());
                subject.verify(issuer.getPublicKey(), "BC");
                return true;
            } catch (CertificateException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                return false;
            } catch (SignatureException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            X509Certificate newIssuer = readCertificateBlind(issuer.getSerialNumber().toString());
            try {
                Security.addProvider(new BouncyCastleProvider());
                subject.verify(issuer.getPublicKey(), "BC");
                return checkSignature(newIssuer, issuer);
            } catch (CertificateException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return false;
            } catch (InvalidKeyException e) {
                e.printStackTrace();
                return false;
            } catch (SignatureException e) {
                e.printStackTrace();
                return false;
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    @Override
    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }

    @Override
    public List<CertificateResponseDTO> getAllWithIssuer() {
        List<CertificateResponseDTO> ret=new ArrayList<>();
        for(CertificateResponseDTO dto: certificateRepository.getAllWithIssuer()){
            for(Certificate c:getChainForCertificate(dto.getSerialNumber())){
                dto.getChain().add(c.getSubject());
            }
            ret.add(dto);
        }
        return ret;
    }

    @Override
    public List<CertificateResponseDTO> getByEmailWithIssuer(String email) {
        List<CertificateResponseDTO> ret=new ArrayList<>();
        for(CertificateResponseDTO dto: certificateRepository.getByEmailWithIssuer(email)){
            for(Certificate c:getChainForCertificate(dto.getSerialNumber())){
                dto.getChain().add(c.getSubject());
            }
            ret.add(dto);
        }
        return ret;
    }

    @Override
    public boolean revokeCertificate(String serialNumber) {

        Certificate certificate = this.certificateRepository.findBySerialNumber(serialNumber);
        if(certificate.getCertificateType() == CertificateType.endEntity) {
            certificate.setCertificateStatus(CertificateStatus.revoked);
            this.certificateRepository.save(certificate);
            return true;
        }
        Set<Certificate> revokeList = new HashSet<>();
        revokeList.add(certificate);
        List<Certificate> allCertificates = this.certificateRepository.findAll();
        for (Certificate cert : allCertificates) {
            ArrayList<Certificate> chain = new ArrayList<>();
            Certificate iterator = cert;
            boolean flag = false;
            chain.add(iterator);
            while (iterator.getCertificateType() != CertificateType.root) {
                Certificate parent = this.certificateRepository.findBySerialNumber(iterator.getIssuer());
                if(parent.getSerialNumber().equals(serialNumber)) {
                    flag = true;
                    break;
                }
                iterator = parent;
                chain.add(iterator);
            }
            if(flag) {
               for(Certificate fromChain : chain) {
                   revokeList.add(fromChain);
               }
            }
            chain = new ArrayList<>();
        }
        for(Certificate forRevoke : revokeList) {
            forRevoke.setCertificateStatus(CertificateStatus.revoked);
            this.certificateRepository.save(forRevoke);
        }

        return true;
    }



    @Override
    public void downloadCertificate(String serialNumber) throws CertificateEncodingException, IOException {
        String fileName = DOWNLOAD_PATH + "BSEP" + serialNumber + ".cer";
        FileOutputStream outputStream = null;
        X509Certificate certificate = this.readCertificateBlind(serialNumber);
        File file = new File(fileName);
        byte[] buf = certificate.getEncoded();
        FileOutputStream os = new FileOutputStream(file);
        os.write(buf);
        os.close();
    }


    private X509Certificate readCertificate(String alias, CertificateType type){
        if(type == CertificateType.root){
            String password = "Pa33w0rd-123";
            X509Certificate certificate = (X509Certificate) fileReaderService.readCertificate("root", password,alias);
            return certificate;
        }else if (type == CertificateType.intermediate){
            String password = "31fRaT-654";
            X509Certificate certificate = (X509Certificate) fileReaderService.readCertificate("intermediate", password,alias);
            return certificate;
        }else {
            String password = "528-3waGeeR";
            X509Certificate certificate = (X509Certificate)fileReaderService.readCertificate("endEntity", password, alias);
            return certificate;
        }
    }

    private X509Certificate readCertificateBlind(String alias){
            String password = "Pa33w0rd-123";
            X509Certificate certificate = (X509Certificate) fileReaderService.readCertificate("root", password,alias);
            if(certificate != null){
                return certificate;
            }
            password = "31fRaT-654";
            certificate = (X509Certificate) fileReaderService.readCertificate("intermediate", password,alias);
            if(certificate != null){
                return certificate;
            }
            password = "528-3waGeeR";
            certificate = (X509Certificate)fileReaderService.readCertificate("endEntity", password, alias);
            return certificate;
    }

    private List<Certificate> getChainForCertificate(String serialNumber){
        Certificate certificate=certificateRepository.findBySerialNumber(serialNumber);
        List<Certificate> chain = new ArrayList<>();
        chain.add(certificate);
        boolean flag = false;
        chain.add(certificate);
        Certificate iterator=certificate;
        while (iterator.getCertificateType() != CertificateType.root) {
            Certificate parent = this.certificateRepository.findBySerialNumber(iterator.getIssuer());
            iterator = parent;
            chain.add(iterator);
        }
        Collections.reverse(chain);
        return chain;

    }

}

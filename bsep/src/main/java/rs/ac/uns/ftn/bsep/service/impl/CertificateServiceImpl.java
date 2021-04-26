package rs.ac.uns.ftn.bsep.service.impl;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.certificate.Certificate;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateDataDTO;
import rs.ac.uns.ftn.bsep.domain.dto.CertificateResponseDTO;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateStatus;
import rs.ac.uns.ftn.bsep.domain.enums.CertificateType;
import rs.ac.uns.ftn.bsep.repository.dbrepository.CertificateRepository;
import rs.ac.uns.ftn.bsep.service.CertificateService;
import rs.ac.uns.ftn.bsep.service.FileReaderService;
import rs.ac.uns.ftn.bsep.service.FileWriterService;

import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.util.*;

@Service
public class CertificateServiceImpl implements CertificateService {
    @Autowired
    FileWriterService fileWriterService;

    private static final String DOWNLOAD_PATH = System.getProperty("user.home") + "\\Downloads\\";

    @Autowired
    FileReaderService fileReaderService;

    @Autowired
    CertificateRepository certificateRepository;


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

    public boolean isValid(Certificate certificate, Date startDate){
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
    public X509Certificate getCertificateBySerialNumber(String serialNumber){
        X509Certificate certificate = this.readCertificateBlind(serialNumber);
        return certificate;
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

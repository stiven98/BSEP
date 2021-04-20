package rs.ac.uns.ftn.bsep.repository.filerepository.impl;

import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.bsep.repository.filerepository.FileWriterRepository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


@Repository
public class FileWriterRepositoryImpl implements FileWriterRepository {

    private KeyStore keyStore;

    public FileWriterRepositoryImpl(){
        try {
            keyStore = KeyStore.getInstance("JKS", "SUN");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public Certificate[] readCertificateChain(KeyStore keyStore, String alias, X509Certificate certificate) throws KeyStoreException {
        Certificate[] certificates = keyStore.getCertificateChain(alias);
        Certificate[] certificateChain = new Certificate[certificates.length + 1];
        certificateChain[0] = certificate;
        for(int i = 0; i < certificates.length; i ++) {
            certificateChain[i+1] = certificates[i];
        }
        return certificateChain;
    }

    @Override
    public void loadKeyStore(String fileName, char[] password) {
        try{
            if(fileName != null) {
                System.out.println("Load key store");
                keyStore.load(new FileInputStream(fileName), password);
            } else {
                keyStore.load(null, password);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            this.init(fileName,password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveKeyStore(String fileName, char[] password) {

        try {
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
        try {
            System.out.println("Write");
            keyStore.setKeyEntry(alias, privateKey, password, new Certificate[] {certificate});
            System.out.println(keyStore.size());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(String fileName, char[] password) {
        try {
            keyStore.load(null, password);
            keyStore.store(new FileOutputStream(fileName), password);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

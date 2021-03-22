package rs.ac.uns.ftn.bsep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.domain.users.IssuerData;
import rs.ac.uns.ftn.bsep.repository.filerepository.FileReaderRepository;
import rs.ac.uns.ftn.bsep.service.FileReaderService;

import java.security.PrivateKey;
import java.security.cert.Certificate;

@Service
public class FileReaderServiceImpl implements FileReaderService {

    @Autowired
    FileReaderRepository fileReaderRepository;

    @Override
    public Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias) {
        return fileReaderRepository.readCertificate(keyStoreFile,keyStorePass,alias);
    }

    @Override
    public PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass) {
        return fileReaderRepository.readPrivateKey(keyStoreFile,keyStorePass,alias,pass);
    }

    @Override
    public IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass) {
        return fileReaderRepository.readIssuerFromStore(keyStoreFile,alias,password,keyPass);
    }
}

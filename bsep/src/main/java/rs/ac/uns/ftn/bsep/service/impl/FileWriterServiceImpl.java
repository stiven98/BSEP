package rs.ac.uns.ftn.bsep.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.bsep.repository.filerepository.FileWriterRepository;
import rs.ac.uns.ftn.bsep.service.FileWriterService;

import java.security.PrivateKey;
import java.security.cert.Certificate;


@Service
public class FileWriterServiceImpl implements FileWriterService {

    @Autowired
    FileWriterRepository fileWriterRepository;

    @Override
    public void loadKeyStore(String fileName, char[] password) {
         fileWriterRepository.loadKeyStore(fileName,password);
    }

    @Override
    public void saveKeyStore(String fileName, char[] password) {
        fileWriterRepository.saveKeyStore(fileName,password);
    }

    @Override
    public void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate) {
        fileWriterRepository.write(alias,privateKey,password,certificate);
    }

    @Override
    public void init(String fileName, char[] password) {
        fileWriterRepository.init(fileName,password);
    }
}

package rs.ac.uns.ftn.bsep.service;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface FileWriterService {

    void loadKeyStore(String fileName, char[] password);
    void saveKeyStore(String fileName, char[] password);
    void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate);
    void init(String fileName, char[] password);

}

package rs.ac.uns.ftn.bsep.service;

import rs.ac.uns.ftn.bsep.domain.users.IssuerData;

import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface FileReaderService {
    Certificate readCertificate(String keyStoreFile, String keyStorePass, String alias);
    PrivateKey readPrivateKey(String keyStoreFile, String keyStorePass, String alias, String pass);
    IssuerData readIssuerFromStore(String keyStoreFile, String alias, char[] password, char[] keyPass);
}

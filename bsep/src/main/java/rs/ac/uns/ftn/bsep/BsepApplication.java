package rs.ac.uns.ftn.bsep;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rs.ac.uns.ftn.bsep.domain.dto.IssuerDTO;
import rs.ac.uns.ftn.bsep.domain.dto.SubjectDTO;
import rs.ac.uns.ftn.bsep.service.CertificateGenerator;
import rs.ac.uns.ftn.bsep.service.keystores.KeyStoreReader;
import rs.ac.uns.ftn.bsep.service.keystores.KeyStoreWriter;

import java.security.KeyPair;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class BsepApplication {

	public static void main(String[] args) {



		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		CertificateGenerator c = new CertificateGenerator();
		KeyPair keyPair = c.generateKeyPair();

		//Datumi od kad do kad vazi sertifikat
		SimpleDateFormat iso8601Formater = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = new Date();
		Date endDate = new Date();

		//Serijski broj sertifikata
		String sn="1";
		//klasa X500NameBuilder pravi X500Name objekat koji predstavlja podatke o vlasniku
		X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
		builder.addRDN(BCStyle.CN, "Goran Sladic");
		builder.addRDN(BCStyle.SURNAME, "Sladic");
		builder.addRDN(BCStyle.GIVENNAME, "Goran");
		builder.addRDN(BCStyle.O, "UNS-FTN");
		builder.addRDN(BCStyle.OU, "Katedra za informatiku");
		builder.addRDN(BCStyle.C, "RS");
		builder.addRDN(BCStyle.E, "sladicg@uns.ac.rs");
		//UID (USER ID) je ID korisnika
		builder.addRDN(BCStyle.UID, "123456");
		//Kreiraju se podaci za sertifikat, sto ukljucuje:
		// - javni kljuc koji se vezuje za sertifikat
		// - podatke o vlasniku
		// - serijski broj sertifikata
		// - od kada do kada vazi sertifikat

		SubjectDTO a = new SubjectDTO(keyPair.getPublic(),builder.build(),"123",startDate,endDate);


		KeyPair keyPair2 = c.generateKeyPair();
		IssuerDTO u = new IssuerDTO(keyPair2.getPrivate(),builder.build(),"01");


		X509Certificate sssss = c.generateCertificate(a,u);


//		System.out.println("\n===== Podaci o izdavacu sertifikata =====");
//		System.out.println(sssss.getIssuerX500Principal().getName());
//		System.out.println("\n===== Podaci o vlasniku sertifikata =====");
//		System.out.println(sssss.getSubjectX500Principal().getName());
//		System.out.println("\n===== Sertifikat =====");
//		System.out.println("-------------------------------------------------------");
//		System.out.println(sssss);
//		System.out.println("-------------------------------------------------------");


		KeyStoreWriter o = new KeyStoreWriter();

		char [] aaaaa = new char[2];
		aaaaa[0] = 'a';
		aaaaa[1] = '3';

		o.saveKeyStore("imeee",aaaaa);

		o.loadKeyStore("imeee",aaaaa);

		o.write("primer",keyPair.getPrivate(),aaaaa,sssss);

		KeyStoreReader ksr = new KeyStoreReader();

		Certificate certificate =  ksr.readCertificate("imeee","a3","primer");


		System.out.println("\n===== Sertifikat =====");
		System.out.println("-------------------------------------------------------");
		System.out.println(certificate);
		System.out.println("-------------------------------------------------------");




		SpringApplication.run(BsepApplication.class, args);
	}

}

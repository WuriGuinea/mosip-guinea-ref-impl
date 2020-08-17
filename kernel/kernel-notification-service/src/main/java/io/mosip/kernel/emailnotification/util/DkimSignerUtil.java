package io.mosip.kernel.emailnotification.util;

import java.io.File;


import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import net.markenwerk.utils.mail.dkim.Canonicalization;
import net.markenwerk.utils.mail.dkim.DkimSigner;
import net.markenwerk.utils.mail.dkim.SigningAlgorithm;
 

 


/**
* Tous droits réservés 
* ANDTHEN TECHNOLOGIE SARL
* 2020
*/
public class DkimSignerUtil {
	

		public static DkimSigner createDKimSigner() throws Exception
		{
			DkimSigner dkim=null;
			dkim= new DkimSigner("inu.gov.gn", "default",new File("/home/condeis/keyout.der") );
			dkim.setCheckDomainKey(false);
			dkim.addHeaderToSign("From");
			dkim.addHeaderToSign("To");
			dkim.addHeaderToSign("Subject");
		 
			dkim.setIdentity("nepasrepondre@inu.gov.gn" );
		//	dkim.setHeaderCanonicalization(Canonicalization.SIMPLE);
			//dkim.setBodyCanonicalization(Canonicalization.RELAXED);
			//dkim.setSigningAlgorithm(SigningAlgorithm.SHA256_WITH_RSA);
		//	dkim.setLengthParam(true);
			
		 
			 
			 
			
			
			return dkim;

	}

}

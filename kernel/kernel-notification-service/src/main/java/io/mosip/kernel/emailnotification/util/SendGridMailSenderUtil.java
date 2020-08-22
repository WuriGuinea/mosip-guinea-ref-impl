package io.mosip.kernel.emailnotification.util;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;

/**
 * 
 * @author condeis
 *
 */
public class SendGridMailSenderUtil {

	public static void mailSend(String apiKey, Mail mail) throws Exception {
		try {

			// mail.addAttachments(message.);
			SendGrid sg = new SendGrid(apiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sg.api(request);
			System.out.println(response.getStatusCode());
		//	System.out.println(response.getBody());
		//	System.out.println(response.getHeaders());
		} catch (Exception ex) {
			throw ex;
		}

	}

}

package io.mosip.kernel.emailnotification.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Attachments;

import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.emailnotification.constant.MailNotifierArgumentErrorConstants;
import io.mosip.kernel.emailnotification.constant.MailNotifierConstants;
import io.mosip.kernel.emailnotification.exception.InvalidArgumentsException;
import io.mosip.kernel.emailnotification.exception.NotificationException;
import io.mosip.kernel.emailnotification.service.impl.EmailNotificationServiceImpl;
import net.markenwerk.utils.mail.dkim.DkimMessage;
import net.markenwerk.utils.mail.dkim.DkimSigner;

/**
 * This class provides with the utility methods for email-notifier service.
 * 
 * @author Sagar Mahapatra
 * @author condeis
 * @since 1.0.0
 */
@Component
public class EmailNotificationUtils {
	Logger LOGGER = LoggerFactory.getLogger(EmailNotificationUtils.class);

	public void addAttachmentsSendGrid(MultipartFile[] attachments, Mail mail) {
		Arrays.asList(attachments).forEach(attachment -> {
			try {
				String content = new Base64().encodeAsString(attachment.getBytes());
				Attachments att = new Attachments();
				att.setFilename(attachment.getName());
				att.setType(attachment.getContentType());
				att.setContent(content);
				att.setDisposition("attachment");
				mail.addAttachments(att);
			} catch (IOException exception) {
				throw new NotificationException(exception);
			}
		});
	}

	/**
	 * This method handles argument validations.
	 * 
	 * @param mailTo
	 *            the to address to be validated.
	 * @param mailSubject
	 *            the subject to be validated.
	 * @param mailContent
	 *            the content to be validated.
	 */
	public static void validateMailArguments(String fromEmail, String[] mailTo, String mailSubject,
			String mailContent) {
		Set<ServiceError> validationErrorsList = new HashSet<>();

		if (null != fromEmail) {

			try {
				validateEmailAddress(fromEmail);
			} catch (AddressException ex) {
				validationErrorsList.add(
						new ServiceError(MailNotifierArgumentErrorConstants.SENDER_ADDRESS_NOT_FOUND.getErrorCode(),
								MailNotifierArgumentErrorConstants.SENDER_ADDRESS_NOT_FOUND.getErrorMessage()));
			}

		}

		if (mailTo == null || mailTo.length == Integer.parseInt(MailNotifierConstants.DIGIT_ZERO.getValue())) {
			validationErrorsList
					.add(new ServiceError(MailNotifierArgumentErrorConstants.RECEIVER_ADDRESS_NOT_FOUND.getErrorCode(),
							MailNotifierArgumentErrorConstants.RECEIVER_ADDRESS_NOT_FOUND.getErrorMessage()));
		} else {
			List<String> tos = Arrays.asList(mailTo);
			tos.forEach(to -> {
				try {
					validateEmailAddress(to);
				} catch (AddressException ex) {
					validationErrorsList.add(
							new ServiceError(MailNotifierArgumentErrorConstants.SENDER_ADDRESS_NOT_FOUND.getErrorCode(),
									MailNotifierArgumentErrorConstants.SENDER_ADDRESS_NOT_FOUND.getErrorMessage()));
				}
			});
		}
		if (mailSubject == null || mailSubject.trim().isEmpty()) {
			validationErrorsList
					.add(new ServiceError(MailNotifierArgumentErrorConstants.SUBJECT_NOT_FOUND.getErrorCode(),
							MailNotifierArgumentErrorConstants.SUBJECT_NOT_FOUND.getErrorMessage()));
		}
		if (mailContent == null || mailContent.trim().isEmpty()) {
			validationErrorsList
					.add(new ServiceError(MailNotifierArgumentErrorConstants.CONTENT_NOT_FOUND.getErrorCode(),
							MailNotifierArgumentErrorConstants.CONTENT_NOT_FOUND.getErrorMessage()));
		}
		if (!validationErrorsList.isEmpty()) {
			throw new InvalidArgumentsException(new ArrayList<ServiceError>(validationErrorsList));
		}
	}

	private static boolean validateEmailAddress(String emailId) throws AddressException {

		InternetAddress fromEmailAddr = new InternetAddress(emailId);
		fromEmailAddr.validate();
		return true;
	}

	public void sendMessage(String apiKey, Mail mail) {
		try {
			SendGridMailSenderUtil.mailSend(apiKey, mail);
		} catch (Exception exception) {
			// TODO Auto-generated catch block
			throw new NotificationException(exception);
		}
		// TODO Auto-generated method stub

	}

}
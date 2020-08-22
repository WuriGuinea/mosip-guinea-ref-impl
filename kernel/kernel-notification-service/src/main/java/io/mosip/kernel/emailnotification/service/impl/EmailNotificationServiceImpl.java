package io.mosip.kernel.emailnotification.service.impl;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import io.mosip.kernel.core.notification.spi.EmailNotification;
import io.mosip.kernel.emailnotification.constant.MailNotifierConstants;
import io.mosip.kernel.emailnotification.dto.ResponseDto;
import io.mosip.kernel.emailnotification.exception.NotificationException;
import io.mosip.kernel.emailnotification.util.EmailNotificationUtils;

/**
 * Service implementation class for {@link EmailNotification}.
 * 
 * @author Sagar Mahapatra
 * @since 1.0.0
 */
@Service
public class EmailNotificationServiceImpl implements EmailNotification<MultipartFile[], ResponseDto> {

	Logger LOGGER = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);
	/**
	 * Autowired reference for {@link JavaMailSender}
	 */
	@Autowired
	private JavaMailSender emailSender;

	/**
	 * Autowired reference for {@link EmailNotificationUtils}
	 */
	@Autowired
	EmailNotificationUtils emailNotificationUtils;

	/**
	 * Optionally an email address can be configured.
	 */
	@Nullable
	@Value("${unir.kernel.notification.email.from:#{null}}")
	private String fromEmailAddress;
	@Value("${unir.kernel.notification.email.apikey}")
	private String apiKey;

	@Value("${mosip.kernel.mail.proxy-mail:false}")
	private boolean isProxytrue;

	/**
	 * SendEmail
	 * 
	 * @param mailTo
	 *            email address to which mail will be sent.
	 * @param mailCc
	 *            email addresses to be cc'ed.
	 * @param mailSubject
	 *            the subject.
	 * @param mailContent
	 *            the content.
	 * @param attachments
	 *            the attachments.
	 * @return the response dto.
	 */
	@Override
	public ResponseDto sendEmail(String[] mailTo, String[] mailCc, String mailSubject, String mailContent,
			MultipartFile[] attachments) {
		ResponseDto dto = new ResponseDto();
		LOGGER.info("To Request : " + String.join(",", mailTo));
		if (!isProxytrue) {
			send(mailTo, mailCc, mailSubject, mailContent, attachments);
		}
		dto.setStatus(MailNotifierConstants.MESSAGE_SUCCESS_STATUS.getValue());
		dto.setMessage(MailNotifierConstants.MESSAGE_REQUEST_SENT.getValue());
		return dto;
	}

	@Async
	private void send(String[] mailTo, String[] mailCc, String mailSubject, String mailContent,
			MultipartFile[] attachments) {
		sendWithSendGrid(mailTo, mailCc, mailSubject, mailContent, attachments);
	}

	@Async
	private void sendWithSendGrid(String[] mailTo, String[] mailCc, String mailSubject, String mailContent,
			MultipartFile[] attachments) {
		EmailNotificationUtils.validateMailArguments(fromEmailAddress, mailTo, mailSubject, mailContent);
		/**
		 * Creates the message.
		 * 
		 */

		Email to = new Email(mailTo[0]);
		Email from = null;
		Mail mail = null;
		Content content = null;
		String subject = "";
		try {

			if (null != fromEmailAddress) {
				from = new Email(fromEmailAddress);
			}

			if (mailSubject != null) {
				subject = mailSubject;
			}
			content = new Content("text/html", mailContent);
			mail = new Mail(from, mailSubject, to, content);
			if (mailCc != null) {
				for (String cc : mailCc) {
					mail.personalization.get(0).addCc(new Email(cc));
				}
				if (null != mailTo && mailTo.length > 1) {
					for (String toCC : mailTo) {
						mail.personalization.get(0).addTo(new Email(toCC));
					}
				}
			}
		} catch (Exception exception) {
			throw new NotificationException(exception);
		}
		if (attachments != null) {

			/**
			 * Adds attachments.
			 */
			emailNotificationUtils.addAttachmentsSendGrid(attachments, mail);
		}
		/**
		 * Sends the mail.
		 */
		emailNotificationUtils.sendMessage(apiKey,mail);
	}
}

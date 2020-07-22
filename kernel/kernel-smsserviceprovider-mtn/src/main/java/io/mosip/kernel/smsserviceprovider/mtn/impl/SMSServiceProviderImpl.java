/**
 * 
 */
package io.mosip.kernel.smsserviceprovider.mtn.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import io.mosip.kernel.core.notification.exception.InvalidNumberException;
import io.mosip.kernel.core.notification.model.SMSResponseDto;
import io.mosip.kernel.core.notification.spi.SMSServiceProvider;
import io.mosip.kernel.core.util.StringUtils;
import io.mosip.kernel.smsserviceprovider.mtn.constant.SmsExceptionConstant;
import io.mosip.kernel.smsserviceprovider.mtn.constant.SmsPropertyConstant;

/**
 * @author CONDEIS
 * @since 1.1.0
 */
@Component
public class SMSServiceProviderImpl implements SMSServiceProvider {

	@Autowired
	RestTemplate restTemplate;

	
	@Value("${mosip.kernel.sms.country.code}")
private   String countryCode   ;
@Value("${mosip.kernel.sms.api}")
private   String apiUrl ;
@Value("${mosip.kernel.sms.client}")
private   String clienId ;
@Value("${mosip.kernel.sms.password}")
private String password;
@Value("${mosip.kernel.sms.sender}")
private String senderId;
@Value("${mosip.kernel.sms.affiliate}")
private String affiliate;
	@Value("${mosip.kernel.sms.unicode:1}")
	String unicode;
	@Value("${mosip.kernel.sms.number.length}")
	int numberLength;
	@Override
	public SMSResponseDto sendSms(String contactNumber, String message) {
		SMSResponseDto smsResponseDTO = new SMSResponseDto();
		validateInput(contactNumber);
		contactNumber=countryCode.concat(contactNumber);
		
		UriComponentsBuilder sms = UriComponentsBuilder.fromHttpUrl(apiUrl)
				.queryParam(SmsPropertyConstant.SENDER_ID.getProperty(), senderId)
				.queryParam(SmsPropertyConstant.PROVIDER_CLIENT.getProperty(), clienId)
				.queryParam(SmsPropertyConstant.PROVIDER_PASSWORD.getProperty(), password)
				.queryParam(SmsPropertyConstant.PROVIDER_AFFILIATE.getProperty(), affiliate)
				.queryParam(SmsPropertyConstant.SMS_MESSAGE.getProperty(), message)
				.queryParam(SmsPropertyConstant.RECIPIENT_NUMBER.getProperty(), contactNumber);
		 		 
		try {
			restTemplate.getForEntity(sms.toUriString(), String.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			throw new RuntimeException(e.getResponseBodyAsString());
		}
		smsResponseDTO.setMessage(SmsPropertyConstant.SUCCESS_RESPONSE.getProperty());
		smsResponseDTO.setStatus("success");
		return smsResponseDTO;
	}

	private void validateInput(String contactNumber) {
		if (!StringUtils.isNumeric(contactNumber) || contactNumber.length() < numberLength
				|| contactNumber.length() > numberLength) {
			throw new InvalidNumberException(SmsExceptionConstant.SMS_INVALID_CONTACT_NUMBER.getErrorCode(),
					SmsExceptionConstant.SMS_INVALID_CONTACT_NUMBER.getErrorMessage() + numberLength
							+ SmsPropertyConstant.SUFFIX_MESSAGE.getProperty());
		}
	}

}
package nl.ecbp.dare2date.Registration.services.sms;

import java.util.ArrayList;
import java.util.List;

import nl.ecbp.dare2date.Registration.entity.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;

public class ValidationSmsService implements IValidationSms {
	// Find your Account Sid and Token at twilio.com/user/account
	public String accountSid;
	public String authToken;

	public ValidationSmsService(String anAccountSid, String anAuthToken) {
		accountSid = anAccountSid;
		authToken = anAuthToken;
	}

	@Override
	public void sendValidationSms(User aUser, String aCode) throws ValidationSmsException {
		try {
			 TwilioRestClient client = new TwilioRestClient(accountSid, authToken);
			 
			// Build a filter for the SmsList
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			String num = "+31" + aUser.getTelefoonnummer().replaceFirst("^(?!00[^0])0", "");
			System.out.println("Sending to phone number: " + num);

			params.add(new BasicNameValuePair("Body", "Hallo, uw validatie code is " + aCode));
			params.add(new BasicNameValuePair("To", num));
			params.add(new BasicNameValuePair("From", "+15005550006"));
			
			SmsFactory smsFactory = client.getAccount().getSmsFactory();
			Sms sms = smsFactory.create(params);
			
			System.out.println("SID van verstuurde SMS: " + sms.getSid());
			System.out.println("BODY van verstuurde SMS: " + sms.getBody());
		} catch (TwilioRestException e) {
			e.printStackTrace();

			throw new ValidationSmsException();
		}
	}
}

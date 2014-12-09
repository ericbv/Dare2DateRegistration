package nl.ecbp.dare2date.Registration.services.sms;

import java.util.ArrayList;
import java.util.List;

import nl.ecbp.dare2date.Registration.entity.User;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.IncomingPhoneNumberFactory;
import com.twilio.sdk.resource.instance.IncomingPhoneNumber;

public class ValidationSmsService implements IValidationSms {
	// Find your Account Sid and Token at twilio.com/user/account
	public String accountSid;
	public String authToken;

	public ValidationSmsService(String anAccountSid, String anAuthToken) {
		accountSid = anAccountSid;
		authToken = anAuthToken;
	}

	@Override
	public void sendValidationSms(User aUser) throws ValidationSmsException {
		try {
			TwilioRestClient client = new TwilioRestClient(accountSid, authToken);

			// Build a filter for the IncomingPhoneNumberList
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("VoiceUrl", "http://demo.twilio.com/docs/voice.xml"));

			String num = "+31" + aUser.getTelefoonnummer().replaceFirst("^(?!00[^0])0", "");
			System.out.println("Sending to phone number: " + num);

			params.add(new BasicNameValuePair("Body", "Hallo, uw validatie code is 1234"));
			params.add(new BasicNameValuePair("To", num));
			params.add(new BasicNameValuePair("From", "+14158141829"));

			IncomingPhoneNumberFactory numberFactory = client.getAccount().getIncomingPhoneNumberFactory();
			IncomingPhoneNumber number = numberFactory.create(params);
			System.out.println(number.getSid());
		} catch (TwilioRestException e) {
			e.printStackTrace();

			throw new ValidationSmsException();
		}
	}
}

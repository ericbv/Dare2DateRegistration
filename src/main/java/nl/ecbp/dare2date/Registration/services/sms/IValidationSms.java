package nl.ecbp.dare2date.Registration.services.sms;

import nl.ecbp.dare2date.Registration.entity.User;

public interface IValidationSms {
	public void sendValidationSms(User aUser, String aCode) throws ValidationSmsException;
}

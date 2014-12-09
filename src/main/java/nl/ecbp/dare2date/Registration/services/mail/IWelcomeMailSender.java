package nl.ecbp.dare2date.Registration.services.mail;

import nl.ecbp.dare2date.Registration.entity.User;

public interface IWelcomeMailSender {
	public void sendWelcomeMail(User user);
}

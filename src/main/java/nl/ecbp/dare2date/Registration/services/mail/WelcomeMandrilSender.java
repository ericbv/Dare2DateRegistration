package nl.ecbp.dare2date.Registration.services.mail;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microtripit.mandrillapp.lutung.MandrillApi;
import com.microtripit.mandrillapp.lutung.model.MandrillApiError;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage;
import com.microtripit.mandrillapp.lutung.view.MandrillMessageStatus;
import com.microtripit.mandrillapp.lutung.view.MandrillMessage.Recipient;

import nl.ecbp.dare2date.Registration.entity.User;

@Service
public class WelcomeMandrilSender implements IWelcomeMailSender {
	MandrillApi api;
	
	@Autowired
	public WelcomeMandrilSender(MandrillApi mandril) {
		api= mandril;
	}
	
	public void sendWelcomeMail(User user) {
	   	MandrillMessage message = new MandrillMessage();
    	message.setSubject("Welkom bij dare2date");
    	message.setHtml("<h1>Welkom bij Dare2Date</h1><br />Hallo "+ user.getVoornaam()+" welkom bij dare2date!");
    	message.setFromEmail("testing@ecbp.nl");
    	message.setFromName("Testing @ ecbp");
    	
    	// add recipients
    	ArrayList<Recipient> recipients = new ArrayList<Recipient>();
    	Recipient recipient = new Recipient();
    	recipient.setEmail(user.getEmail());
    	recipient.setName(user.getVoornaam() +" "+ user.getAchternaam() );
    	recipients.add(recipient);
    	message.setTo(recipients);
    	message.setPreserveRecipients(true);
    	
    	try {
			MandrillMessageStatus[] messageStatusReports = api
			        .messages().send(message, false);
		} catch (MandrillApiError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

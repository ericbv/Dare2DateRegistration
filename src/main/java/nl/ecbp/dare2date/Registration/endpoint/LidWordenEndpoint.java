package nl.ecbp.dare2date.Registration.endpoint;

import java.math.BigInteger;
import java.util.UUID;

import nl.ecbp.dare2date.Registration.PhoneParameters;
import nl.ecbp.dare2date.Registration.RegistrationDataRequest;
import nl.ecbp.dare2date.Registration.RegistrationDataResponse;
import nl.ecbp.dare2date.Registration.RegistrationDataResult;
import nl.ecbp.dare2date.Registration.VerificationCompleteRequest;
import nl.ecbp.dare2date.Registration.VerificationCompleteResponse;
import nl.ecbp.dare2date.Registration.VerificationCompleteResult;
import nl.ecbp.dare2date.Registration.VerificationStartRequest;
import nl.ecbp.dare2date.Registration.VerificationStartResponse;
import nl.ecbp.dare2date.Registration.VerificationStartResult;
import nl.ecbp.dare2date.Registration.entity.User;
import nl.ecbp.dare2date.Registration.entity.UserRepository;
import nl.ecbp.dare2date.Registration.entity.Verification;
import nl.ecbp.dare2date.Registration.entity.VerificationRepository;
import nl.ecbp.dare2date.Registration.services.mail.IWelcomeMailSender;
import nl.ecbp.dare2date.Registration.services.postcodecheck.PostcodeCheck;
import nl.ecbp.dare2date.Registration.services.postcodecheck.PostcodeCheckException;
import nl.ecbp.dare2date.Registration.services.postcodecheck.PostcodeCheckResponse;
import nl.ecbp.dare2date.Registration.services.sms.ValidationSmsException;
import nl.ecbp.dare2date.Registration.services.sms.ValidationSmsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class LidWordenEndpoint {
	private static final String NAMESPACE_URI = "http://www.han.nl/registration";

	private UserRepository userDao;
	private VerificationRepository verificationDao;
	private PostcodeCheck postcodeService;
	private ValidationSmsService smsService;
	IWelcomeMailSender mailer;

	@Autowired
	public LidWordenEndpoint(UserRepository userDao,
			VerificationRepository verificationDao,
			PostcodeCheck postcodeService, ValidationSmsService smsService,
			IWelcomeMailSender mailer) {
		this.userDao = userDao;
		this.verificationDao = verificationDao;
		this.postcodeService = postcodeService;
		this.smsService = smsService;
		this.mailer = mailer;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationDataRequest")
	@ResponsePayload
	public RegistrationDataResponse register(
			@RequestPayload RegistrationDataRequest req) {
		// Create result and response objects.
		RegistrationDataResult result = new RegistrationDataResult();
		RegistrationDataResponse resp = new RegistrationDataResponse();
		resp.setResult(result);

		try {
			// Retrieve data from request.
			String userVoornaam = req.getInput().getName().getVoornaam();
			String userAchternaam = req.getInput().getName().getAchternaam();
			String userEmail = req.getInput().getEmail().getEmail();
			String userPostcode = req.getInput().getAdres().getPostcode();
			BigInteger userHuisnr = req.getInput().getAdres().getHuisnr();
			String userPhoneNumber = req.getInput().getPhone().getPhoneNumber();

			// Get additional information from postcode service.
			PostcodeCheckResponse response = postcodeService.doCheck(
					userPostcode, userHuisnr.intValue());
			String userStreet = response.getStreet();
			String userTown = response.getTown();

			// Register new user.
			User registerdUser = userDao.save(new User(userVoornaam,
					userAchternaam, userEmail, userPostcode, userHuisnr,
					userPhoneNumber, userTown, userStreet));

			// Set result.
			result.setSuccess(true);
			result.setUserId(registerdUser.getId());

			// Return result.
			return resp;
		} catch (PostcodeCheckException e) {
			// Set failure result
			result.setSuccess(false);
			result.setMessage("fout bij ophalen van postcode gegevens");

			return resp;
		}
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "VerificationStartRequest")
	@ResponsePayload
	public VerificationStartResponse register(
			@RequestPayload VerificationStartRequest req) {
		// Create result and response objects.
		VerificationStartResult result = new VerificationStartResult();
		VerificationStartResponse resp = new VerificationStartResponse();
		resp.setResult(result);

		// Lookup user.
		User user = userDao.findOne(req.getInput().getUserId());
		if (user == null) {
			result.setSuccess(false);
			result.setMessage("onbekende gebruiker");

			return resp;
		}

		// Send validation SMS
		try {
			// Update phone number if given.
			PhoneParameters newPhone = req.getInput().getPhoneCorrection();
			if (newPhone != null && newPhone.getPhoneNumber() != null) {
				user.setTelefoonnummer(newPhone.getPhoneNumber());
				userDao.save(user);
			}
			
			// Delete old verification if present.
			Verification verification = verificationDao.findByUserId(req.getInput()
					.getUserId());
			if(verification != null) {
				verificationDao.delete(verification);
			}

			// Generate code and store it.
			String newCode = UUID.randomUUID().toString().substring(0, 7);
			verificationDao.save(new Verification(newCode, user));

			// Send validation SMS
			smsService.sendValidationSms(user, newCode);

			// Set result
			result.setSuccess(true);
			result.setMessage("verificatie code verstuurd");
		} catch (ValidationSmsException e) {
			e.printStackTrace();

			// Set failure result
			result.setSuccess(false);
			result.setMessage("fout bij versturen van verificatiecode, geef een correct telefoonnummer mee");
		}

		// Return result
		return resp;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "VerificationCompleteRequest")
	@ResponsePayload
	public VerificationCompleteResponse register(
			@RequestPayload VerificationCompleteRequest req) {
		// Create result and response objects.
		VerificationCompleteResult result = new VerificationCompleteResult();
		VerificationCompleteResponse resp = new VerificationCompleteResponse();
		resp.setResult(result);

		// Lookup verification object by user id.
		Verification verification = verificationDao.findByUserId(req.getInput()
				.getUserId());
		if (verification == null) {
			result.setSuccess(false);
			result.setMessage("onbekende gebruiker");
			return resp;
		} else if (!verification.getCode().equals(req.getInput()
				.getVerificationCode())) {
			result.setSuccess(false);
			result.setMessage("Ongeldige code");
		} else {
			User user = verification.getUser();
			user.setValidated(true);
			verificationDao.delete(verification);
			mailer.sendWelcomeMail(user);
			// Set result
			result.setSuccess(true);
			result.setMessage("U bent geverificeerd");
		}
		// Return result
		return resp;
	}
}

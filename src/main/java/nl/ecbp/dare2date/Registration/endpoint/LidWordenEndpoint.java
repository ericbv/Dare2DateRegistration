package nl.ecbp.dare2date.Registration.endpoint;

import nl.ecbp.dare2date.Registration.*;
import nl.ecbp.dare2date.Registration.entity.User;
import nl.ecbp.dare2date.Registration.entity.UserRepository;
import nl.ecbp.dare2date.Registration.gateway.IMoviePrinter;
import nl.ecbp.dare2date.Registration.services.postcodecheck.PostcodeCheck;
import nl.ecbp.dare2date.Registration.services.postcodecheck.PostcodeCheckException;
import nl.ecbp.dare2date.Registration.services.postcodecheck.PostcodeCheckResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.math.BigInteger;

@Endpoint
public class LidWordenEndpoint {
	private static final String NAMESPACE_URI = "http://www.han.nl/registration";

	private UserRepository userDao;
	private IMoviePrinter moviePrinter;
	private PostcodeCheck postcodeService;

	@Autowired
	public LidWordenEndpoint(UserRepository userDao,
			IMoviePrinter moviePrinter,PostcodeCheck postcodeService) {
		this.userDao = userDao;
		this.moviePrinter = moviePrinter;
		this.postcodeService = postcodeService;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "RegistrationDataRequest")
	@ResponsePayload
	public RegistrationDataResponse register(
			@RequestPayload RegistrationDataRequest req) {
		try {
			String voornaam =  req.getInput().getName().getVoornaam();
			String achternaam = req.getInput().getName().getAchternaam();
			String email = req.getInput().getEmail().getEmail();
			String postcode = req.getInput().getAdres().getPostcode();
			BigInteger huisnr = req.getInput().getAdres().getHuisnr();
			String telefoonnummer = req.getInput().getPhone().getPhoneNumber();
			int Ihuisnr = huisnr.intValue();
			PostcodeCheckResponse response = postcodeService.doCheck(postcode, Ihuisnr);
			String straat = response.getStreet();
			String plaats = response.getTown();
			
	
			User registerdUser = userDao.save(new User(voornaam,achternaam,email,postcode,huisnr,telefoonnummer,plaats,straat));
	
			RegistrationDataResult result = new RegistrationDataResult();
			result.setMessage("ID of the newly created user "
					+ registerdUser.getId() +" Street and Townname are:" + straat + " " + plaats );
			RegistrationDataResponse resp = new RegistrationDataResponse();
			resp.setResult(result);
	
			return resp;
			
		} catch (PostcodeCheckException e) {
			RegistrationDataResult result = new RegistrationDataResult();
			result.setMessage("Invalid postcode");
			RegistrationDataResponse resp = new RegistrationDataResponse();
			resp.setResult(result);

			return resp;
		}
		
	
	}
	
}

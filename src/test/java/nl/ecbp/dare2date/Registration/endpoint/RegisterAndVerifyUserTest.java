package nl.ecbp.dare2date.Registration.endpoint;

import nl.ecbp.dare2date.Registration.CalculatorWsApplication;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.OutputCapture;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import java.io.StringReader;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CalculatorWsApplication.class)
@WebAppConfiguration
@IntegrationTest("server.port=8080")
public class RegisterAndVerifyUserTest {
	@Rule
	public OutputCapture output = new OutputCapture();

	private WebServiceTemplate webServiceTemplate = new WebServiceTemplate();

	@Value("${local.server.port}")
	private int serverPort;

	@Before
	public void setUp() {
		this.webServiceTemplate.setDefaultUri("http://localhost:" + this.serverPort + "/ws/");
	}
	
	@Test
	public void testRegisterAndVerifyUser() {
		this.testRegisterUser();
		this.testVerifyStart();
		//this.testVerifyDone();
	}

	public void testRegisterUser() {
		String registrationDataRequest = 
			"<reg:RegistrationDataRequest xmlns:reg=\"http://www.han.nl/registration\">      " +
			"	<input>      " +
			"		<!--You may enter the following 4 items in any order-->      " +
			"		<name>      " +
			"			<!--You may enter the following 2 items in any order-->      " +
			"			<voornaam>Test</voornaam>      " +
			"			<achternaam>Toos</achternaam>      " +
			"		</name>      " +
			"		<email>      " +
			"			<email>public@maurits.tv</email>      " +
			"		</email>      " +
			"		<phone>      " +
			"			<phoneNumber>0612345678</phoneNumber>      " +
			"		</phone>      " +
			"		<adres>      " +
			"			<!--You may enter the following 2 items in any order-->      " +
			"			<postcode>1621AA</postcode>      " +
			"			<huisnr>3</huisnr>      " +
			"		</adres>      " +
			"	</input>      " +
			"</reg:RegistrationDataRequest>      ";

		StreamSource source = new StreamSource(new StringReader(registrationDataRequest));
		StreamResult result = new StreamResult(System.out);

		this.webServiceTemplate.sendSourceAndReceiveToResult(source, result);
		assertThat(this.output.toString(), containsString("<success>true</success>"));
		assertThat(this.output.toString(), containsString("<userId>1</userId>"));
	}
	
	public void testVerifyStart() {
		String verificationStartRequest = 
			"<reg:VerificationStartRequest xmlns:reg=\"http://www.han.nl/registration\">" +
			"   <input>" +
			"      <userId>1</userId>" +
			"   </input>" +
			"</reg:VerificationStartRequest>";

			StreamSource source = new StreamSource(new StringReader(verificationStartRequest));
			StreamResult result = new StreamResult(System.out);

			this.webServiceTemplate.sendSourceAndReceiveToResult(source, result);
			assertThat(this.output.toString(), containsString("<success>true</success>"));
			assertThat(this.output.toString(), containsString("<message>verificatie code verstuurd</message>"));
	}
	
	public void testVerifyDone() {
		String verificationStartRequest = 
			"<reg:VerificationCompleteRequest xmlns:reg=\"http://www.han.nl/registration\">" +
			"   <input>" +
			"      <!--You may enter the following 2 items in any order-->" +
			"      <userId>1</userId>" +
			"      <!--Optional:-->" +
			"      <verificationCode>562c0fd</verificationCode>" +
			"   </input>" +
			"</reg:VerificationCompleteRequest>";

			StreamSource source = new StreamSource(new StringReader(verificationStartRequest));
			StreamResult result = new StreamResult(System.out);

			this.webServiceTemplate.sendSourceAndReceiveToResult(source, result);
			assertThat(this.output.toString(), containsString("<success>true</success>"));
			assertThat(this.output.toString(), containsString("<message>U bent geverificeerd</message>"));
	}
}

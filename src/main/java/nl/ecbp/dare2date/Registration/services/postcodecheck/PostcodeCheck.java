package nl.ecbp.dare2date.Registration.services.postcodecheck;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PostcodeCheck {
	private String apiKey;
	private HttpClient http;

	public PostcodeCheck(String anApiKey) {
		this.apiKey = anApiKey;
		this.http = HttpClients.custom().build();
	}

	public PostcodeCheckResponse doCheck(String aPostcode, int aHouseNumber) throws PostcodeCheckException {
		HttpGet httpget = new HttpGet("http://api.postcodeapi.nu/" + aPostcode + "/" + aHouseNumber);
		httpget.addHeader("Api-Key", apiKey);
		System.out.println("executing request" + httpget.getRequestLine());

		try {
			HttpResponse httpResponse = http.execute(httpget);
			HttpEntity httpEntity = httpResponse.getEntity();

			if (httpResponse.getStatusLine().getStatusCode() != 200) {
				throw new PostcodeCheckException("no valid response from api: " + httpResponse.getStatusLine().getReasonPhrase() + ": "
						+ IOUtils.toString(httpEntity.getContent()));
			}

			String jsonResponse = IOUtils.toString(httpEntity.getContent());

			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(jsonResponse).getAsJsonObject();

			boolean success = obj.get("success").getAsBoolean();

			if (success) {
				Gson jsonConverter = new Gson();
				PostcodeCheckResponse checkResponse = jsonConverter.fromJson(obj.get("resource").toString(), PostcodeCheckResponse.class);

				return checkResponse;
			} else {
				throw new PostcodeCheckException("api returned non success");
			}
		} catch (ClientProtocolException e) {
			throw new PostcodeCheckException("failed to connect to api: " + e.getMessage());
		} catch (IOException e) {
			throw new PostcodeCheckException("failed to connect to api: " + e.getMessage());
		}
	}
}

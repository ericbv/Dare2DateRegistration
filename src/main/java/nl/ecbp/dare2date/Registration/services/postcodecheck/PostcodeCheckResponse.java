package nl.ecbp.dare2date.Registration.services.postcodecheck;

public class PostcodeCheckResponse {
	private int house_number;
	private String street;
	private String postcode;
	private String town;
	private String municipality;
	private float latitude;
	private float longitude;
	private float x;
	private float y;
	
	public int getHouseNumber() {
		return house_number;
	}
	public String getStreet() {
		return street;
	}
	public String getPostcode() {
		return postcode;
	}
	public String getTown() {
		return town;
	}
	public String getMunicipality() {
		return municipality;
	}
	public float getLatitude() {
		return latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public float getX() {
		return x;
	}
	public float getY() {
		return y;
	}
	
}

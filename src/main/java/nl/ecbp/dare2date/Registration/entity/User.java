package nl.ecbp.dare2date.Registration.entity;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String voornaam;
	private String achternaam;
	private String email;
	private String postcode;
	private BigInteger huisnr;
	private String plaats;
	private String straat;
	private String telefoonnummer;
	private boolean validated;

	public User() {
		this.validated = false;
	}

	public User(String voornaam, String achternaam, String email,
			String postcode, BigInteger huisnr, String telefoonnummer,String plaats, String straat) {
		super();
		this.voornaam = voornaam;
		this.achternaam = achternaam;
		this.email = email;
		this.postcode = postcode;
		this.huisnr = huisnr;
		this.telefoonnummer = telefoonnummer;
		this.validated = false;
		this.plaats = plaats;
		this.straat =  straat;
	}

	public String getVoornaam() {
		return voornaam;
	}

	public void setVoornaam(String voornaam) {
		this.voornaam = voornaam;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public void setAchternaam(String achternaam) {
		this.achternaam = achternaam;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public BigInteger getHuisnr() {
		return huisnr;
	}

	public void setHuisnr(BigInteger huisnr) {
		this.huisnr = huisnr;
	}

	public String getTelefoonnummer() {
		return telefoonnummer;
	}

	public void setTelefoonnummer(String telefoonnummer) {
		this.telefoonnummer = telefoonnummer;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlaats() {
		return plaats;
	}

	public void setPlaats(String plaats) {
		this.plaats = plaats;
	}

	public String getStraat() {
		return straat;
	}

	public void setStraat(String straat) {
		this.straat = straat;
	}
	
	
	

}

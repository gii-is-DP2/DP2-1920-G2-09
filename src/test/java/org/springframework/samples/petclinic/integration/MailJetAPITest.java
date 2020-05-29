package org.springframework.samples.petclinic.integration;

import java.io.File;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.java.Log;

@Log
public class MailJetAPITest {
	private final String API_PUBLIC_KEY = "20324ecc96fa2b9f5ca67b2834a6b687";
	private final String API_SECRET_KEY = "461c1bda71139a9af5743eb2f74abc15";
	private final File BODY = new File("src/main/resources/mailjet.json");

	@Test
	public void testSendEmailSuccess() {
		RestAssured.given().auth().preemptive().basic(this.API_PUBLIC_KEY, this.API_SECRET_KEY).request()
				.contentType(ContentType.JSON).log().all().response().log().all().with().body(this.BODY).when()
				.post(" https://api.mailjet.com/v3.1/send").then().statusCode(200).assertThat()
				.body("Messages.Status", Matchers.hasItems("success"))
				.body("Messages.To.Email[0]", Matchers.hasItems("fraromgon2@gmail.com"));

	}

}

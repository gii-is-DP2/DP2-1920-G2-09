package org.springframework.samples.petclinic.integration;

import java.io.File;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.java.Log;

@Log
public class MailJetAPITest {
	private final String API_PUBLIC_KEY = "04b48d716a9b9f02bec4275d28e59612";
	private final String API_SECRET_KEY = "0a8b42f0a1a119f24541356e0588b3e0";
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

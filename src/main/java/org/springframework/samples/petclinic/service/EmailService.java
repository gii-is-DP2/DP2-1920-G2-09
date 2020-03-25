package org.springframework.samples.petclinic.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.stereotype.Service;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.errors.MailjetSocketTimeoutException;
import com.mailjet.client.resource.Emailv31;

@Service
public class EmailService {

	@Autowired
	public EmailService() {
	}

	public static final String API_PUBLIC_KEY = "04b48d716a9b9f02bec4275d28e59612";
	public static final String API_SECRET_KEY = "0a8b42f0a1a119f24541356e0588b3e0";
	public static final String FROM = "fraromgon2@gmail.com";

	public void sendEmailOfThePrescription(final Prescription p)
			throws MailjetException, MailjetSocketTimeoutException {
		MailjetClient client;
		MailjetRequest request;
		MailjetResponse response;

		Vet vet = p.getVet();
		Owner to = p.getPet().getOwner();
		Pet pet = p.getPet();

		client = new MailjetClient(EmailService.API_PUBLIC_KEY, EmailService.API_SECRET_KEY, new ClientOptions("v3.1"));
		request = new MailjetRequest(Emailv31.resource).property(Emailv31.MESSAGES,
				new JSONArray().put(new JSONObject()
						.put(Emailv31.Message.FROM,
								new JSONObject().put("Email", EmailService.FROM).put("Name",
										vet.getFirstName() + " " + vet.getLastName()))
						.put(Emailv31.Message.TO,
								new JSONArray().put(new JSONObject().put("Email", to.getUser().getEmail()).put("Name",
										to.getFirstName())))
						.put(Emailv31.Message.SUBJECT, "Your pet: " + p.getPet().getName() + ", has a new prescription")
						.put(Emailv31.Message.TEXTPART, "")
						.put(Emailv31.Message.HTMLPART, "<h2> Hello " + to.getFirstName() + "</h2><br>"
								+ "<h3> Your pet: " + pet.getName() + ", has a new prescription. </h3>"
								+ "<ul> <li> <b>Start Date: <b>" + p.getDateInicio().toString() + "</li>"
								+ "<li> <b>End Date: <b>" + p.getDateFinal().toString() + "</li>"
								+ "<li> <b>Description: <b>" + p.getDescription() + "</li>"
								+ "<li> <b> Prescribed by: <b>" + vet.getFirstName() + " " + vet.getLastName()
								+ "</li> </ul> <br> To see more details of the prescription, go to your profile in the PetClinic Web")
						.put(Emailv31.Message.CUSTOMID, "AppGettingStartedTest")));
		response = client.post(request);
	}
}

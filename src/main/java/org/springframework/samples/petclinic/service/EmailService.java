package org.springframework.samples.petclinic.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.samples.petclinic.model.Prescription;
import org.springframework.samples.petclinic.model.api.From;
import org.springframework.samples.petclinic.model.api.Message;
import org.springframework.samples.petclinic.model.api.Messages;
import org.springframework.samples.petclinic.model.api.To;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

	@Autowired
	public EmailService() {
	}

	public static final String API_PUBLIC_KEY = "20324ecc96fa2b9f5ca67b2834a6b687";
	public static final String API_SECRET_KEY = "461c1bda71139a9af5743eb2f74abc15";
	public static final String FROM = "jromerogr11@gmail.com";

	@SuppressWarnings("deprecation")
	public void sendEmailOfPrescriptionWithRestTemplate(final Prescription p) {
		RestTemplate restTemplate = new RestTemplate();

		Messages email = new Messages();
		From from = new From();
		To to = new To();
		Message message = new Message();

		from.setEmail(EmailService.FROM);
		from.setName(p.getVet().getFirstName() + " " + p.getVet().getLastName());

		to.setEmail(p.getPet().getOwner().getUser().getEmail());
		to.setName(p.getPet().getOwner().getFirstName());
		List<To> listReceptores = new ArrayList<>();
		listReceptores.add(to);

		message.setFrom(from);
		message.setTo(listReceptores);
		message.setSubject("Your pet: " + p.getPet().getName() + ", has a new prescription");
		message.setTextPart("");
		message.setHTMLPart("<h2> Hello " + p.getPet().getOwner().getFirstName() + "</h2><br>" + "<h3> Your pet: "
				+ p.getPet().getName() + ", has a new prescription. </h3>" + "<ul> <li> <b>Start Date: <b>"
				+ p.getDateInicio().toString() + "</li>" + "<li> <b>End Date: <b>" + p.getDateFinal().toString()
				+ "</li>" + "<li> <b>Description: <b>" + p.getDescription() + "</li>" + "<li> <b> Prescribed by: <b>"
				+ p.getVet().getFirstName() + " " + p.getVet().getLastName()
				+ "</li> </ul> <br> To see more details of the prescription, go to your profile in the PetClinic Web");

		List<Message> listaEmails = new ArrayList<>();
		listaEmails.add(message);
		email.setMessages(listaEmails);

		restTemplate.getInterceptors()
				.add(new BasicAuthorizationInterceptor(EmailService.API_PUBLIC_KEY, EmailService.API_SECRET_KEY));

		ResponseEntity<Messages> response = restTemplate.postForEntity("https://api.mailjet.com/v3.1/send", email,
				Messages.class);
		Assert.assertTrue(response.getStatusCode().equals(HttpStatus.OK));
	}
}

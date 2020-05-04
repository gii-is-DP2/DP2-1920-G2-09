
package org.springframework.samples.petclinic.model.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "From", "To", "Subject", "TextPart", "HTMLPart" })
public class Message {

	@JsonProperty("From")
	private From from;
	@JsonProperty("To")
	private List<To> to = null;
	@JsonProperty("Subject")
	private String subject;
	@JsonProperty("TextPart")
	private String textPart;
	@JsonProperty("HTMLPart")
	private String hTMLPart;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("From")
	public From getFrom() {
		return this.from;
	}

	@JsonProperty("From")
	public void setFrom(final From from) {
		this.from = from;
	}

	@JsonProperty("To")
	public List<To> getTo() {
		return this.to;
	}

	@JsonProperty("To")
	public void setTo(final List<To> to) {
		this.to = to;
	}

	@JsonProperty("Subject")
	public String getSubject() {
		return this.subject;
	}

	@JsonProperty("Subject")
	public void setSubject(final String subject) {
		this.subject = subject;
	}

	@JsonProperty("TextPart")
	public String getTextPart() {
		return this.textPart;
	}

	@JsonProperty("TextPart")
	public void setTextPart(final String textPart) {
		this.textPart = textPart;
	}

	@JsonProperty("HTMLPart")
	public String getHTMLPart() {
		return this.hTMLPart;
	}

	@JsonProperty("HTMLPart")
	public void setHTMLPart(final String hTMLPart) {
		this.hTMLPart = hTMLPart;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(final String name, final Object value) {
		this.additionalProperties.put(name, value);
	}

}


package org.springframework.samples.petclinic.model.api;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "Email", "Name" })
public class From {

	@JsonProperty("Email")
	private String email;
	@JsonProperty("Name")
	private String name;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("Email")
	public String getEmail() {
		return this.email;
	}

	@JsonProperty("Email")
	public void setEmail(final String email) {
		this.email = email;
	}

	@JsonProperty("Name")
	public String getName() {
		return this.name;
	}

	@JsonProperty("Name")
	public void setName(final String name) {
		this.name = name;
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

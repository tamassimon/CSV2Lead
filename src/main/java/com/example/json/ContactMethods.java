package com.example.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactMethods {
	List<ContactMethod> contactmethods;

	public List<ContactMethod> getContactmethods() {
		return contactmethods;
	}

	public void setContactmethods(List<ContactMethod> contactmethods) {
		this.contactmethods = contactmethods;
	}
	
}

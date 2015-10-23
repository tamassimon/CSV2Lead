package com.example.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactMethodsEmbedded {
	int count;
	ContactMethods _embedded;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public ContactMethods get_embedded() {
		return _embedded;
	}
	public void set_embedded(ContactMethods _embedded) {
		this._embedded = _embedded;
	}
	
}

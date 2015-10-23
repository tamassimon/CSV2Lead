package com.example.json;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticatedStaff {
	String clubUUID;
	String staffUUID;
	
	// getters and setters
	
	public String getClubUUID() {
		return clubUUID;
	}
	public void setClubUUID(String clubUUID) {
		this.clubUUID = clubUUID;
	}
	public String getStaffUUID() {
		return staffUUID;
	}
	public void setStaffUUID(String staffUUID) {
		this.staffUUID = staffUUID;
	}
	
}

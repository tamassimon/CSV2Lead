package com.example.json;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeadSources {
	List<LeadSource> leadsources;

	public List<LeadSource> getLeadsources() {
		return leadsources;
	}

	public void setLeadsources(List<LeadSource> leadsources) {
		this.leadsources = leadsources;
	}
	
}

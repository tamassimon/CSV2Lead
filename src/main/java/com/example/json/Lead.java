package com.example.json;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Lead {
	Contact contact;
	Staff owner;
	Product product;
	String productDetails;
	LeadSource leadSource;
	String leadSourceDetails;
	ContactMethod contactMethod;
	Contact referrer;
	String hasVisited;
	Date firstVisitedDate;
	Date trialStartDate;
	
	// getters and setters
	
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public Staff getOwner() {
		return owner;
	}
	public void setOwner(Staff owner) {
		this.owner = owner;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public String getProductDetails() {
		return productDetails;
	}
	public void setProductDetails(String productDetails) {
		this.productDetails = productDetails;
	}
	public LeadSource getLeadSource() {
		return leadSource;
	}
	public void setLeadSource(LeadSource leadSource) {
		this.leadSource = leadSource;
	}
	public String getLeadSourceDetails() {
		return leadSourceDetails;
	}
	public void setLeadSourceDetails(String leadSourceDetails) {
		this.leadSourceDetails = leadSourceDetails;
	}
	public ContactMethod getContactMethod() {
		return contactMethod;
	}
	public void setContactMethod(ContactMethod contactMethod) {
		this.contactMethod = contactMethod;
	}
	public Contact getReferrer() {
		return referrer;
	}
	public void setReferrer(Contact referrer) {
		this.referrer = referrer;
	}
	public String getHasVisited() {
		return hasVisited;
	}
	public void setHasVisited(String hasVisited) {
		this.hasVisited = hasVisited;
	}
	public Date getFirstVisitedDate() {
		return firstVisitedDate;
	}
	public void setFirstVisitedDate(Date firstVisitedDate) {
		this.firstVisitedDate = firstVisitedDate;
	}
	public Date getTrialStartDate() {
		return trialStartDate;
	}
	public void setTrialStartDate(Date trialStartDate) {
		this.trialStartDate = trialStartDate;
	}
	
	@Override
	public String toString() {
		return "Lead [contact=" + contact + ", owner=" + owner + "]";
	}
	
	// TODO: history notes	
	
}

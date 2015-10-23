package com.example;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.json.AuthenticatedStaff;
import com.example.json.Contact;
import com.example.json.ContactMethod;
import com.example.json.ContactMethodsEmbedded;
import com.example.json.Counter;
import com.example.json.Lead;
import com.example.json.LeadSource;
import com.example.json.LeadSourcesEmbedded;
import com.example.json.BadRequestLoggingResponseErrorHandler;
import com.example.json.Product;
import com.example.json.Staff;

@Component
public class CSV2LeadClient implements CommandLineRunner {

	@Value("${urls.api}")
	String apiURL;

	@Value("${urls.auth}")
	String authURL;

	@Value("${auth.clientid}")
	String clientID;

	@Value("${auth.clientsecret}")
	String clientSecret;

	@Value("${auth.granttype}")
	String grantType;

	@Value("${auth.scope}")
	String scope;
	
	private Logger logger = LoggerFactory.getLogger(CSV2LeadClient.class);

	RestTemplate getRestTemplate() {

		List<String> scopes = new ArrayList<>();
		scopes.add(scope);

		ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
		resourceDetails.setAccessTokenUri(authURL);
		resourceDetails.setClientId(clientID);
		resourceDetails.setClientSecret(clientSecret);
		resourceDetails.setGrantType(grantType);
		resourceDetails.setScope(scopes);

		// ClientCredentialsAccessTokenProvider provider = new
		// ClientCredentialsAccessTokenProvider();
		// OAuth2AccessToken accessToken =
		// provider.obtainAccessToken(resourceDetails, new
		// DefaultAccessTokenRequest());

		// OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(
		// resourceDetails, new DefaultOAuth2ClientContext(accessToken));

		OAuth2RestTemplate oauth2RestTemplate = new OAuth2RestTemplate(resourceDetails);
		
		// set our custom error handler to see error codes in the log
		oauth2RestTemplate.setErrorHandler(new BadRequestLoggingResponseErrorHandler());

		oauth2RestTemplate.setInterceptors(Collections.singletonList(new ClubUUIDInterceptor()));

		return oauth2RestTemplate;
	}

	@Override
	public void run(String... args) throws Exception {

		String clubsURI = apiURL + "/clubs";
		String leadsURI = apiURL + "/leads";
		String meURI = apiURL + "/me";
		String leadSourcesURI = apiURL + "/leadsources";
		String contactMethodsURI = apiURL + "/contactmethods";

		RestTemplate restTemplate = getRestTemplate();

		// get number of clubs
		Counter clubCounter = restTemplate.getForObject(clubsURI, Counter.class);
		Assert.isTrue(clubCounter.count == 1);

		// get the staff's UUID
		AuthenticatedStaff authenticatedStaff = restTemplate.getForObject(meURI, AuthenticatedStaff.class);
		logger.info("Staff UUID: " + authenticatedStaff.getStaffUUID());

		// get the list of lead sources and contact methods to be able to check
		// the ID against
		LeadSourcesEmbedded leadSourcesEmbedded = restTemplate.getForObject(leadSourcesURI, LeadSourcesEmbedded.class);
		ContactMethodsEmbedded contactMethodsEmbedded = restTemplate.getForObject(contactMethodsURI,
				ContactMethodsEmbedded.class);

		// create the staff as the logged on user
		Staff staff = new Staff();
		staff.setUuid(authenticatedStaff.getStaffUUID());

		Product product = new Product();
		product.setId("MEMBERSHIP");

		LeadSource leadSource = new LeadSource();
		leadSource.setId("IMPORT");
		Assert.isTrue(leadSourcesEmbedded.get_embedded().getLeadsources().contains(leadSource));

		ContactMethod contactMethod = new ContactMethod();
		contactMethod.setId("WEB LEAD");
		Assert.isTrue(contactMethodsEmbedded.get_embedded().getContactmethods().contains(contactMethod));

		// try reading in CSV files passed in on the command line

		for (String csvFileName : args) {

			// get number of leads
			Counter numberOfLeadsBefore = restTemplate.getForObject(leadsURI, Counter.class);
			logger.info("Number of leads before import: " + numberOfLeadsBefore.count);

			Reader in = new FileReader(csvFileName);
			Iterable<CSVRecord> records = CSVFormat.EXCEL.withHeader().parse(in);
			int recordCount = 0;
			int errorCount = 0;
			
			for (CSVRecord record : records) {
				recordCount++;

				String lastName = record.get("Last Name");
				String firstName = record.get("First Name");
				String email = record.get("Email");
				String mobilePhone = record.get("Mobile Phone");

				Contact csvContact = new Contact();
				csvContact.setFirstName(firstName);
				csvContact.setLastName(lastName);
				csvContact.setEmail(email);
				csvContact.setMobilePhone(mobilePhone);

				Lead csvLead = new Lead();
				csvLead.setContact(csvContact);
				csvLead.setOwner(staff);
				csvLead.setProduct(product);
				csvLead.setLeadSource(leadSource);
				csvLead.setContactMethod(contactMethod);

				// create my CSV lead
				try {
					logger.info("Creating LEAD:" + csvLead + "...");
					Lead createdLead = restTemplate.postForObject(leadsURI, csvLead, Lead.class);
					logger.info("...created LEAD:" + createdLead);
				} catch (RestClientException rce) {
					logger.error(rce.toString());
					errorCount++;
				}
			}

			// get number of leads
			Counter numberOfLeadsAfter = restTemplate.getForObject(leadsURI, Counter.class);
			logger.info("Number of leads after import: " + numberOfLeadsAfter.getCount());

			// Note: duplicates do not result in new leads therefore the assertion below could fail:
			// Assert.isTrue((numberOfLeadsAfter.count - numberOfLeadsBefore.count) == recordCount);
			
			Assert.isTrue(numberOfLeadsAfter.getCount() >= numberOfLeadsBefore.getCount()) ;
			
			logger.info("New leads created: " + ( numberOfLeadsAfter.getCount() - numberOfLeadsBefore.getCount()) );
			logger.info("Errors: " + errorCount );
			logger.info("Duplicates: " + ( recordCount - errorCount - numberOfLeadsAfter.getCount() + numberOfLeadsBefore.getCount()));
		}

	}

}

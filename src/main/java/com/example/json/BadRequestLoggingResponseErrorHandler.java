package com.example.json;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

public class BadRequestLoggingResponseErrorHandler extends DefaultResponseErrorHandler implements ResponseErrorHandler {
	private static final Logger log = LoggerFactory.getLogger(BadRequestLoggingResponseErrorHandler.class);

	@Override
	public void handleError(ClientHttpResponse response) throws IOException {

		if (response.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
			String responseBody = IOUtils.toString(response.getBody());
			log.error("Response error: {} {}", response.getStatusCode(), responseBody);
		}

		super.handleError(response);
	}

}

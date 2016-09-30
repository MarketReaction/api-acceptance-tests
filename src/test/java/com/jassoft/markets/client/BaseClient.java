package com.jassoft.markets.client;

import com.jassoft.markets.client.exception.ApiClientException;
import com.jayway.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by jonshaw on 11/05/2016.
 */
public abstract class BaseClient<T> {

    protected T entity;

    private static final Logger LOG = LoggerFactory.getLogger(BaseClient.class);

    protected static final String BASE_API_URL = "http://api:8080/";
//    protected static final String BASE_API_URL = "http://localhost:32836/";

    protected static final String HEADER_SECURITY_TOKEN = "X-AuthToken";
    protected static final String HEADER_SECURITY_EMAIL = "X-AuthEmail";

    public T getEntity() {
        return entity;
    }

    protected T validateResponseAndReturn(final Response response, final int expectedStatus, final String errorMessage, final Class returnType) throws ApiClientException {

        if (response.getStatusCode() != expectedStatus) {
            throw new ApiClientException(String.format("%s. Expected status code [%s] but got [%s]. Status Message [%s]", errorMessage, expectedStatus, response.getStatusCode(), response.getStatusLine()));
        }

        if(returnType != null) {
            return (T) response.as(returnType);
        }
        else {
            return null;
        }

    }

}

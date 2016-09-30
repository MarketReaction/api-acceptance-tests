package com.jassoft.markets.client;

import com.jassoft.markets.client.exception.ApiClientException;
import com.jassoft.markets.datamodel.company.Exchange;
import com.jassoft.markets.datamodel.company.Exchanges;
import com.jassoft.markets.datamodel.user.User;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class ExchangeClient extends BaseClient<Exchange> {

    public Exchanges retrieveExchanges() throws ApiClientException {

        User user = UserClient.getAuthenticatedUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get("/exchange")
                .thenReturn();

        if (response.getStatusCode() != 200) {
            throw new ApiClientException(String.format("Failed to get Exchanges. Status [%s] Message [%s]", response.statusCode(), response.getStatusLine()));
        }

        return response.as(Exchanges.class);
    }

    public Exchanges retrieveAllExchanges() throws ApiClientException {

        User user = UserClient.getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get("/exchange/all")
                .thenReturn();

        if (response.getStatusCode() != 200) {
            throw new ApiClientException(String.format("Failed to get Exchanges. Status [%s] Message [%s]", response.statusCode(), response.getStatusLine()));
        }

        return response.as(Exchanges.class);
    }

    public ExchangeClient retrieveExchange(String id) throws ApiClientException {

        User user = UserClient.getAuthenticatedUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/exchange/%s", id))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to retrieve Exchange", Exchange.class);

        return this;
    }

    public ExchangeClient enableExchange() throws ApiClientException {

        User user = UserClient.getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/exchange/%s/enable", getEntity().getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to enable Exchange", Exchange.class);

        return this;
    }

    public ExchangeClient disableExchange() throws ApiClientException {

        User user = UserClient.getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/exchange/%s/disable", getEntity().getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to disable Exchange", Exchange.class);

        return this;
    }
}

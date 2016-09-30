package com.jassoft.markets.client;

import com.jassoft.markets.client.exception.ApiClientException;
import com.jassoft.markets.datamodel.user.User;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class SystemClient extends BaseClient {

    public SystemClient validateApiStatus() throws Exception {

        int count = 0;

        while(true) {

            if(count > 120) {
                throw new Exception("API not started");
            }

            try {

                User user = new UserClient().getAdminUser().authenticateUser().getEntity();

                ExtractableResponse<Response> response = given().header(HEADER_SECURITY_TOKEN, user.getToken())
                        .header(HEADER_SECURITY_EMAIL, user.getEmail())
                        .baseUri(BASE_API_URL)
                        .contentType(ContentType.JSON)
                        .when().get("/system/status")
                        .then().extract();

                System.out.println("API Status returned status code [" + response.statusCode() + "] - [" + response.statusLine() + "]");

                if(response.statusCode() == 200) {
                    break;
                }
            }
            catch (ApiClientException exception) {
                throw exception;
            }
            catch (Exception exception) {
                System.out.println(String.format("Waiting for API to start. Error [%s]", exception.getMessage()));
            }
            Thread.sleep(3000);

            count++;
        }

        return this;
    }

    public List<String> getQueueNames() throws ApiClientException {
        User user = UserClient.getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get("/system/queues")
                .thenReturn();

        if (response.getStatusCode() != 200) {
            throw new ApiClientException("Failed to get Queues");
        }

        return response.as(List.class);
    }

    public Map<String, String> getQueueStats(String queueName) throws ApiClientException {
        User user = UserClient.getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get("/system/queue/Queue/" + queueName)
                .thenReturn();

        if (response.getStatusCode() != 200) {
            throw new ApiClientException(String.format("Failed to get Queue Stats. Status Code [%s] Status Line [%s]", response.statusCode(), response.statusLine()));
        }

        return response.as(Map.class);
    }
}

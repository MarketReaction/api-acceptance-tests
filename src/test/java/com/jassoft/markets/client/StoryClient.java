package com.jassoft.markets.client;

import com.jassoft.markets.client.exception.ApiClientException;
import com.jassoft.markets.datamodel.company.Exchange;
import com.jassoft.markets.datamodel.sources.Source;
import com.jassoft.markets.datamodel.story.Stories;
import com.jassoft.markets.datamodel.user.User;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class StoryClient extends BaseClient<Exchange> {

    public Stories getStoriesForSource(Source source) throws ApiClientException {

        User user = UserClient.getAuthenticatedUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/story/source/%s", source.getId()))
                .thenReturn();

        if (response.getStatusCode() != 200) {
            throw new ApiClientException(String.format("Failed to get Stories for Source [%s]. Status [%s] Message [%s]", source.getName(), response.statusCode(), response.getStatusLine()));
        }

        return response.as(Stories.class);
    }

}

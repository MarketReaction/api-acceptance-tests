package com.jassoft.markets.client;

import com.jassoft.markets.client.exception.ApiClientException;
import com.jassoft.markets.datamodel.sources.Source;
import com.jassoft.markets.datamodel.sources.SourceBuilder;
import com.jassoft.markets.datamodel.sources.SourceType;
import com.jassoft.markets.datamodel.user.User;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by jonshaw on 04/07/2016.
 */
public class SourceClient extends BaseClient<Source> {

    private RequestSpecification getAdminRrequest() throws ApiClientException {
        User user = UserClient.getAuthenticatedAdminUser();

        return given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken());
    }

    public SourceClient createSource() throws ApiClientException {

        String uniqueId = UUID.randomUUID().toString();

        Source source = SourceBuilder.aSource()
                .withName(String.format("TestSource-%s", uniqueId))
                .withType(SourceType.Crawler)
                .build();

        Response response = getAdminRrequest()
                .body(source, ObjectMapperType.GSON)
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().post("/source/add")
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Create Source", Source.class);

        return this;
    }

    public SourceClient withUrl(String urlToAdd) throws ApiClientException {

        Response response = getAdminRrequest()
                .body(urlToAdd)
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().post(String.format("/source/%s/url/add", entity.getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Add URL to Source", Source.class);

        return this;
    }

    public SourceClient enableUrl(String urlToAdd) throws ApiClientException {

        Response response = getAdminRrequest()
                .body(urlToAdd)
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().post(String.format("/source/%s/url/enable", entity.getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Enable URL to Source", Source.class);

        return this;
    }

    public SourceClient enable() throws ApiClientException {

        Response response = getAdminRrequest()
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/source/%s/enable", entity.getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Enable Source", Source.class);

        return this;
    }

    public SourceClient disable() throws ApiClientException {

        Response response = getAdminRrequest()
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/source/%s/disable", entity.getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Disable Source", Source.class);

        return this;
    }

}
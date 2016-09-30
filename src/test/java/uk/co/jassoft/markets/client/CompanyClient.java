package uk.co.jassoft.markets.client;

import uk.co.jassoft.markets.client.exception.ApiClientException;
import uk.co.jassoft.markets.datamodel.company.Company;
import uk.co.jassoft.markets.datamodel.user.User;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class CompanyClient extends BaseClient<Company> {

    public CompanyClient findCompanies() throws ApiClientException {

        User user = UserClient.getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, user.getEmail())
                .header(HEADER_SECURITY_TOKEN, user.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get("/company/triggerFind")
                .thenReturn();

        validateResponseAndReturn(response, 200, "Failed to trigger FindCompanies", null);

        return this;
    }
}

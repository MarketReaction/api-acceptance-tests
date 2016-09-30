package uk.co.jassoft.markets.client;

import uk.co.jassoft.markets.client.exception.ApiClientException;
import uk.co.jassoft.markets.datamodel.user.User;
import uk.co.jassoft.markets.datamodel.user.UserBuilder;
import uk.co.jassoft.markets.datamodel.user.Users;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.internal.mapper.ObjectMapperType;
import com.jayway.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.jayway.restassured.RestAssured.given;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class UserClient extends BaseClient<User> {

    public UserClient getAdminUser() throws ApiClientException {
        entity = UserBuilder.anUser()
                .withEmail("admin.user@market-reaction.com")
                .withPassword("password")
                .build();
        return this;
    }

    public UserClient createUser() throws ApiClientException {

        String uniqueId = UUID.randomUUID().toString();

        User createUser = UserBuilder.anUser()
                .withEmail(String.format("test.user.%s@market-reaction.com", uniqueId))
                .withForename("Test")
                .withSurname(String.format("User_%s", uniqueId))
                .withPassword(String.format("password_%s", uniqueId))
                .build();

        return createUser(createUser);
    }

    public UserClient createUser(User createUser) throws ApiClientException {

        Response response = given().body(createUser, ObjectMapperType.GSON)
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().post("/user/register")
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Create User", User.class);

        return this;
    }

    public UserClient activateUser() throws ApiClientException {

        // TODO - this is a temp hack until a solution of getting activationId is found.
        adminActivateUser();

//        String activationId = "";
//
//        Response response = given().body(activationId)
//                .contentType(ContentType.JSON)
//                .baseUri(BASE_API_URL)
//                .when().post("/user/register/confirm")
//                .thenReturn();
//
//        user = validateResponseAndReturn(response, 200, "Failed to Activate User", User.class);

        return this;
    }

    public UserClient authenticateUser() throws ApiClientException {

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", getEntity().getEmail());

        if(getEntity().getPassword() == null) {
            credentials.put("password", String.format("password_%s", getEntity().getEmail().replace("test.user.", "").replace("@market-reaction.com", "")));
        }
        else {
            credentials.put("password", getEntity().getPassword());
        }

        Response response = given().body(credentials)
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().post("/user/authenticate")
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Authenticate User", User.class);

        return this;
    }

    public UserClient logoutUser() throws ApiClientException {
        Response response = given()
                .header(HEADER_SECURITY_EMAIL, getEntity().getEmail())
                .header(HEADER_SECURITY_TOKEN, getEntity().getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().post("/user/logout")
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Logout User", User.class);

        return this;
    }

    public UserClient updatePassword() throws ApiClientException {

        String uniqueId = UUID.randomUUID().toString();

        Map<String, String> passwords = new HashMap<>();
        passwords.put("currentPassword", String.format("password_%s", getEntity().getEmail().replace("test.user.", "").replace("@market-reaction.com", "")));
        passwords.put("newPassword", String.format("NewPassword_", uniqueId));
        passwords.put("newPasswordConfirm", String.format("NewPassword_", uniqueId));

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, getEntity().getEmail())
                .header(HEADER_SECURITY_TOKEN, getEntity().getToken())
                .body(passwords)
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().post("/user/updatePassword")
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to Update User Password", User.class);

        return this;
    }

    public UserClient makeAdmin() throws ApiClientException {

        User adminUser = getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, adminUser.getEmail())
                .header(HEADER_SECURITY_TOKEN, adminUser.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/user/%s/role/ROLE_ADMIN", getEntity().getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to assign admin role to user", User.class);

        return this;
    }

    public UserClient adminActivateUser() throws ApiClientException {
        User adminUser = getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, adminUser.getEmail())
                .header(HEADER_SECURITY_TOKEN, adminUser.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/user/%s/enable", getEntity().getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to activate user as admin", User.class);

        return this;
    }

    public UserClient adminDisableUser() throws ApiClientException {
        User adminUser = getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, adminUser.getEmail())
                .header(HEADER_SECURITY_TOKEN, adminUser.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/user/%s/disable", getEntity().getId()))
                .thenReturn();

        entity = validateResponseAndReturn(response, 200, "Failed to activate user as admin", User.class);

        return this;
    }

    public UserClient adminDeleteUser() throws ApiClientException {
        User adminUser = getAuthenticatedAdminUser();

        Response response = given()
                .header(HEADER_SECURITY_EMAIL, adminUser.getEmail())
                .header(HEADER_SECURITY_TOKEN, adminUser.getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get(String.format("/user/%s/delete", getEntity().getId()))
                .thenReturn();

        validateResponseAndReturn(response, 200, "Failed to activate user as admin", null);

        return this;
    }

    public Users getUsers() throws ApiClientException {
        Response response =  given()
                .header(HEADER_SECURITY_EMAIL, getEntity().getEmail())
                .header(HEADER_SECURITY_TOKEN, getEntity().getToken())
                .contentType(ContentType.JSON)
                .baseUri(BASE_API_URL)
                .when().get("/user/list")
                .thenReturn();

        if (response.getStatusCode() != 200) {
            throw new ApiClientException("Failed to get Users");
        }

        return response.as(Users.class);
    }

    public static User getAuthenticatedUser() throws ApiClientException {
        return new UserClient().createUser().activateUser().authenticateUser().getEntity();
    }

    public static User getAuthenticatedAdminUser() throws ApiClientException {
        return new UserClient().getAdminUser().authenticateUser().getEntity();
    }

}

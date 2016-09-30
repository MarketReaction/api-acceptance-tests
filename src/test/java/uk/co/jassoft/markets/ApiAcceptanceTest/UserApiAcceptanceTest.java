package uk.co.jassoft.markets.ApiAcceptanceTest;

import uk.co.jassoft.markets.client.UserClient;
import uk.co.jassoft.markets.client.exception.ApiClientException;
import uk.co.jassoft.markets.datamodel.user.Users;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class UserApiAcceptanceTest extends ApiAcceptanceTestBase {

    @Test
    public void testUserCanSuccessfullyRegister() throws ApiClientException {
        new UserClient().createUser();
    }

    @Test
    public void testUserCanActivateRegistration() throws ApiClientException {
        new UserClient().createUser().activateUser();
    }

    @Test
    public void testUserCanAuthenticate() throws ApiClientException {
        new UserClient().createUser().activateUser().authenticateUser();
    }

    @Test
    public void testUserCanLogout() throws ApiClientException {
        new UserClient().createUser().activateUser().authenticateUser().logoutUser();
    }

    @Test
    public void testUserCanUpdatePassword() throws ApiClientException {
        new UserClient().createUser().activateUser().authenticateUser().updatePassword();
    }

    @Test
    public void testAdminUserCanListUsers() throws ApiClientException {
        new UserClient().createUser();
        new UserClient().createUser();
        new UserClient().createUser();
        new UserClient().createUser();
        Users users = new UserClient().createUser().activateUser().makeAdmin().authenticateUser().getUsers();

        Assert.assertTrue(users.size() >= 5);
    }

    @Test
    public void testDisabledUserCanNotLogin() throws ApiClientException {
        UserClient userUnderTest = new UserClient().createUser().activateUser().authenticateUser();

        try {
            userUnderTest.adminDisableUser().authenticateUser();
        }
        catch (ApiClientException exception) {
            return;
        }

        Assert.fail();
    }

    @Test
    public void testDeletedUserCanNotLogin() throws ApiClientException {
        UserClient userUnderTest = new UserClient().createUser().activateUser().authenticateUser();

        try {
            userUnderTest.adminDeleteUser().authenticateUser();
        }
        catch (ApiClientException exception) {
            return;
        }

        Assert.fail();
    }
}

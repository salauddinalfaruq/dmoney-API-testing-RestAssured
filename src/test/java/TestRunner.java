import io.qameta.allure.Allure;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestRunner {

    User user;

    @Test(priority = 3 , description = "User provide valid creads for Login")
    public void doLogin() throws ConfigurationException, IOException {
        user = new User();
        user.doLoginWithValidCreds();
        Allure.description("User login successfully and a token is generated , status code is 200");
    }

    @Test(priority = 1 , description = "User provide incorrect email for Login")
    public void doLoginWrongEmail() throws IOException {
        user = new User();
        user.doLoginWrongEmail();
        Allure.description("'User not found' message given to user and status code is 404 not found");
    }

    @Test(priority = 2 , description = "User provide wrong password")
    public void doLoginWrongPassword() throws IOException {
        user = new User();
        user.doLoginWrongPassword();
        Allure.description("'Password incorrect' message given to user and status code is 401 Unauthorized");
    }

    @Test(priority = 6 , description = "User list should found by correct Authorization and Token")
    public void getUserData() throws IOException {
        user = new User();
        user.getUserList();
        Allure.description("User list is found and status code is 200");
    }

    @Test(priority = 4 , description = "User list should not found by wrong token")
    public void wrongToken() throws IOException {
        user = new User();
        user.getUserLIdtWithWrongToken();
        Allure.description("User list is not found and shown message 'Token expired!' , status code 403 Forbidden");
    }

    @Test(priority = 5 , description = "User list should not found for empty token")
    public void emptyAuth() throws IOException {
        user = new User();
        user.getUserLIdtWithEmptyAuth();
        Allure.description("User list not found and 'No token found' message shown , status code 401 Unauthorized");
    }

    @Test(priority = 8 , description = "Create new user")
    public void createNewUser() throws ConfigurationException, IOException {
        user = new User();
        user.createNewUser();
        Allure.description("User created successfully and status code is 201 created");
    }

    @Test(priority = 7, description = "User try to create existing user")
    public void existingUser() throws IOException {
        user = new User();
        user.alreadyExistUser();
        Allure.description("User already exist message shown and status code is 208 already reported");
    }

    @Test(priority = 9 , description = "Search user by id")
    public void searchUser() throws IOException {
        user = new User();
        user.searchUserById();
        Allure.description("User is found by id");
    }

    @Test(priority = 10 , description = "User data updated by id")
    public void userDataUpdate() throws IOException {
        user = new User();
        user.updateUser();
        Allure.description("User data updated and 'User updated successfully' message shown");
    }

    @Test(priority = 11 , description = "User update phone number")
    public void updateUserPhoneNumber() throws IOException {
        user = new User();
        user.updateUserPhoneNumber();
        Allure.description("Updated phone number successfully and 'User updated successfully' message shown");
    }

    @Test(priority = 12 , description = "Delete user by id")
    public void deleteUser() throws IOException {
        user = new User();
        user.deleteUser();
        Allure.description("User deleted successfully' message shown and status code is 200");
    }
}

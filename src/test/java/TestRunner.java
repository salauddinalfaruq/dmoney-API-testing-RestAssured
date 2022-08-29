import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;

public class TestRunner {

    User user;

    @Test
    public void doLogin() throws ConfigurationException, IOException {
        user = new User();
        user.callingLoginAPI();
    }

    @Test
    public void doLoginWrongEmail() throws IOException {
        user = new User();
        user.callingLoginWrongEmail();
    }

    @Test
    public void doLoginWrongPassword() throws IOException {
        user = new User();
        user.callingLoginWrongPassword();
    }

    @Test
    public void getUserData() throws IOException {
        user = new User();
        user.callingGetAPI();
    }

    @Test
    public void wrongToken() throws IOException {
        user = new User();
        user.callingGetAPI1();
    }

    @Test
    public void emptyAuth() throws IOException {
        user = new User();
        user.callingGetAPI2();
    }
}

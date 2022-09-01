import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.LoginModel;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;


public class User {
    Properties prop = new Properties();
    FileInputStream file;
    {
        try {
            file = new FileInputStream("./src/test/resources/config.properties");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void doLoginWithValidCreds() throws ConfigurationException, IOException {
        prop.load(file);
        LoginModel loginModel = new LoginModel("salman@grr.la", "1234");
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel).
                        when().post("/user/login").
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        String message = response.get("message");
        String token = response.get("token");
        System.out.println(message);
        System.out.println(token);
        Utils.setCollectionVariable("token" , token);
    }

    public void doLoginWrongEmail() throws IOException {
        prop.load(file);
        LoginModel loginModel = new LoginModel("salman@grr", "1234");
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel).
                        when().post("/user/login").
                        then()
                        .assertThat().statusCode(404).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString() , "User not found");
    }

    public void doLoginWrongPassword() throws IOException {
        prop.load(file);
        LoginModel loginModel = new LoginModel("salman@grr.la", "123768");
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel).
                        when().post("/user/login").
                        then()
                        .assertThat().statusCode(401).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString() , "Password incorrect");
    }

    public void getUserList() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , prop.getProperty("token")).
                        when().get("user/list").
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString(), "User list");
    }

    public void getUserLIdtWithWrongToken() throws IOException {
        prop.load(file);
        String token = "abc123";
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , token).
                        when().get("/user/list").
                        then()
                        .assertThat().statusCode(403).extract().response();

        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("error.message");
        System.out.println(message);
    }

    public void getUserLIdtWithEmptyAuth() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , "").
                        when().get("/user/list").
                        then()
                        .assertThat().statusCode(401).extract().response();

        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("error.message");
        System.out.println(message);
    }

    public void createNewUser() throws IOException, ConfigurationException {
        prop.load(file);
        Faker faker = new Faker();
        String name = faker.name().fullName();
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String phone_number = faker.phoneNumber().phoneNumber();
        String nid = "980" + (int)Math.random()*((9999999-1000000+1)+9999999);

        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body("{\n" +
                                " \"name\": \"" + name + "\",\n" +
                                " \"email\":\"" + email + "\",\n" +
                                " \"password\":\"" + password + "\",\n" +
                                " \"phone_number\":\"" +  phone_number +"\",\n" +
                                " \"nid\":\"" + nid + "\",\n" +
                                " \"role\":\"Customer\"\n" +
                                "}").
                        when().post("/user/create").
                        then()
                        .assertThat().statusCode(201).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString() , "User created successfully");
        String id = response.get("user.id").toString();
        System.out.println(id);
        Utils.setCollectionVariable("id" , id);
    }

    public void alreadyExistUser() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY" , "ROADTOSDET")
                        .body("{\n" +
                                " \"name\":\"Mr. Jamal 2\",\n" +
                                " \"email\":\"jamal2@test.com\",\n" +
                                " \"password\":\"12345678\",\n" +
                                " \"phone_number\":\"01504474770\",\n" +
                                " \"nid\":\"124654\",\n" +
                                " \"role\":\"Customer\"\n" +
                                "}").
                        when().post("/user/create").
                        then()
                        .assertThat().statusCode(208).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString() , "User already exists");
        System.out.println(response.get("message").toString());
    }

    public void searchUserById() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY" , "ROADTOSDET").
                        when().get("/user/search?id=" + prop.getProperty("id")).
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("user.id").toString() , prop.getProperty("id"));
        System.out.println(response.get("user.id").toString());
    }

    public void updateUser() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY" , "ROADTOSDET")
                        .body("{\n" +
                                " \"name\":\"Shahriar Sadi\",\n" +
                                " \"email\":\"shahriar.sadi@gmail.com\",\n" +
                                " \"password\":\"gt$35tru\",\n" +
                                " \"phone_number\":\"01763553077\",\n" +
                                " \"nid\":\"54646464\",\n" +
                                " \"role\":\"Customer\"\n" +
                                "}").
                        when().put("/user/update/" + prop.getProperty("id")).
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString() , "User updated successfully");
        System.out.println(response.get("message").toString());
    }

    public void updateUserPhoneNumber() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY" , "ROADTOSDET")
                        .body("{\n" +
                                "        \"phone_number\": \"01673122712\"\n" +
                                "    }").
                        when().patch("/user/update/" + prop.getProperty("id")).
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString() , "User updated successfully");
        System.out.println(response.get("message").toString());
    }

    public void deleteUser() throws IOException {
        prop.load(file);
        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY" , "ROADTOSDET").
                        when().delete("/user/delete/" + prop.getProperty("id")).
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath response = res.jsonPath();
        Assert.assertEquals(response.get("message").toString() , "User deleted successfully");
        System.out.println(response.get("message").toString());
    }
}

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

    public void callingLoginAPI() throws ConfigurationException, IOException {

        prop.load(file);
        LoginModel loginModel = new LoginModel("salman@grr.la", "1234");

        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel).
                        when()
                        .post("/user/login").
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("message");
        String token = jsonpath.get("token");
        System.out.println(message);
        System.out.println(token);
        Utils.setEnvVariables("token" , token);
    }

    public void callingLoginWrongEmail() throws IOException {

        prop.load(file);
        LoginModel loginModel = new LoginModel("salman@grr", "1234");

        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel).
                        when()
                        .post("/user/login").
                        then()
                        .assertThat().statusCode(404).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("message");
        System.out.println(message);
    }

    public void callingLoginWrongPassword() throws IOException {

        prop.load(file);
        LoginModel loginModel = new LoginModel("salman@grr.la", "123768");

        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel).
                        when()
                        .post("/user/login").
                        then()
                        .assertThat().statusCode(401).extract().response();

        JsonPath jsonpath = res.jsonPath();
        String message = jsonpath.get("message");
        System.out.println(message);
    }

    public void callingGetAPI() throws IOException {
        prop.load(file);

        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , prop.getProperty("token")).
                        when()
                        .get("user/list").
                        then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonpath = res.jsonPath();
        Assert.assertEquals(jsonpath.get("users[0].id").toString(), "33");
        System.out.println("User ID: " + jsonpath.get("users[0].id").toString());
    }

    public void callingGetAPI1() throws IOException {

        prop.load(file);
        String token = "abc123";

        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , token).
                        when()
                        .get("/user/list").
                        then()
                        .assertThat().statusCode(403).extract().response();

        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("error.message");
        System.out.println(message);
    }

    public void callingGetAPI2() throws IOException {

        prop.load(file);

        RestAssured.baseURI = prop.getProperty("baseUrl");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization" , "").
                        when()
                        .get("/user/list").
                        then()
                        .assertThat().statusCode(401).extract().response();

        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("error.message");
        System.out.println(message);
    }
}

package tests;


import User.UserCreate;
import User.UserLocation;
import User.UserResponse;
import Utils.Constants;
import com.github.javafaker.Faker;
import io.restassured.config.Config;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class UserTests  {

    SoftAssert softAssert;
    @BeforeMethod
    public void setUp(){
        softAssert=new SoftAssert();
    }

    @Test
    public void getAllUsersTest(){
            Map<String,String> headers=new HashMap<>();
            headers.put("app-id","6463fc5a882b5e44a859cc5f");
       Response response = given()
                .baseUri("https://dummyapi.io/data/")
                .basePath("v1/")
                .queryParams("page","1")
                .queryParams("limit","50")
                .headers(headers)
                .when().get(Constants.GET_ALL_USERS);
        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(response.jsonPath().get("data[0].firstName"),"Dylan");
    }
    @Test
    public void getUserByIDTest(){
        Map<String,String> headers=new HashMap<>();
        headers.put("app-id","6463fc5a882b5e44a859cc5f");
        Response response = given()
                .baseUri("https://dummyapi.io/data/")
                .basePath("v1/")
                .pathParam("id","60d0fe4f5311236168a10a06")
                .headers(headers)
                .when().get(Constants.GET_USER_BY_ID);

        Assert.assertEquals(response.getStatusCode(),200);
        Assert.assertEquals(response.jsonPath().get("firstName"),"Ane");
    }
    @Test
    public void deleteUserById(){

        String userID="60d0fe4f5311236168a10a06";
        Map<String,String> headers=new HashMap<>();
        headers.put("app-id","6463fc5a882b5e44a859cc5f");
        Response response = given()
                .baseUri("https://dummyapi.io/data/")
                .basePath("v1/")
                .pathParam("id",userID)
                .headers(headers)
                .when().delete(Constants.DELETE_USER);

        Assert.assertEquals(response.getStatusCode(),200);
        String actualUserID=response.jsonPath().get("id");
        Assert.assertEquals(actualUserID,userID,"Expected result is: " + userID + "but actual is: " + actualUserID);
    }
    @Test
    public void createUser(){
        String body="{\n" +
                " \"gender\": \"male\",\n" +
                " \"email\": \"perazder34a@gmail.com\",\n" +
                "\"firstName\": \"Pera1\",\n" +
                "\"lastName\": \"Markovic1234\",\n" +
                "\"location\": {\n" +
                "    \"street\": \"Vojvode Stepe\",\n" +
                "    \"city\": \"Loznica\",\n" +
                "    \"state\": \"Serbia\"\n" +
                "    }\n" +
                "}";
        given()
                .body(body)
                .log().all()
                .when().post(Constants.CREATE_USER)
                .then().log().all();
    }
    @Test
    public void createUserViaObject(){
        Faker faker=new Faker(new Locale("en-US"));
        UserLocation location=UserLocation.builder().
                city(faker.address().city())
                .street(faker.address().streetAddress())
                .state(faker.address().state())
                .build();

        UserCreate user=UserCreate.builder()
                .email(faker.internet().emailAddress())
                        .first_Name(faker.name().firstName())
                                .lastName(faker.name().lastName())
                                        .userLocation(location)
                                                .build();


       UserResponse userResponse= given()
                .body(user)
                .log().all()
                .when().post(Constants.CREATE_USER).getBody().as(UserResponse.class);

       softAssert.assertEquals(userResponse.getEmail(),user.getEmail());
       softAssert.assertEquals(userResponse.getFirstName(),user.getFirst_Name());
       softAssert.assertAll();

    }

}

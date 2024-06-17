package tests;
import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import  lib.ApiCoreRequests;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;

@Epic("Registration cases")
@Feature("Registration")
@Link("https://software-testing.ru/lms/mod/assign/view.php?id=308005")
public class UserRegisterTest extends BaseTestCase{
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String url = "https://playground.learnqa.ru/api/user/";

    @Description("Successfully user creation")
    @DisplayName("Positive registration test")
    @Test
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateUserPositive(){
        Map<String,String> userdata = DataGenerator.getRegistrationData();

        Response responseCreateAuth = apiCoreRequests.makePostRequest(this.url,userdata);
        Assertions.assertJsonHasField(responseCreateAuth,"id");
        Assertions.assertStatusCodeEquals(responseCreateAuth,200);
    }

    @Description("Unsuccessfully user creation: existing email")
    @DisplayName("Negative registration test: existing email")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";
        Map<String,String> userdata = new HashMap<>();
        userdata.put("email",email);
        userdata = DataGenerator.getRegistrationData(userdata);
        Response responceCreateAuth = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responceCreateAuth,"Users with email '"+ email +"' already exists");
        Assertions.assertStatusCodeEquals(responceCreateAuth,400);
    }

    @Description("Unsuccessfully user creation: invalid email")
    @DisplayName("Negative registration test: invalid email")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testNotCorrectEmail(){
        String email = "zloyasexample.com";
        Map<String,String> userdata = new HashMap<>();
        userdata.put("email",email);
        userdata = DataGenerator.getRegistrationData(userdata);

        Response responseNotCorrectEmail = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responseNotCorrectEmail,"Invalid email format");
        Assertions.assertStatusCodeEquals(responseNotCorrectEmail,400);

    }

    @Severity(SeverityLevel.MINOR)
    @Description("Unsuccessfully user creation: key missing")
    @DisplayName("Negative registration test: key missing")
    @ParameterizedTest
    @MethodSource("UserDataWithNoKey")
    public void testUserDataWithNoKey(Map<String,String> userData){

        Response responseUserDataWithNoKey = apiCoreRequests.makePostRequest(this.url, userData);

        Assertions.assertStatusCodeEquals(responseUserDataWithNoKey,400);
    }
    static Stream<Map<String,String>> UserDataWithNoKey(){
        Map<String,String> userdata1 = new HashMap<>();
        userdata1.put("email","");
        userdata1 = DataGenerator.getRegistrationData(userdata1);

        Map<String,String> userdata2 = new HashMap<>();
        userdata2.put("password","");
        userdata2 = DataGenerator.getRegistrationData(userdata2);

        Map<String,String> userdata3 = new HashMap<>();
        userdata3.put("username","");
        userdata3 = DataGenerator.getRegistrationData(userdata3);

        Map<String,String> userdata4 = new HashMap<>();
        userdata4.put("firstName","");
        userdata4 = DataGenerator.getRegistrationData(userdata4);

        Map<String,String> userdata5 = new HashMap<>();
        userdata5.put("lastName","");
        userdata5 = DataGenerator.getRegistrationData(userdata5);

        return Stream.of(userdata1,userdata2,userdata3,userdata4,userdata5);
    }

    @Description("Unsuccessfully user creation: short username")
    @DisplayName("Negative registration test: short username")
    @Test
    @Severity(SeverityLevel.MINOR)
    public void testShortName(){
        Map<String,String> userdata = new HashMap<>();
        userdata.put("username","l");
        userdata = DataGenerator.getRegistrationData(userdata);

        Response responseShortName = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responseShortName,"The value of 'username' field is too short");
        Assertions.assertStatusCodeEquals(responseShortName,400);
    }

    @Description("Unsuccessfully user creation: username longer than 250 symbols")
    @DisplayName("Negative registration test: long username")
    @Test
    @Severity(SeverityLevel.MINOR)
    public void testLongName(){
        Map<String,String> userdata = new HashMap<>();
        userdata.put("username",DataGenerator.getRandomString(251));
        userdata = DataGenerator.getRegistrationData(userdata);

        Response responseLongName = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responseLongName,"The value of 'username' field is too long");
        Assertions.assertStatusCodeEquals(responseLongName,400);
    }
    //after new branch
}

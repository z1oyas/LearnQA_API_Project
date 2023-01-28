package tests;
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
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;

@Epic("Registration cases")
@Feature("Registration")
public class UserRegisterTest extends BaseTestCase{
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    String url = "https://playground.learnqa.ru/api/user/";

    @Description("Successfully user creation")
    @DisplayName("Positive registration test")
    @Test
    public void testCreateUserPositive(){
        String email = DataGenerator.getRandomEmail();
        Map<String,String> userdata = new HashMap<>();
        userdata.put("email",email);
        userdata.put("password","123");
        userdata.put("username","zloyas");
        userdata.put("firstName","zloyas");
        userdata.put("lastName","zloyas");

        Response responseCreateAuth = apiCoreRequests.makePostRequest(this.url,userdata);
        Assertions.assertJsonHasField(responseCreateAuth,"id");
        Assertions.assertStatusCodeEquals(responseCreateAuth,200);
    }

    @Description("Unsuccessfully user creation: existing email")
    @DisplayName("Negative registration test: existing email")
    @Test
    public void testCreateUserWithExistingEmail(){
        String email = "vinkotov@example.com";
        Map<String,String> userdata = new HashMap<>();
        userdata.put("email",email);
        userdata.put("password","123");
        userdata.put("username","learnqa");
        userdata.put("firstName","learnqa");
        userdata.put("lastName","learnqa");

        Response responceCreateAuth = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responceCreateAuth,"Users with email '"+ email +"' already exists");
        Assertions.assertStatusCodeEquals(responceCreateAuth,400);
    }

    @Description("Unsuccessfully user creation: invalid email")
    @DisplayName("Negative registration test: invalid email")
    @Test
    public void testNotCorrectEmail(){
        String email = "zloyasexample.com";
        Map<String,String> userdata = new HashMap<>();
        userdata.put("email",email);
        userdata.put("password","0000");
        userdata.put("username","zloyas");
        userdata.put("firstName","zloyas");
        userdata.put("lastName","zloyas");

        Response responseNotCorrectEmail = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responseNotCorrectEmail,"Invalid email format");
        Assertions.assertStatusCodeEquals(responseNotCorrectEmail,400);

    }

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
        userdata1.put("password","123");
        userdata1.put("username","learnqa");
        userdata1.put("firstName","learnqa");
        userdata1.put("lastName","learnqa");

        Map<String,String> userdata2 = new HashMap<>();
        userdata2.put("email","zloyas1@example.com");
        userdata2.put("password","");
        userdata2.put("username","learnqa");
        userdata2.put("firstName","learnqa");
        userdata2.put("lastName","learnqa");

        Map<String,String> userdata3 = new HashMap<>();
        userdata3.put("email","zloyas3@example.com");
        userdata3.put("password","123");
        userdata3.put("username","");
        userdata3.put("firstName","learnqa");
        userdata3.put("lastName","learnqa");

        Map<String,String> userdata4 = new HashMap<>();
        userdata4.put("email","zloyas@example.com");
        userdata4.put("password","123");
        userdata4.put("username","learnqa");
        userdata4.put("firstName","");
        userdata4.put("lastName","learnqa");

        Map<String,String> userdata5 = new HashMap<>();
        userdata5.put("email","zloyas@example.com");
        userdata5.put("password","123");
        userdata5.put("username","learnqa");
        userdata5.put("firstName","learnqa");
        userdata5.put("lastName","");

        return Stream.of(userdata1,userdata2,userdata3,userdata4,userdata5);
    }

    @Description("Unsuccessfully user creation: short username")
    @DisplayName("Negative registration test: short username")
    @Test
    public void testShortName(){
        String email = "zloyas@example.com";
        Map<String,String> userdata = new HashMap<>();
        userdata.put("email",email);
        userdata.put("password","123");
        userdata.put("username","l");
        userdata.put("firstName","zloyas");
        userdata.put("lastName","zloyas");

        Response responseShortName = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responseShortName,"The value of 'username' field is too short");
        Assertions.assertStatusCodeEquals(responseShortName,400);
    }

    @Description("Unsuccessfully user creation: username longer than 250 symbols")
    @DisplayName("Negative registration test: long username")
    @Test
    public void testLongName(){
        String email = "zloyas@example.com";
        Map<String,String> userdata = new HashMap<>();
        String name = DataGenerator.getRandomString(251);
        userdata.put("email",email);
        userdata.put("password","123");
        userdata.put("username",name);
        userdata.put("firstName","zloyas");
        userdata.put("lastName","zloyas");

        Response responseLongName = apiCoreRequests.makePostRequest(this.url,userdata);

        Assertions.assertResponseTextEquals(responseLongName,"The value of 'username' field is too long");
        Assertions.assertStatusCodeEquals(responseLongName,400);
    }
}

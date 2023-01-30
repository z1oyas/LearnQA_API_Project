package tests;
import io.qameta.allure.*;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import java.util.HashMap;
import java.util.Map;

@Epic("Get user information cases")
@Feature("Get user information")
@Link("https://software-testing.ru/lms/mod/assign/view.php?id=308006")
public class UserGetTest extends BaseTestCase {
    String header ="";
    String cookie ="";
    ApiCoreRequests apiCoreRequests =new ApiCoreRequests();
    @BeforeEach
    public void  testUserAuth(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Get user data test without auth")){
        }
        else
        {
            Map<String, String> authData = new HashMap<>();
            authData.put("email", "zloyas12@example.com");
            authData.put("password", "123");

//            Response responseGetAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login",authData);
            Map<String, String> authUser = authUser(authData);

            this.header = authUser.get("x-csrf-token");
            this.cookie = authUser.get("auth_sid");
        }
    }

    @Description("Test get user data without registration")
    @DisplayName("Get user data without auth")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void  testUserdataNotAuth(){
    Response responseUserData = apiCoreRequests.makeUnautorisedGetRequest("https://playground.learnqa.ru/api/user/2");

    String[] unexpectedFields = {"firstName","lastName","email"};

    Assertions.assertJsonHasField(responseUserData,"username");
    Assertions.assertJsonHasNotFields(responseUserData,unexpectedFields);

}

    @Description("Test get user data with registration")
    @DisplayName("Get user data with auth")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserDetailsAuth(){
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/61444",this.header,this.cookie);

        String[] expectedFields = {"username","firstName","lastName","email"};

        Assertions.assertJsonHasFields(responseUserData,expectedFields);

    }

    @Description("Test get another user data with registration")
    @DisplayName("Get another user data with auth")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testGetAnotherUserDetailsAuth(){
        Response responseAnotherUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/61442",this.header,this.cookie);

        String[] unexpectedFields = {"firstName","lastName","email"};

        Assertions.assertJsonHasField(responseAnotherUserData,"username");
        Assertions.assertJsonHasNotFields(responseAnotherUserData,unexpectedFields);
    }
}

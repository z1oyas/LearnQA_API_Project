package tests;
import io.qameta.allure.*;
import io.restassured.specification.RequestSpecification;
import lib.Assertions;

import io.restassured.RestAssured;

import io.restassured.response.Response;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import lib.ApiCoreRequests;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@Epic("Authorisation cases")
@Feature("Authorisation")
public class AuthTest extends BaseTestCase {
    String cookie;
    String header;
    int userIdOnAuth;
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @BeforeEach
    public void loginUser(){
        Map<String,String> authData = new HashMap<>();
        authData.put("email","vinkotov@example.com");
        authData.put("password","1234");

        Response responceGetAuth = apiCoreRequests
                .makePostRequest("https://playground.learnqa.ru/api/user/login",authData);

        this.cookie =getCookie(responceGetAuth,"auth_sid");
        this.header = getHeader(responceGetAuth,"x-csrf-token");
        this.userIdOnAuth =this.getIntFromJson(responceGetAuth,"user_id");

    }

    @Test
    @Description("Successfully authorize user")
    @DisplayName("Test positive auth user")
    @Severity(SeverityLevel.BLOCKER)
    public void testAuthUser(){
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest("https://playground.learnqa.ru/api/user/auth",
                        this.header,
                        this.cookie);

        Assertions.assertJsonByName(responseCheckAuth,"user_id",this.userIdOnAuth);
    }

    @Description("Test check authorization status w/o sending cookie or token")
    @DisplayName("Test negative auth user")
    @Severity(SeverityLevel.MINOR)
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"})
    public void testNegativeAuthUser(String condition){
        RequestSpecification spec= RestAssured.given();
        spec.baseUri("https://playground.learnqa.ru/api/user/auth");

        if (condition.equals("cookie")){
            Response responseForCheck =apiCoreRequests.makeGetRequestWithCookie("https://playground.learnqa.ru/api/user/auth",
                    this.cookie);
            Assertions.assertJsonByName(responseForCheck,"user_id",0);
        }else if (condition.equals("headers")){
            Response responseForCheck =apiCoreRequests.makeGetRequestWithToken("https://playground.learnqa.ru/api/user/auth",
                    this.header);
            Assertions.assertJsonByName(responseForCheck,"user_id",0);
        }else {
            throw new IllegalArgumentException("condition value not known"+condition);
        }
    }
}

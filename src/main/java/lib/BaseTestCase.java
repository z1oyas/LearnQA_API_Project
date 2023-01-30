package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    protected String getHeader(Response Response, String name){
        Headers headers = Response.getHeaders();
        assertTrue(headers.hasHeaderWithName(name),"doesn't have such header:"+ name);
        return headers.getValue(name);
    }

    protected  String getCookie(Response Response,String name){
        Map<String,String> cookies = Response.getCookies();
        assertTrue(cookies.containsKey(name),"Responce doesn't have such cookie:"+ name);
        return cookies.get(name);
    }
    protected int getIntFromJson(Response Responce,String name){
        Responce.then().assertThat().body("$",hasKey(name));
        return Responce.jsonPath().getInt(name);
    }

    protected String getStringFromJson(Response Responce,String name){
        Responce.then().assertThat().body("$",hasKey(name));
        return Responce.jsonPath().getString(name);
    }

    protected  Map<String,String> getUserData(Response Response){
        String[] fields = {"email","username","firstName","lastName"};
        Map<String,String> userData = new HashMap<>();
        for (String field : fields) {
            userData.put(field,getStringFromJson(Response,field));
        }
        return userData;
    }
    protected Map<String,String> createUser(){
        Map<String,String> regData = DataGenerator.getRegistrationData();
        Response responseNewUser = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user",regData);

        Map <String,String> authData = new HashMap<>();
        authData.put("email",regData.get("email"));
        authData.put("password",regData.get("password"));

        return  authData;
    }
    protected Map<String,String> authUser(Map<String,String> authData){
        Response responseNewUserAuth = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login",
                authData);

        String header = this.getHeader(responseNewUserAuth, "x-csrf-token");
        String cookie = this.getCookie(responseNewUserAuth, "auth_sid");
        String user_id = this.getStringFromJson(responseNewUserAuth, "user_id");

        Map <String,String> dataForAuth = new HashMap<>();
        dataForAuth.put("x-csrf-token",header);
        dataForAuth.put("auth_sid",cookie);
        dataForAuth.put("user_id",user_id);

        return  dataForAuth;
    }
}


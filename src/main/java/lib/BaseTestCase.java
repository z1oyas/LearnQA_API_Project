package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;
public class BaseTestCase {
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
}


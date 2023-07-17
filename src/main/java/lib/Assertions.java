package lib;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class Assertions {
    //fix2
    public static  <T> void assertJsonByName(Response Response,String name,T expectedValue){
        Response.then().assertThat().body("$",hasKey(name));

        T value = Response.jsonPath().get(name);
        assertEquals(expectedValue, value,"json value is not equal expected");
    }
    public static void assertJsonHasField(Response Response, String name){
        Response.then().assertThat().body("$",hasKey(name));
    }
    public static void assertResponseTextEquals(Response Response,String expectedAnswer){
        assertEquals(expectedAnswer, Response.asString(),"unexpected response text ");
    }
    public static void assertStatusCodeEquals(Response Response,int expectedCode){
        assertEquals(expectedCode, Response.statusCode(),"unexpected status code");
    }

    public static void assertJsonHasNotField(Response Response, String unexpectedName){
        Response.then().assertThat().body("$",not(hasKey(unexpectedName)));
    }
    public static void assertJsonHasFields(Response Response, String[] expectedNames){
        for (String expectedName:expectedNames) {
            Assertions.assertJsonHasField(Response,expectedName);
        }
    }
    public static void assertJsonHasNotFields(Response Response, String[] unexpectedNames) {
        for (String unexpectedName : unexpectedNames) {
            Assertions.assertJsonHasNotField(Response, unexpectedName);
        }
    }
}

package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("User delete")
@Feature("User delete")
@Link("https://software-testing.ru/lms/mod/assign/view.php?id=308008")
public class UserDeleteTest extends BaseTestCase {
    ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @Description("Try to delete user with id 2")
    @DisplayName("Negative delete user test: id 2")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testTryToDeleteVipUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        //AUTH VIP USER
        Map<String, String> authVipUser = authUser(authData);

        //TRY TO DELETE THAT VIP USER
        Response responseDeleteVipUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user",
                authVipUser.get("x-csrf-token"),
                authVipUser.get("auth_sid"),
                authVipUser.get("user_id"));

        Assertions.assertStatusCodeEquals(responseDeleteVipUser,400);
        Assertions.assertResponseTextEquals(responseDeleteVipUser,"Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
    }
    @Description("Try to delete user")
    @DisplayName("Positive delete user test")
    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteUser(){
        //CREATE NEW USER
    Map<String,String> authData = createUser();

    //AUTH THAT NEW USER
        Map<String,String> AuthUser = authUser(authData);

    // DELETE THAT AUTH USER
        Response responseDeleteNewUser = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user",
                AuthUser.get("x-csrf-token"),
                AuthUser.get("auth_sid"),
                AuthUser.get("user_id"));

        Assertions.assertStatusCodeEquals(responseDeleteNewUser,200);

        //CHECK USER DELETE
        Response responseNewUserAuthAfterDelete = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login",
                authData);

        Assertions.assertResponseTextEquals(responseNewUserAuthAfterDelete,"Invalid username/password supplied");
    }
    @Description("Try to delete another user")
    @DisplayName("Negative delete another user test")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testTryToDeleteAnotherUser(){
        //CREATE NEW USER1
        Map<String,String> authData1 = createUser();
        //CREATE NEW USER2
        Map<String,String> authData2 = createUser();

        //AUTH THAT NEW USER1
        Map<String,String> AuthUser1 = authUser(authData1);
        //AUTH THAT NEW USER2
        Map<String,String> AuthUser2 = authUser(authData2);

        //DELETE USER2 BY USER1
        Response responseDeleteNewUser2 = apiCoreRequests.makeDeleteRequest("https://playground.learnqa.ru/api/user",
                AuthUser1.get("x-csrf-token"),
                AuthUser1.get("auth_sid"),
                AuthUser2.get("user_id"));

        //CHECK USER DELETE
        Response responseNewUserAuthAfterDelete = apiCoreRequests.makePostRequest("https://playground.learnqa.ru/api/user/login",
                authData2);

        Assertions.assertResponseTextEquals(responseNewUserAuthAfterDelete,"Invalid username/password supplied");

    }
}

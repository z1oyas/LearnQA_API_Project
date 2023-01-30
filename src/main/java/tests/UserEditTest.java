package tests;
import io.qameta.allure.*;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import  lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.util.HashMap;
import java.util.Map;


@Epic("Edit userData")
@Feature("userData edition")
@Link("https://software-testing.ru/lms/mod/assign/view.php?id=308007")
public class UserEditTest extends  BaseTestCase{

    ApiCoreRequests apiCoreRequests =new ApiCoreRequests();
    String header ="";
    String cookie ="";
    String userId ="";

    Map<String,String> UserData = new HashMap<>();

    @BeforeEach
    public void  testUserAuth(TestInfo testInfo) {
        if (testInfo.getDisplayName().equals("Edit user data test without auth")){
        }
        else
        {
            //GENERATE_USER
            Map<String,String> authData = createUser();
            //LOGIN
            Map<String,String> authUser = authUser(authData);
            this.header = authUser.get("x-csrf-token");
            this.cookie = authUser.get("auth_sid");
            this.userId = authUser.get("user_id");
            //GET USERDATA
            Response responseGetData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + this.userId,
                    this.header,
                    this.cookie);

            UserData = getUserData(responseGetData);
        }
    }
    @Description("Creating user and edit his userdata successfully")
    @DisplayName("Edit user data for user test")
    @Test
    @Severity(SeverityLevel.CRITICAL)
    public  void testEditJustCreatedUserTest(){
        //EDIT USERDATA
        String newName = "New Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newName);

        Response responseEditUser = apiCoreRequests.makPutRequest("https://playground.learnqa.ru/api/user/"+userId,
                        this.header,
                        this.cookie,
                        editData);

        //GET NEW USERDATA
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"+this.userId,
                        this.header,
                        this.cookie);

        Assertions.assertJsonByName(responseUserData,"firstName",newName);
    }
    @Description("Try to edit user data without authorisation")
    @DisplayName("Edit user data test without auth")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testEditNotAuthorised(){
        String newNameNoAuth = "New Name no Auth";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName",newNameNoAuth);

        Response responseEditUserNotAuthoriser = apiCoreRequests.makPutRequest("https://playground.learnqa.ru/api/user/"+this.userId,
                "",
                "",
                editData);

        Assertions.assertResponseTextEquals(responseEditUserNotAuthoriser,"Auth token not supplied");
        Assertions.assertStatusCodeEquals(responseEditUserNotAuthoriser,400);
    }
    @Description("Try to edit another user data")
    @DisplayName("Edit another user data test")
    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void testEditAnotherUserData(){
        String existingUserID = "61671";

        //GET SECOND USER DATA BEFORE EDIT
        Response responseSecondUser = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"+existingUserID,
                this.header,
                this.cookie);
        String SecondUserUsername = getStringFromJson(responseSecondUser,"username");

        //EDIT SECOND USER DATA
        String NewUsername = "New for Second User";
        Map<String,String> editData = new HashMap<>();
        editData.put("username",NewUsername);

        Response responseEditUser = apiCoreRequests.makPutRequest("https://playground.learnqa.ru/api/user/"+existingUserID,
                this.header,
                this.cookie,
                editData);

        //GET SECOND USER NEW DATA
        Response responseSecondUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/"+existingUserID,
                this.header,
                this.cookie);

        Assertions.assertJsonByName(responseSecondUserData,"username",SecondUserUsername);
    }

    @Description("Try to edit email to email without @")
    @DisplayName("Negative email edit test")
    @Test
    @Severity(SeverityLevel.NORMAL)
    public void testNegativeEditEmail() {

        //EDIT USERDATA
        Map<String, String> userdata = new HashMap<>();
        userdata.put("email", "zloyazexample.com");
        Response responseEditUser = apiCoreRequests.makPutRequest("https://playground.learnqa.ru/api/user/" + this.userId,
                this.header,
                this.cookie,
                userdata);

        Assertions.assertResponseTextEquals(responseEditUser,"Invalid email format");

        //GET NEW USERDATA
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + this.userId,
                this.header,
                this.cookie);

        Assertions.assertJsonByName(responseUserData, "email", this.UserData.get("email"));
    }

    @Description("Try to edit firsName to one symbol")
    @DisplayName("Negative firsName edit test")
    @Test
    @Severity(SeverityLevel.MINOR)
    public void testNegativeEditFirstName() {

        //EDIT USERDATA
        Map<String, String> userdata = new HashMap<>();
        userdata.put("firstName", "z");
        Response responseEditUser = apiCoreRequests.makPutRequest("https://playground.learnqa.ru/api/user/" + this.userId,
                this.header,
                this.cookie,
                userdata);

        Assertions.assertJsonByName(responseEditUser,"error","Too short value for field firstName");

        //GET NEW USERDATA
        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/" + this.userId,
                this.header,
                this.cookie);

        Assertions.assertJsonByName(responseUserData, "firstName", this.UserData.get("firstName"));
    }
}

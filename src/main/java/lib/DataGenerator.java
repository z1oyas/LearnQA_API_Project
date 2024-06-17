package lib;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
//
public class DataGenerator {

    //

    public static String getRandomString(int len) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < len; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            newString.append(chars.charAt(randomIndex));
        }

        return newString.toString();
    }

    public static String getRandomEmail() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder newString = new StringBuilder();
        for (int i = 0; i < 7; i++)
        {
            int randomIndex = random.nextInt(chars.length());
            newString.append(chars.charAt(randomIndex));
        }
        newString.append("@example.com");
        return newString.toString();
    }
    public static Map<String,String> getRegistrationData(){
        Map<String,String> data = new HashMap<>();
        data.put("email",DataGenerator.getRandomEmail());
        data.put("password","123");
        data.put("username","zloyas");
        data.put("firstName","zloyas");
        data.put("lastName","zloyas");
        return data;
    }
    public static Map<String,String> getRegistrationData(Map<String,String> nonDefaultData){
        Map<String,String> defaultData = DataGenerator.getRegistrationData();
        Map<String,String> userdata = new HashMap<>();
        String[] keys = {"email","password","username","firstName","lastName"};
        for (String key:keys){
            if(nonDefaultData.containsKey(key)){
                userdata.put(key, nonDefaultData.get(key));
            }else {
                userdata.put(key,defaultData.get(key));
            }
        }
        return userdata;
    }
    //new rebase
}

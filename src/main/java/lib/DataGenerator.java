package lib;

import java.security.SecureRandom;

public class DataGenerator {
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
}

package com.example.projetseg2505;

public class RegistrationValidator {

    public boolean isUsernameValid(String username) {
        // Check if the username is not null and has a length between 5 and 20 characters
        return username != null && username.length() >= 5 && username.length() <= 20;
    }

    public boolean isPasswordValid(String password) {
        // Check if the password is not null and has a length between 8 and 20 characters
        return password != null && password.length() >= 8 && password.length() <= 20;
    }
}


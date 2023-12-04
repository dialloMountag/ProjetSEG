package com.example.projetseg2505;


import org.junit.Test;

import static org.junit.Assert.*;



public class RegisterTest {
    @Test
    public void testValidUsername() {
        RegistrationValidator validator = new RegistrationValidator();
        assertTrue(validator.isUsernameValid("validUsername"));
    }

    @Test
    public void testInvalidShortUsername() {
        RegistrationValidator validator = new RegistrationValidator();
        assertFalse(validator.isUsernameValid("shor"));
    }

    @Test
    public void testInvalidLongUsername() {
        RegistrationValidator validator = new RegistrationValidator();
        assertFalse(validator.isUsernameValid("thisUsernameIsTooLong"));
    }

    @Test
    public void testValidPassword() {
        RegistrationValidator validator = new RegistrationValidator();
        assertTrue(validator.isPasswordValid("validPassword"));
    }

    @Test
    public void testInvalidShortPassword() {
        RegistrationValidator validator = new RegistrationValidator();
        assertFalse(validator.isPasswordValid("short"));
    }

    @Test
    public void testInvalidLongPassword() {
        RegistrationValidator validator = new RegistrationValidator();
        assertFalse(validator.isPasswordValid("thisPasswordIsTooLong"));
    }




}


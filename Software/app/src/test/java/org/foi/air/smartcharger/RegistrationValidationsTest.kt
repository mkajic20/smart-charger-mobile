package org.foi.air.smartcharger

import org.foi.air.smartcharger.validations.RegistrationValidations
import org.junit.Test
import org.junit.Assert.*

class RegistrationValidationsTest {
    private val registrationValidations = RegistrationValidations()

    @Test
    fun firstNameValidation_ValidInput_True() {
        assertTrue(registrationValidations.firstNameValidation("John"))
    }

    @Test
    fun firstNameValidation_EmptyInput_False() {
        assertFalse(registrationValidations.firstNameValidation(""))
    }

    @Test
    fun lastNameValidation_ValidInput_True() {
        assertTrue(registrationValidations.lastNameValidation("Doe"))
    }

    @Test
    fun lastNameValidation_EmptyInput_False() {
        assertFalse(registrationValidations.lastNameValidation(""))
    }

    @Test
    fun emailValidation_ValidEmail_True() {
        assertTrue(registrationValidations.emailValidation("john.doe@example.com"))
    }

    @Test
    fun emailValidation_InvalidEmail_False() {
        assertFalse(registrationValidations.emailValidation("invalid.email@"))
    }

    @Test
    fun passwordValidation_ValidPassword_True() {
        assertTrue(registrationValidations.passwordValidation("strongPassword"))
    }

    @Test
    fun passwordValidation_ShortPassword_False() {
        assertFalse(registrationValidations.passwordValidation("weak"))
    }

    @Test
    fun confirmPasswordValidation_MatchingPasswords_True() {
        assertTrue(registrationValidations.confirmPasswordValidation("password123", "password123"))
    }

    @Test
    fun confirmPasswordValidation_NonMatchingPasswords_False() {
        assertFalse(registrationValidations.confirmPasswordValidation("password123", "differentPassword"))
    }
}
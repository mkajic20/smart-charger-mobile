package org.foi.air.smartcharger.validations

class RegistrationValidations {

    private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", RegexOption.IGNORE_CASE)

    fun firstNameValidation(firstName: String): Boolean{
        return firstName.isNotEmpty()
    }

    fun lastNameValidation(lastName: String): Boolean{
        return lastName.isNotEmpty()
    }

    fun emailValidation(email: String): Boolean{
        return emailPattern.matches(email.trim())
    }

    fun passwordValidation(password: String): Boolean{
        return password.trim().length>=6
    }

    fun confirmPasswordValidation(password: String, confirmPassword: String): Boolean{
        return confirmPassword == password
    }
}
package org.foi.air.smartcharger.validations

class RegistrationValidations {

    private val emailPattern = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", RegexOption.IGNORE_CASE)

    fun firstNameValidation(firstName: String): Boolean{
        if(firstName.isNotEmpty()) return true
        return false
    }

    fun lastNameValidation(lastName: String): Boolean{
        if(lastName.isNotEmpty()) return true
        return false
    }

    fun emailValidation(email: String): Boolean{
        if(emailPattern.matches(email.trim())) return true
        return false
    }

    fun passwordValidation(password: String): Boolean{
        if(password.trim().length>6) return true
        return false
    }

    fun confirmPasswordValidation(password: String, confirmPassword: String): Boolean{
        if(confirmPassword == password) return true
        return false
    }
}
package com.example.danusin.utils

import android.util.Patterns

object ValidationUtils {
    fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }

    fun isFieldEmpty(field: String): Boolean {
        return field.trim().isEmpty()
    }
}

package com.example.domain.model


data class Email(val value: String, val password: String) {

    fun isValidEmail(): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return value.matches(emailRegex)
    }

    companion object {
        val EMPTY = Email("", "")

        fun create(value: String): PhoneNumber {
            return PhoneNumber(value)
        }
    }

    override fun toString(): String {
        return value
    }
}
package com.example.domain.interactor

import com.example.domain.model.PhoneNumber
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.PhoneAuthOptions
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun login(phoneNumber: PhoneNumber, options: PhoneAuthOptions) {
        authRepository.login(phoneNumber, options)
    }
}
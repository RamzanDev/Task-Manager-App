package com.example.domain.interactor

import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthInteractor @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser?> {
        return authRepository.loginWithEmail(email, password)
    }

    suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        return authRepository.registerWithEmailAndPassword(email, password)
    }
}
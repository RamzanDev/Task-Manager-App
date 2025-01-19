package com.example.data.repository

import com.example.domain.model.PhoneNumber
import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {


    override suspend fun login(
        phoneNumber: PhoneNumber,
        options: PhoneAuthOptions
    ) {
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override suspend fun register(phoneNumber: PhoneNumber) {
        TODO("Not yet implemented")
    }
}
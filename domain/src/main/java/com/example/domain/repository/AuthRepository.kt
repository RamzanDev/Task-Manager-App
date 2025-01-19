package com.example.domain.repository

import com.example.domain.model.PhoneNumber
import com.google.firebase.auth.PhoneAuthOptions

interface AuthRepository {

    suspend fun login(phoneNumber: PhoneNumber, options: PhoneAuthOptions)
    suspend fun register(phoneNumber: PhoneNumber)
}
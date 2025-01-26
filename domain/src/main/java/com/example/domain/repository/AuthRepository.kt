package com.example.domain.repository

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun registerWithEmailAndPassword(email: String, password: String): Result<FirebaseUser?>
}
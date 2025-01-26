package com.example.data.repository

import com.example.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : AuthRepository {

    override suspend fun registerWithEmailAndPassword(
        email: String,
        password: String
    ): Result<FirebaseUser?> {
        return suspendCoroutine { continuation ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Успешная регистрация, возвращаем текущего пользователя
                        val user = firebaseAuth.currentUser
                        continuation.resume(Result.success(user))
                    } else {
                        // Ошибка регистрации, возвращаем причину ошибки
                        val exception = task.exception
                        continuation.resume(Result.failure(exception ?: Exception("Unknown error")))
                    }
                }
        }
    }

    override suspend fun loginWithEmail(email: String, password: String): Result<FirebaseUser?> {
        return suspendCoroutine { continuation ->
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Успешный вход, возвращаем текущего пользователя
                        val user = firebaseAuth.currentUser
                        continuation.resume(Result.success(user))
                    } else {
                        // Ошибка входа, возвращаем причину ошибки
                        val exception = task.exception
                        continuation.resume(Result.failure(exception ?: Exception("Unknown error")))
                    }
                }
        }
    }
}
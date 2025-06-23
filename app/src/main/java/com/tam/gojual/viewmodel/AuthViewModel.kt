package com.tam.gojual.viewmodel // Sesuaikan dengan package Anda

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

// Class pembantu untuk mengelola state: Loading, Success, Error (ini tetap sama)
sealed class AuthResult {
    data class Success(val user: FirebaseUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
    object Loading : AuthResult()
}

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> = _authResult

    fun loginUser(email: String, password: String) {
        _authResult.value = AuthResult.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                // Jika login berhasil, kirim data pengguna
                _authResult.value = AuthResult.Success(result.user!!)
            }
            .addOnFailureListener { e ->
                // Jika login gagal, kirim pesan error
                _authResult.value = AuthResult.Error(e.message ?: "Login failed")
            }
    }

    fun registerUser(name: String, email: String, password: String) {
        _authResult.value = AuthResult.Loading
        // Langkah 1: Buat pengguna dengan email dan password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val user = authResult.user!!
                // Langkah 2: Jika pengguna berhasil dibuat, update namanya
                val profileUpdates = userProfileChangeRequest {
                    displayName = name
                }
                user.updateProfile(profileUpdates)
                    .addOnSuccessListener {
                        // Jika update nama berhasil, kirim data pengguna
                        _authResult.value = AuthResult.Success(user)
                    }
                    .addOnFailureListener { e ->
                        // Jika update nama gagal, kirim pesan error
                        _authResult.value = AuthResult.Error(e.message ?: "Failed to update profile")
                    }
            }
            .addOnFailureListener { e ->
                // Jika pembuatan pengguna gagal, kirim pesan error
                _authResult.value = AuthResult.Error(e.message ?: "Registration failed")
            }
    }
}
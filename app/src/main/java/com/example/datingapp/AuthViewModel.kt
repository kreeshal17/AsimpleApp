package com.example.datingapp

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthEvent>()
    var authState: LiveData<AuthEvent> = _authState

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val storage: FirebaseStorage = FirebaseStorage.getInstance()

    init {
        checkStatus()
    }

    fun checkStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthEvent.Unauthenticated
        } else {
            _authState.value = AuthEvent.Authenticated
        }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthEvent.Loading
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthEvent.Error("Email and password cannot be empty")
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthEvent.Authenticated
                } else {
                    _authState.value = AuthEvent.Error(task.exception?.message ?: "Login failed")
                }
            }
    }

    fun signUp(email: String, password: String) {
        _authState.value = AuthEvent.Loading
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthEvent.Error("Email and password cannot be empty")
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthEvent.Authenticated
                } else {
                    _authState.value = AuthEvent.Error(task.exception?.message ?: "Sign up failed")
                }
            }
    }

    fun signOut() {
        auth.signOut()  // Sign out from Firebase Authentication
        _authState.value = AuthEvent.Unauthenticated
    }

    fun forgotPassword(email: String) {
        _authState.value = AuthEvent.Loading
        if (email.isEmpty()) {
            _authState.value = AuthEvent.Error("Email cannot be empty")
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthEvent.Success("Password reset email sent to $email")
                } else {
                    _authState.value = AuthEvent.Error(task.exception?.message ?: "Failed to send reset email")
                }
            }
    }


    fun uploadImageAndSaveUserData(
        firstName: String,
        lastName: String,
        age: Int,
        gender: String,
        imageUri: Uri?,
        context: Context  // Make sure you have a Context available
    ) {
        val userId = auth.currentUser?.uid ?: return  // Get user ID or return if null

        if (imageUri == null) {
            _authState.value = AuthEvent.Error("Profile picture is required")
            return
        }

        _authState.value = AuthEvent.Loading  // Set loading state

        val storageRef = storage.reference.child("profile_images/profile_$userId")

        storageRef.putFile(imageUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    saveUserData(firstName, lastName, age, gender, uri.toString())
                }
            }
            .addOnFailureListener { e ->
                _authState.value = AuthEvent.Error(e.message ?: "Image upload failed")
            }
    }

    private fun saveUserData(
        firstName: String,
        lastName: String,
        age: Int,
        gender: String,
        imageUri: String
    ) {
        val userId = auth.currentUser?.uid ?: return

        val userReference = db.collection("users").document(userId) // Correct collection name: "users"

        val userData = hashMapOf(
            "firstName" to firstName,
            "lastName" to lastName,
            "age" to age,
            "gender" to gender,
            "imageUri" to imageUri
        )

        _authState.value = AuthEvent.Loading // Set loading state

        userReference.set(userData)
            .addOnSuccessListener {
                _authState.value = AuthEvent.Success("Profile updated successfully")
            }
            .addOnFailureListener { e ->
                _authState.value = AuthEvent.Error(e.message ?: "Failed to save user data")
            }
    }
}



sealed class AuthEvent {
    object Authenticated : AuthEvent()
    object Unauthenticated : AuthEvent()
    data class Error(val message: String) : AuthEvent()
    data class Success(val message: String) : AuthEvent()
    object Loading : AuthEvent()
}
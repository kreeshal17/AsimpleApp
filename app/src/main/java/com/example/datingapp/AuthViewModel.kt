package com.example.datingapp

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel:ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthEvent>()
    var authstate: LiveData<AuthEvent> = _authState


    init {
        checkStatus()
    }

    fun checkStatus() {
        if (auth.currentUser == null) {
            _authState.value = AuthEvent.unAuthenticated
        } else {
            _authState.value = AuthEvent.unAuthenticated
        }
    }


    fun login(email: String, password: String) {
        _authState.value = AuthEvent.Loading
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthEvent.Error("it cannot be empty")
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthEvent.Aunthenticated

            } else {
                _authState.value =
                    AuthEvent.Error(task.exception?.message ?: "Something went wrong")
            }
        }

    }

    fun signUp(email: String, password: String) {
        _authState.value = AuthEvent.Loading
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthEvent.Error("this cannot be empty")

        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthEvent.Aunthenticated
            } else {

                _authState.value =
                    AuthEvent.Error(task.exception?.message ?: "Something went wrong")

            }
        }
    }


    fun signout() {
        _authState.value = AuthEvent.unAuthenticated
    }
    fun forgotPassword(email: String) {
        _authState.value = AuthEvent.Loading
        if (email.isEmpty()) {
            _authState.value = AuthEvent.Error("Email cannot be empty")
            return
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _authState.value = AuthEvent.Success("Reset link sent to $email")
            } else {
                _authState.value = AuthEvent.Error(task.exception?.message ?: "Something went wrong")
            }
        }
    }
}



sealed class AuthEvent{

    object Aunthenticated:AuthEvent()
    object unAuthenticated:AuthEvent()
    data class Error(val message:String):AuthEvent()
    data class Success(val message:String):AuthEvent()
    object Loading:AuthEvent()
}
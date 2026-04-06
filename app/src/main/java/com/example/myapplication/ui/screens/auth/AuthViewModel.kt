package com.example.myapplication.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.UserSessionManager
import com.example.myapplication.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthUiState(
    val email: String = "",
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val authSuccess: Boolean = false,
    val emailAccepted: Boolean = false,
    val userLoggedIn: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun clearState() {
        _uiState.value = AuthUiState()
    }

    fun validateSignInEmail(email: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                errorMessage = null,
                authSuccess = false,
                emailAccepted = false
            )

            if (email.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "Email is required"
                )
                return@launch
            }

            if (!isValidEmail(email)) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "Please enter a valid email"
                )
                return@launch
            }

            val exists = authRepository.checkIfUserExists(email)
            if (!exists) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "Email not registered"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    email = email,
                    emailAccepted = true
                )
            }
        }
    }

    fun registerUser(email: String, pin: String, confirmPin: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                errorMessage = null,
                authSuccess = false
            )

            if (email.isBlank() || pin.isBlank() || confirmPin.isBlank()) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "All fields are required"
                )
                return@launch
            }

            if (!isValidEmail(email)) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "Please enter a valid email"
                )
                return@launch
            }

            if (pin.length != 4 || !pin.all { it.isDigit() }) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "PIN must be 4 digits"
                )
                return@launch
            }

            if (pin != confirmPin) {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "PINs do not match"
                )
                return@launch
            }

            val result = authRepository.registerUser(email, pin)
            result
                .onSuccess {
                    userSessionManager.saveLoggedInUser(email)
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        authSuccess = true,
                        userLoggedIn = true,
                        email = email
                    )
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        errorMessage = error.message ?: "Registration failed"
                    )
                }
        }
    }

    fun verifyPin(email: String, pin: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true,
                errorMessage = null,
                authSuccess = false
            )

            val isCorrect = authRepository.verifyPin(email, pin)

            if (isCorrect) {
                userSessionManager.saveLoggedInUser(email)
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    authSuccess = true,
                    userLoggedIn = true,
                    email = email
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    errorMessage = "Incorrect PIN"
                )
            }
        }
    }

    fun logout() {
        userSessionManager.logout()
        _uiState.value = AuthUiState()
    }

    fun isUserLoggedIn(): Boolean {
        return userSessionManager.isLoggedIn()
    }

    fun getLoggedInUserEmail(): String? {
        return userSessionManager.getLoggedInUserEmail()
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

class AuthViewModelFactory(
    private val authRepository: AuthRepository,
    private val userSessionManager: UserSessionManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(authRepository, userSessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
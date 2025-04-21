package com.example.danusin.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.danusin.models.User
import com.example.danusin.models.UserRole
import com.example.danusin.utils.SharedPreferencesManager
import com.example.danusin.utils.ValidationUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: UserRole = UserRole.BUYER,
    val rememberMe: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val generalError: String? = null,
    val isPasswordVisible: Boolean = false
)

class AuthViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    fun initialize(context: Context) {
        sharedPreferencesManager = SharedPreferencesManager(context)

        // Check if user is already logged in
        val isLoggedIn = sharedPreferencesManager.isLoggedIn()
        if (isLoggedIn && sharedPreferencesManager.getRememberMePreference()) {
            _uiState.update { it.copy(
                isLoggedIn = true,
                email = sharedPreferencesManager.getEmail() ?: "",
                password = sharedPreferencesManager.getPassword() ?: "",
                rememberMe = true,
                role = if (sharedPreferencesManager.getUserRole() == UserRole.SELLER.name)
                    UserRole.SELLER else UserRole.BUYER
            ) }
        }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(
            email = email,
            emailError = null
        ) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(
            password = password,
            passwordError = null
        ) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(
            confirmPassword = confirmPassword,
            confirmPasswordError = null
        ) }
    }

    fun updateRole(role: UserRole) {
        _uiState.update { it.copy(role = role) }
    }

    fun updateRememberMe(rememberMe: Boolean) {
        _uiState.update { it.copy(rememberMe = rememberMe) }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    fun login() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // Reset errors
            _uiState.update { it.copy(
                emailError = null,
                passwordError = null,
                generalError = null,
                isLoading = true
            ) }

            // Validate fields
            var isValid = true

            if (ValidationUtils.isFieldEmpty(currentState.email)) {
                _uiState.update { it.copy(emailError = "Email tidak boleh kosong") }
                isValid = false
            } else if (!ValidationUtils.isEmailValid(currentState.email)) {
                _uiState.update { it.copy(emailError = "Format email tidak valid") }
                isValid = false
            }

            if (ValidationUtils.isFieldEmpty(currentState.password)) {
                _uiState.update { it.copy(passwordError = "Password tidak boleh kosong") }
                isValid = false
            } else if (!ValidationUtils.isPasswordValid(currentState.password)) {
                _uiState.update { it.copy(passwordError = "Password minimal 6 karakter") }
                isValid = false
            }

            if (isValid) {
                // Simulate login process
                // In a real app, you would call an API here

                // Save preferences if remember me is checked
                if (currentState.rememberMe) {
                    sharedPreferencesManager.saveRememberMePreference(true)
                    sharedPreferencesManager.saveLoginCredentials(
                        currentState.email,
                        currentState.password
                    )
                    sharedPreferencesManager.saveUserRole(currentState.role.name)
                } else {
                    sharedPreferencesManager.saveRememberMePreference(false)
                    sharedPreferencesManager.clearLoginData()
                }

                sharedPreferencesManager.setLoggedIn(true)

                _uiState.update { it.copy(
                    isLoading = false,
                    isLoggedIn = true
                ) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // Reset errors
            _uiState.update { it.copy(
                emailError = null,
                passwordError = null,
                confirmPasswordError = null,
                generalError = null,
                isLoading = true
            ) }

            // Validate fields
            var isValid = true

            if (ValidationUtils.isFieldEmpty(currentState.email)) {
                _uiState.update { it.copy(emailError = "Email tidak boleh kosong") }
                isValid = false
            } else if (!ValidationUtils.isEmailValid(currentState.email)) {
                _uiState.update { it.copy(emailError = "Format email tidak valid") }
                isValid = false
            }

            if (ValidationUtils.isFieldEmpty(currentState.password)) {
                _uiState.update { it.copy(passwordError = "Password tidak boleh kosong") }
                isValid = false
            } else if (!ValidationUtils.isPasswordValid(currentState.password)) {
                _uiState.update { it.copy(passwordError = "Password minimal 6 karakter") }
                isValid = false
            }

            if (ValidationUtils.isFieldEmpty(currentState.confirmPassword)) {
                _uiState.update { it.copy(confirmPasswordError = "Konfirmasi password tidak boleh kosong") }
                isValid = false
            } else if (currentState.password != currentState.confirmPassword) {
                _uiState.update { it.copy(confirmPasswordError = "Password tidak cocok") }
                isValid = false
            }

            if (isValid) {
                // Simulate registration process
                // In a real app, you would call an API here

                // Create user
                val user = User(
                    email = currentState.email,
                    password = currentState.password,
                    role = currentState.role
                )

                // Save user role
                sharedPreferencesManager.saveUserRole(user.role.name)

                // Auto login after registration
                sharedPreferencesManager.setLoggedIn(true)

                _uiState.update { it.copy(
                    isLoading = false,
                    isLoggedIn = true
                ) }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun logout() {
        sharedPreferencesManager.setLoggedIn(false)
        if (!sharedPreferencesManager.getRememberMePreference()) {
            sharedPreferencesManager.clearLoginData()
        }

        _uiState.update { it.copy(
            isLoggedIn = false,
            password = ""
        ) }
    }
}

package com.kl3jvi.animity.ui.activities.login

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.domain.use_cases.GetAccessTokenUseCase
import com.kl3jvi.animity.utils.State
import com.kl3jvi.animity.utils.mapToState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val userRepository: UserRepositoryImpl
) : ViewModel() {

    fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String
    ): Flow<State<AuthResponse>> {
        return getAccessTokenUseCase(
            grantType,
            clientId,
            clientSecret,
            redirectUri,
            authorizationToken
        ).mapToState()
    }

    fun saveToken(token: String) {
        userRepository.setBearerToken(token)
    }

    fun getToken(): String? {
        return userRepository.bearerToken
    }

    fun saveGuestToken(token: String) {
        userRepository.setGuestToken(token = token)
    }

    fun getGuestToken(): String? {
        return userRepository.guestToken
    }
}
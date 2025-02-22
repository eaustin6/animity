package com.kl3jvi.animity.data.network.anilist_service

import com.apollographql.apollo3.annotations.ApolloExperimental
import com.kl3jvi.animity.data.model.auth_models.AniListAuth
import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.utils.Constants.Companion.AUTH_URL
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AniListService {
    @Headers(
        "Content-Type: application/json",
        "Accept: application/json"
    )
    @POST(AUTH_URL)
    suspend fun getAccessToken(@Body aniListAuth: AniListAuth): Response<AuthResponse>


}
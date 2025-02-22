package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MarkAnimeAsFavoriteUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(animeId: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>> {
        return userRepositoryImpl.markAnimeAsFavorite(animeId = animeId).flowOn(ioDispatcher)
    }
}
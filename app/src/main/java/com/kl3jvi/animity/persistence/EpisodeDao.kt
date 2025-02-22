package com.kl3jvi.animity.persistence

import androidx.room.*
import com.kl3jvi.animity.data.model.ui_models.Content

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEpisode(content: Content)

    @Update
    suspend fun updateEpisode(content: Content)

    @Query("SELECT * FROM Content WHERE episodeUrl =:episodeUrl")
    suspend fun getEpisodeContent(episodeUrl: String): Content

    @Query("SELECT EXISTS(SELECT * FROM Content WHERE episodeUrl = :episodeUrl)")
    suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean

}
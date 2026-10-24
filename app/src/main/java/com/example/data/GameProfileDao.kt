package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.model.GameProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface GameProfileDao {
    @Query("SELECT * FROM game_profiles ORDER BY lastLaunchedTimestamp DESC, title ASC")
    fun getAllGames(): Flow<List<GameProfile>>

    @Query("SELECT * FROM game_profiles WHERE packageName = :packageName LIMIT 1")
    suspend fun getGameByPackage(packageName: String): GameProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(game: GameProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(games: List<GameProfile>)

    @Update
    suspend fun update(game: GameProfile)

    @Delete
    suspend fun delete(game: GameProfile)

    @Query("UPDATE game_profiles SET launchCount = launchCount + 1, lastLaunchedTimestamp = :timestamp WHERE packageName = :packageName")
    suspend fun incrementLaunch(packageName: String, timestamp: Long)

    @Query("SELECT COUNT(*) FROM game_profiles")
    suspend fun getCount(): Int
}

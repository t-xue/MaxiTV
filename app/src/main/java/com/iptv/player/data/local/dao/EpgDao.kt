package com.iptv.player.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iptv.player.data.local.entity.EpgProgramEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EpgDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(program: EpgProgramEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(programs: List<EpgProgramEntity>)

    @Query("SELECT * FROM epg_programs WHERE channelId = :channelId AND startTime <= :now AND endTime > :now LIMIT 1")
    suspend fun getCurrentProgram(channelId: String, now: Long = System.currentTimeMillis()): EpgProgramEntity?

    @Query("SELECT * FROM epg_programs WHERE channelId = :channelId AND startTime > :now ORDER BY startTime LIMIT 1")
    suspend fun getNextProgram(channelId: String, now: Long = System.currentTimeMillis()): EpgProgramEntity?

    @Query("SELECT * FROM epg_programs WHERE channelId = :channelId AND startTime >= :startTime AND endTime <= :endTime ORDER BY startTime")
    fun getProgramsByTimeRange(channelId: String, startTime: Long, endTime: Long): Flow<List<EpgProgramEntity>>

    @Query("SELECT * FROM epg_programs WHERE startTime >= :dayStart AND startTime < :dayEnd ORDER BY startTime")
    fun getProgramsByDay(dayStart: Long, dayEnd: Long): Flow<List<EpgProgramEntity>>

    @Query("SELECT * FROM epg_programs WHERE channelId = :channelId ORDER BY startTime")
    fun getAllProgramsForChannel(channelId: String): Flow<List<EpgProgramEntity>>

    @Query("DELETE FROM epg_programs WHERE endTime < :before")
    suspend fun deleteOldPrograms(before: Long)

    @Query("DELETE FROM epg_programs")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM epg_programs")
    suspend fun getProgramCount(): Int
}

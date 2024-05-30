package com.doorxii.ludus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.doorxii.ludus.data.models.ludus.Ludus
import kotlinx.coroutines.flow.Flow

@Dao
interface LudusDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLudus(ludus: Ludus)

    @Query(value = "SELECT * FROM ludus")
    fun getAllLudus(): Flow<List<Ludus>>

    @Query(value = "SELECT * FROM ludus WHERE ludusId = :ludusId")
    fun getLudus(ludusId: Int): Flow<Ludus>

    @Query(value = "SELECT * FROM ludus WHERE name = :name")
    fun getLudusByName(name: String): Flow<Ludus>

    @Query(value = "SELECT * FROM ludus WHERE ludusId = :ludusId")
    fun getLudusById(ludusId: Int): Flow<Ludus>

    @Query("SELECT * FROM ludus WHERE playerLudus = 1 LIMIT 1")
    fun getPlayerLudus(): Flow<Ludus>

    @Update
    suspend fun updateLudusById(ludus: Ludus)

    @Query(value = "DELETE FROM ludus WHERE ludusId = :ludusId")
    suspend fun deleteLudusById(ludusId: Int)


}
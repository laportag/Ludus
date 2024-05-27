package com.doorxii.ludus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.doorxii.ludus.data.models.animal.Gladiator
import kotlinx.coroutines.flow.Flow

@Dao
interface GladiatorDao {

    @Query("SELECT * FROM Gladiator")
    fun getAll(): Flow<List<Gladiator>>

    @Query("SELECT * FROM Gladiator WHERE id = :id")
    fun getById(id: Int): Flow<Gladiator>

    @Query("SELECT * FROM Gladiator WHERE name = :name")
    fun getByName(name: String): Flow<Gladiator>

    @Query("SELECT * FROM Gladiator WHERE ludusId = :id")
    fun getByLudusId(id: Int): Flow<List<Gladiator>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertGladiator(gladiator: Gladiator)

    @Update
    suspend fun updateGladiator(gladiator: Gladiator)

    @Query("DELETE FROM Gladiator WHERE id = :id")
    suspend fun deleteById(id: Int)



}
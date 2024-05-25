package com.doorxii.ludus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.doorxii.ludus.data.models.ludus.Ludus

@Dao
abstract class LudusDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertLudus(ludus: Ludus)

    @Query(value = "SELECT * FROM ludus")
    abstract suspend fun getAllLudus(): List<Ludus>

    @Query(value = "SELECT * FROM ludus WHERE id = :id")
    abstract suspend fun getLudus(id: Int): Ludus

    @Update
    abstract suspend fun updateLudusById(id: Int, ludus: Ludus)

    @Query(value = "DELETE FROM ludus WHERE id = :id")
    abstract suspend fun deleteLudusById(id: Int)


}
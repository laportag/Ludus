package com.doorxii.ludus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus

@Dao
interface LudusDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertLudus(ludus: Ludus)

    @Query(value = "SELECT * FROM ludus")
    fun getAllLudus(): List<Ludus>

    @Query(value = "SELECT * FROM ludus WHERE ludusId = :ludusId")
    fun getLudus(ludusId: Int): Ludus

    @Query(value = "SELECT * FROM ludus WHERE name = :name")
    fun getLudusByName(name: String): Ludus

    @Query(value = "SELECT * FROM ludus WHERE ludusId = :ludusId")
    fun getLudusById(ludusId: Int): Ludus

    @Update
    fun updateLudusById(ludus: Ludus)

    @Query(value = "DELETE FROM ludus WHERE ludusId = :ludusId")
    fun deleteLudusById(ludusId: Int)


}
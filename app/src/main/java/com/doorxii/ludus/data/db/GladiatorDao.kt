package com.doorxii.ludus.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus

@Dao
abstract class GladiatorDao {

    @Query("SELECT * FROM Gladiator")
    abstract fun getAll(): List<Gladiator>

    @Query("SELECT * FROM Gladiator WHERE id = :id")
    abstract fun getById(id: Int): Gladiator?

    @Query("SELECT * FROM Gladiator WHERE name = :name")
    abstract fun getByName(name: String): Gladiator?

    @Query("SELECT * FROM Gladiator WHERE ludusId = :id")
    abstract fun getByLudusId(id: Int): List<Gladiator>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertGladiator(gladiator: Gladiator)

    @Update
    abstract fun update(gladiator: Gladiator)

    @Query("DELETE FROM Gladiator WHERE id = :id")
    abstract fun deleteById(id: Int)



}
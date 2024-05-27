package com.doorxii.ludus.data.db

import androidx.room.withTransaction
import com.doorxii.ludus.data.models.animal.Gladiator
import com.doorxii.ludus.data.models.ludus.Ludus
import kotlinx.coroutines.flow.Flow

class LudusRepository(db: AppDatabase) {
    val db = db
    val ludusDao = db.ludusDao()
    val gladiatorDao = db.gladiatorDao()

    suspend fun addLudus(ludus: Ludus) = ludusDao.insertLudus(ludus)

    fun getAllLudi(): Flow<List<Ludus>> = ludusDao.getAllLudus()

    fun getLudusByName(name: String): Flow<Ludus> = ludusDao.getLudusByName(name)

    suspend fun updateLudusById(ludus: Ludus) = ludusDao.updateLudusById(ludus)

    suspend fun deleteLudusById(ludusId: Int) = ludusDao.deleteLudusById(ludusId)

    suspend fun insertGladiator(gladiator: Gladiator) = gladiatorDao.insertGladiator(gladiator)


    fun getAllGladiators(): Flow<List<Gladiator>> = gladiatorDao.getAll()

    fun getGladiator(gladiatorId: Int): Flow<Gladiator> = gladiatorDao.getById(gladiatorId)

    fun getGladiatorByName(name: String): Flow<Gladiator> = gladiatorDao.getByName(name)

    fun getGladiatorsByLudusId(ludusId: Int): Flow<List<Gladiator>> = gladiatorDao.getByLudusId(ludusId)

    suspend fun updateGladiator(gladiator: Gladiator) = gladiatorDao.updateGladiator(gladiator)

    suspend fun deleteGladiator(gladiator: Gladiator) = gladiatorDao.deleteById(gladiator.gladiatorId)


}
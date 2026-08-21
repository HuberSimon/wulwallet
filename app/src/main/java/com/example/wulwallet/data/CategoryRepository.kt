package com.example.wulwallet.data

import com.example.wulwallet.data.local.Person
import com.example.wulwallet.data.local.PersonDao
import kotlinx.coroutines.flow.Flow

class PersonRepository(private val personDao: PersonDao) {

    val allPersons: Flow<List<Person>> = personDao.getAllPersons()

    val personCount: Flow<Int> = personDao.getPersonCount()

    suspend fun insertPerson(person: Person) {
        personDao.insertPerson(person)
    }

    suspend fun updatePerson(person: Person) {
        personDao.updatePerson(person)
    }

    suspend fun deletePerson(person: Person) {
        personDao.deletePerson(person)
    }

    suspend fun getPersonById(id: Int): Person? {
        return personDao.getPersonById(id)
    }
}

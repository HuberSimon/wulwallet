package com.example.wulwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wulwallet.data.PersonRepository
import com.example.wulwallet.data.local.Person
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonViewModel(
    private val repository: PersonRepository,
) : ViewModel() {

    private val _selectedPerson = MutableStateFlow<Person?>(null)
    val selectedPerson: StateFlow<Person?> = _selectedPerson

    val allPersons: StateFlow<List<Person>> = repository.allPersons
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )

    val personCount = repository.personCount.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        0
    )

    fun addPerson(person: Person) {
        viewModelScope.launch {
            repository.insertPerson(person)
        }
    }

    fun updatePerson(person: Person) {
        viewModelScope.launch {
            repository.updatePerson(person)
        }
    }

    fun deletePerson(person: Person) {
        viewModelScope.launch {
            repository.deletePerson(person)
        }
    }

    fun getPersonById(id: Int) {
        viewModelScope.launch {
            val person = repository.getPersonById(id)
            _selectedPerson.value = person
        }
    }
}

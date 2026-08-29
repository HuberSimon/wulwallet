package com.example.wulwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wulwallet.data.CategoryRepository
import com.example.wulwallet.data.local.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    // ============================================================
    // ALLE KATEGORIEN
    // ============================================================

    val categories =
        repository.getAllCategories()


    // Alias für bestehende Screens
    val allCategories =
        categories


    // ============================================================
    // AUSGEWÄHLTE KATEGORIE
    // ============================================================

    private val _selectedCategory =
        MutableStateFlow<Category?>(null)

    val selectedCategory: StateFlow<Category?> =
        _selectedCategory.asStateFlow()


    // ============================================================
    // KATEGORIE LADEN
    // ============================================================

    fun getCategoryById(
        id: Int
    ) {

        viewModelScope.launch {

            _selectedCategory.value =
                repository.getCategoryById(
                    id
                )
        }
    }


    // ============================================================
    // KATEGORIE EINFÜGEN
    // ============================================================

    fun insertCategory(
        category: Category
    ) {

        viewModelScope.launch {

            repository.insertCategory(
                category
            )
        }
    }


    // Alias für bestehende Screens
    fun addCategory(
        category: Category
    ) {

        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }


    // ============================================================
    // KATEGORIE AKTUALISIEREN
    // ============================================================

    fun updateCategory(
        category: Category
    ) {

        viewModelScope.launch {

            repository.updateCategory(
                category
            )

            _selectedCategory.value =
                repository.getCategoryById(
                    category.id
                )
        }
    }


    // ============================================================
    // KATEGORIE LÖSCHEN
    // ============================================================

    fun deleteCategory(
        category: Category
    ) {

        viewModelScope.launch {

            repository.deleteCategory(
                category
            )

            if (
                _selectedCategory.value?.id ==
                category.id
            ) {

                _selectedCategory.value =
                    null
            }
        }
    }
}
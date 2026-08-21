package com.example.wulwallet.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wulwallet.data.CategoryRepository
import com.example.wulwallet.data.local.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: CategoryRepository,
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    val allCategories: StateFlow<List<Category>> = repository.allCategories
        .stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            emptyList()
        )


    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun getCategoryById(id: Int) {
        viewModelScope.launch {
            val category = repository.getCategoryById(id)
            _selectedCategory.value = category
        }
    }
}

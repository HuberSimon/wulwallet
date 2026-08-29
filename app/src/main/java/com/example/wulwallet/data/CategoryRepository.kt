package com.example.wulwallet.data

import androidx.room.withTransaction
import com.example.wulwallet.data.local.AppDatabase
import com.example.wulwallet.data.local.Category
import com.example.wulwallet.data.local.CategoryDao

class CategoryRepository(
    private val dao: CategoryDao,
    private val database: AppDatabase
) {

    // ============================================================
    // ALLE KATEGORIEN
    // ============================================================

    fun getAllCategories() =
        dao.getAllCategories()


    // ============================================================
    // KATEGORIE NACH ID
    // ============================================================

    suspend fun getCategoryById(
        id: Int
    ): Category? {

        return dao.getCategoryById(id)
    }


    // ============================================================
    // KATEGORIE EINFÜGEN
    // ============================================================

    suspend fun insertCategory(
        category: Category
    ) {

        dao.insertCategory(
            category
        )
    }


    // ============================================================
    // KATEGORIE AKTUALISIEREN
    // ============================================================

    suspend fun updateCategory(
        category: Category
    ) {

        dao.updateCategory(
            category
        )
    }


    // ============================================================
    // KATEGORIE + ALLE ZUGEHÖRIGEN KOSTEN LÖSCHEN
    // ============================================================

    suspend fun deleteCategory(
        category: Category
    ) {

        database.withTransaction {

            // Alle Kosten dieser Kategorie
            // inklusive Payer und Participant löschen.
            database
                .costsDao()
                .deleteCostsForCategory(
                    category.id
                )

            // Danach Kategorie löschen.
            dao.deleteCategory(
                category
            )
        }
    }
}
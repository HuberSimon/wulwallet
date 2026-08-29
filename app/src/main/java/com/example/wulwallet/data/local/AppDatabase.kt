// ================================================================
// FILE: data/local/AppDatabase.kt
// ================================================================

package com.example.wulwallet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        User::class,
        Category::class,
        Costs::class,
        CostPayer::class,
        CostParticipant::class,
        Todo::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(TypeConvert::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    abstract fun categoryDao(): CategoryDao

    abstract fun costsDao(): CostsDao

    abstract fun todoDao(): TodoDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null


        fun getDatabase(
            context: Context
        ): AppDatabase {

            return INSTANCE
                ?: synchronized(this) {

                    val instance =
                        Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "app_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance

                    instance
                }
        }
    }
}
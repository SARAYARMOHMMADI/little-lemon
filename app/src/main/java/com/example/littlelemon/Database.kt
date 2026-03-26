package com.example.littlelemon

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

@Entity
data class MenuItemRoom(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val image: String,
    val category: String
)

@Dao
interface MenuItemDao {
    @Query("SELECT * FROM MenuItemRoom")
    fun getAll(): LiveData<List<MenuItemRoom>>
    @Insert
    fun insertAll(vararg menuItems: MenuItemRoom)
    @Query("SELECT (SELECT COUNT(*) FROM MenuItemRoom) == 0")
    fun isEmpty(): Boolean

    @Query("SELECT * FROM MenuItemRoom WHERE id = :id")
    fun getById(id: Int): LiveData<MenuItemRoom>
}

@Database(entities = [MenuItemRoom::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun menuItemDao(): MenuItemDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "little_lemon_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
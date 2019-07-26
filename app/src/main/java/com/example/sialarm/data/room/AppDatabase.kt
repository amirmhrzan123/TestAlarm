package com.example.sialarm.data.room

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sialarm.data.room.dao.UserDao

@Database(entities = [
    UserTable::class


], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}


val MIGRATION_1_2: Migration = object : Migration(0, 1) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Since we didn't alter the table, there's nothing else to do here.
    }
}

@Entity
data class UserTable(
    @PrimaryKey val value:String)
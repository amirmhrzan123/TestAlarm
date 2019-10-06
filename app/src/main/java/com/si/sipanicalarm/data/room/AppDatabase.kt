package com.si.sipanicalarm.data.room

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.si.sipanicalarm.data.room.dao.NotificationDao

@Database(entities = [
    NotificationCountTable::class


], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getNotificationDao(): NotificationDao
}


val MIGRATION_1_2: Migration = object : Migration(0, 1) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Since we didn't alter the table, there's nothing else to do here.
    }
}

@Entity(tableName = "notification_count")
data class NotificationCountTable(
    @PrimaryKey
    var id:String ,
    var count:Int = 0
)
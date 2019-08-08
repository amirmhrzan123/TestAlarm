package com.example.sialarm.data.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.sialarm.data.room.NotificationCountTable


@Dao
interface NotificationDao {
    /**
     * Insert an object in the database.
     *
     * @param obj the object to be inserted.
     */
  /*  @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserInfo(obj: UserTable): Long

    @Query("UPDATE user SET profile_image = :url WHERE _id= :userId")
    fun updateProfileImage(url: String, userId: String)*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setNotificationCount(obj: NotificationCountTable)


    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateNotificationCount(obj: NotificationCountTable)

    @Query("SELECT * FROM notification_count WHERE id = :notificationId")
    fun getNotificationCount(notificationId:String): LiveData<NotificationCountTable>

    @Query("SELECT count FROM notification_count WHERE id = :notiicationId")
    fun getBadgeCount(notiicationId:String):Int




}

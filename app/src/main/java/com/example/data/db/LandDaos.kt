package com.example.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LandDao {
    @Query("SELECT * FROM land_records ORDER BY id DESC")
    fun getAllLandRecords(): Flow<List<LandRecord>>

    @Query("SELECT * FROM land_records WHERE khatianNo LIKE '%' || :khatian || '%' OR ownerName LIKE '%' || :owner || '%' OR dagNo LIKE '%' || :dag || '%'")
    fun searchLandRecords(khatian: String, owner: String, dag: String): Flow<List<LandRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLandRecord(land: LandRecord)

    @Delete
    suspend fun deleteLandRecord(land: LandRecord)
}

@Dao
interface DocumentDao {
    @Query("SELECT * FROM land_documents ORDER BY id DESC")
    fun getAllDocuments(): Flow<List<LandDocument>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDocument(doc: LandDocument)

    @Delete
    suspend fun deleteDocument(doc: LandDocument)
}

@Dao
interface PaymentDao {
    @Query("SELECT * FROM payment_records ORDER BY id DESC")
    fun getAllPayments(): Flow<List<PaymentRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentRecord)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)
}

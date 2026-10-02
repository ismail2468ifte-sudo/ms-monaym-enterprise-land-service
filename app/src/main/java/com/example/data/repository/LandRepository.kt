package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.LandDocument
import com.example.data.db.LandRecord
import com.example.data.db.NotificationItem
import com.example.data.db.PaymentRecord
import kotlinx.coroutines.flow.Flow

class LandRepository(private val database: AppDatabase) {

    val allLandRecords: Flow<List<LandRecord>> = database.landDao().getAllLandRecords()
    val allDocuments: Flow<List<LandDocument>> = database.documentDao().getAllDocuments()
    val allPayments: Flow<List<PaymentRecord>> = database.paymentDao().getAllPayments()
    val allNotifications: Flow<List<NotificationItem>> = database.notificationDao().getAllNotifications()

    fun searchLand(query: String): Flow<List<LandRecord>> {
        return database.landDao().searchLandRecords(query, query, query)
    }

    suspend fun addLandRecord(landRecord: LandRecord) {
        database.landDao().insertLandRecord(landRecord)
    }

    suspend fun deleteLandRecord(landRecord: LandRecord) {
        database.landDao().deleteLandRecord(landRecord)
    }

    suspend fun addDocument(document: LandDocument) {
        database.documentDao().insertDocument(document)
    }

    suspend fun deleteDocument(document: LandDocument) {
        database.documentDao().deleteDocument(document)
    }

    suspend fun addPayment(payment: PaymentRecord) {
        database.paymentDao().insertPayment(payment)
    }

    suspend fun addNotification(notification: NotificationItem) {
        database.notificationDao().insertNotification(notification)
    }

    suspend fun markNotificationRead(id: Int) {
        database.notificationDao().markAsRead(id)
    }
}

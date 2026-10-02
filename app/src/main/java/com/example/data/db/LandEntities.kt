package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "land_records")
data class LandRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val khatianNo: String,
    val dagNo: String,
    val mouza: String,
    val district: String,
    val ownerName: String,
    val area: String,
    val landType: String = "কৃষি / Nal",
    val status: String = "যাচাইকৃত (Verified)",
    val lat: Double = 23.8103,
    val lng: Double = 90.4125
)

@Entity(tableName = "land_documents")
data class LandDocument(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val documentType: String,
    val fileName: String,
    val fileSize: String,
    val uploadDate: String,
    val khatianNo: String
)

@Entity(tableName = "payment_records")
data class PaymentRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerName: String,
    val customerMobile: String,
    val amount: Double,
    val method: String,
    val transactionId: String,
    val purpose: String,
    val status: String = "সফল (Successful)",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val message: String,
    val date: String,
    val isRead: Boolean = false
)

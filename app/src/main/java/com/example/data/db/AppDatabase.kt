package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        LandRecord::class,
        LandDocument::class,
        PaymentRecord::class,
        NotificationItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun landDao(): LandDao
    abstract fun documentDao(): DocumentDao
    abstract fun paymentDao(): PaymentDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ms_monaym_land_database"
                )
                    .addCallback(DatabaseCallback(context.applicationContext))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database)
                    }
                }
            }

            suspend fun populateInitialData(db: AppDatabase) {
                // Populate default land records
                db.landDao().insertLandRecord(
                    LandRecord(
                        khatianNo = "১০৫৪",
                        dagNo = "২৩৪৫",
                        mouza = "মিরপুর (Mirpur)",
                        district = "ঢাকা (Dhaka)",
                        ownerName = "এম. এস. মোনায়েম (M.S Monaym)",
                        area = "৩৫.৫০ শতক",
                        landType = "বাস্তু ও বাণিজ্যিক",
                        status = "যাচাইকৃত (Verified)",
                        lat = 23.8103,
                        lng = 90.4125
                    )
                )
                db.landDao().insertLandRecord(
                    LandRecord(
                        khatianNo = "৮৮২",
                        dagNo = "১১৯০",
                        mouza = "ধানমন্ডি (Dhanmondi)",
                        district = "ঢাকা (Dhaka)",
                        ownerName = "মোনায়েম এন্টারপ্রাইজ ট্রাস্ট",
                        area = "১২.০০ শতক",
                        landType = "আবাসিক (Residential)",
                        status = "যাচাইকৃত (Verified)",
                        lat = 23.7461,
                        lng = 90.3742
                    )
                )
                db.landDao().insertLandRecord(
                    LandRecord(
                        khatianNo = "৪২০১",
                        dagNo = "৫৬৭",
                        mouza = "পতেঙ্গা (Patenga)",
                        district = "চট্টগ্রাম (Chattogram)",
                        ownerName = "আব্দুল করিম ও অংশীদারগণ",
                        area = "৫০.০০ শতক",
                        landType = "কৃষি ও বাণিজ্যিক",
                        status = "প্রসেসিং (Processing)",
                        lat = 22.2359,
                        lng = 91.7915
                    )
                )

                // Default Documents
                db.documentDao().insertDocument(
                    LandDocument(
                        title = "মূল ক্রয় দলিল (Khatian 1054)",
                        documentType = "deed",
                        fileName = "Deed_1054_Monaym.pdf",
                        fileSize = "2.4 MB",
                        uploadDate = "2026-08-01",
                        khatianNo = "১০৫৪"
                    )
                )
                db.documentDao().insertDocument(
                    LandDocument(
                        title = "নামজারি খতিয়ান রসিদ (Mutation)",
                        documentType = "mutation",
                        fileName = "Porcha_Namjari_882.pdf",
                        fileSize = "1.8 MB",
                        uploadDate = "2026-08-03",
                        khatianNo = "৮৮২"
                    )
                )

                // Default Payments
                db.paymentDao().insertPayment(
                    PaymentRecord(
                        customerName = "এম. এস. মোনায়েম",
                        customerMobile = "01700000000",
                        amount = 1250.0,
                        method = "bKash",
                        transactionId = "TRX9928310",
                        purpose = "ভূমি উন্নয়ন কর (Land Development Tax 1431 BS)",
                        status = "সফল (Successful)",
                        timestamp = System.currentTimeMillis() - 86400000
                    )
                )

                // Default Notifications
                db.notificationDao().insertNotification(
                    NotificationItem(
                        title = "জমির তথ্য যাচাই সম্পন্ন",
                        message = "আপনার খতিয়ান নং ১০৫৪ এর তথ্য ভূমি রেজিস্ট্রি ডাটাবেজে সফলভাবে হালনাগাদ করা হয়েছে।",
                        date = "আজ, ০৬ আগস্ট ২০২৬",
                        isRead = false
                    )
                )
                db.notificationDao().insertNotification(
                    NotificationItem(
                        title = "খাজনা পরিশোধ নিশ্চিতকরণ",
                        message = "bKash এর মাধ্যমে ১,২৫০ টাকা ভূমি উন্নয়ন কর পরিশোধ সফল হয়েছে। ট্রানজেকশন ID: TRX9928310",
                        date = "গতকাল",
                        isRead = true
                    )
                )
            }
        }
    }
}

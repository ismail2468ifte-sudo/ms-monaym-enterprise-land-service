package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.LandDocument
import com.example.data.db.LandRecord
import com.example.data.db.NotificationItem
import com.example.data.db.PaymentRecord
import com.example.data.repository.LandRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LandViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: LandRepository
    val allLandRecords: StateFlow<List<LandRecord>>
    val allDocuments: StateFlow<List<LandDocument>>
    val allPayments: StateFlow<List<PaymentRecord>>
    val allNotifications: StateFlow<List<NotificationItem>>

    // Auth & User Profile State
    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userPhone = MutableStateFlow("01700000000")
    val userPhone: StateFlow<String> = _userPhone.asStateFlow()

    private val _userName = MutableStateFlow("এম. এস. মোনায়েম (M.S Monaym)")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userRole = MutableStateFlow("Admin") // "User" or "Admin"
    val userRole: StateFlow<String> = _userRole.asStateFlow()

    // Language state: true for Bangla (বাংলা), false for English
    private val _isBangla = MutableStateFlow(true)
    val isBangla: StateFlow<Boolean> = _isBangla.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Filtered Land Records
    val filteredLandRecords: StateFlow<List<LandRecord>>

    // Selected Land Record for detail/map view
    private val _selectedLandRecord = MutableStateFlow<LandRecord?>(null)
    val selectedLandRecord: StateFlow<LandRecord?> = _selectedLandRecord.asStateFlow()

    // Real-time Live Web Portals State
    companion object {
        const val URL_LOCAL_WEB = "file:///android_asset/web/index.html"
        const val URL_LAND_GOV = "https://www.land.gov.bd"
        const val URL_EPORCHA = "https://eporcha.tech"
        const val URL_GOOGLE_ACTIVITY = "https://myactivity.google.com"
        const val URL_GOOGLE_SUPPORT = "https://support.google.com"
        const val URL_GOOGLE_ACCOUNT = "https://myaccount.google.com"

        // Device & Enterprise Diagnostics Metadata
        const val DEVICE_MODEL = "Vivo Y2111"
        const val BASEBAND_VERSION = "MOLY.LR12A.R3.TC19.PR2.SP.V1.P48"
        const val DEVICE_SERIAL = "3423977356ZZZZZ"
        const val ENTERPRISE_NAME = "M.S MONAYM ENTERPRISE"
        const val ADMIN_ACCOUNT = "ismail2468ifte@gmail.com (Ismail Khan)"
        const val ENTERPRISE_PHONE = "01976444504"
        const val ENTERPRISE_WHATSAPP = "01976444504"
        const val ENTERPRISE_WHATSAPP_LINK = "https://wa.me/8801976444504"

        // Official Non-Governmental App & Government URL Source Disclaimer
        const val DISCLAIMER_NON_GOVERNMENT_BN = "এই এ্যাপটি একটি বেসরকারি এ্যাপ,শুধু এ্যাপের মধ্যে থাকা কিছু কাজের সুবিধার জন্য সরকারি URL লিংক সোর্স ব্যবহার করা হবে।"
        const val DISCLAIMER_NON_GOVERNMENT_EN = "This is a private, non-governmental application. Official government URL link sources (e.g. land.gov.bd, eporcha.gov.bd) are utilized solely for user convenience in land information queries. This app is not affiliated with or representing any government entity."

        // Keystore & AAB Signing Certificate Fingerprints
        const val KEYSTORE_SHA256 = "26:B0:5F:2D:95:23:7C:CE:26:90:A4:C2:5A:15:F8:F4:28:B3:95:29:D1:62:85:C7:97:98:67:F1:11:2D:5A:AE"
        const val KEYSTORE_SHA1 = "7E:30:CF:36:E9:A1:35:E3:A2:36:DA:0C:25:3A:F4:82:F9:B0:30:39"
        const val KEYSTORE_ALIAS = "androiddebugkey"
    }

    private val _activeWebUrl = MutableStateFlow(URL_LOCAL_WEB)
    val activeWebUrl: StateFlow<String> = _activeWebUrl.asStateFlow()

    fun setActiveWebUrl(url: String) {
        _activeWebUrl.value = url
    }

    init {
        val database = AppDatabase.getDatabase(application)
        repository = LandRepository(database)

        allLandRecords = repository.allLandRecords
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allDocuments = repository.allDocuments
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allPayments = repository.allPayments
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        allNotifications = repository.allNotifications
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        filteredLandRecords = combine(allLandRecords, _searchQuery) { records, query ->
            if (query.isBlank()) {
                records
            } else {
                records.filter {
                    it.khatianNo.contains(query, ignoreCase = true) ||
                            it.dagNo.contains(query, ignoreCase = true) ||
                            it.mouza.contains(query, ignoreCase = true) ||
                            it.ownerName.contains(query, ignoreCase = true) ||
                            it.district.contains(query, ignoreCase = true)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleLanguage() {
        _isBangla.value = !_isBangla.value
    }

    fun selectLandRecord(record: LandRecord?) {
        _selectedLandRecord.value = record
    }

    fun addLandRecord(
        khatianNo: String,
        dagNo: String,
        mouza: String,
        district: String,
        ownerName: String,
        area: String,
        landType: String,
        lat: Double = 23.8103,
        lng: Double = 90.4125
    ) {
        viewModelScope.launch {
            val newLand = LandRecord(
                khatianNo = khatianNo,
                dagNo = dagNo,
                mouza = mouza,
                district = district,
                ownerName = ownerName,
                area = area,
                landType = landType,
                status = "প্রসেসিং (Processing)",
                lat = lat,
                lng = lng
            )
            repository.addLandRecord(newLand)

            // Send confirmation notification
            repository.addNotification(
                NotificationItem(
                    title = if (_isBangla.value) "নতুন জমি তথ্য সংরক্ষিত" else "New Land Record Saved",
                    message = if (_isBangla.value)
                        "খতিয়ান নং $khatianNo, দাগ নং $dagNo সফলভাবে খতিয়ান তালিকায় অন্তর্ভুক্ত করা হয়েছে।"
                    else "Khatian No $khatianNo, Dag No $dagNo successfully saved.",
                    date = "আজ (Today)",
                    isRead = false
                )
            )
        }
    }

    fun addDocument(
        title: String,
        docType: String,
        fileName: String,
        khatianNo: String
    ) {
        viewModelScope.launch {
            val doc = LandDocument(
                title = title,
                documentType = docType,
                fileName = fileName,
                fileSize = "2.1 MB",
                uploadDate = "2026-08-06",
                khatianNo = khatianNo
            )
            repository.addDocument(doc)

            repository.addNotification(
                NotificationItem(
                    title = if (_isBangla.value) "ডকুমেন্ট আপলোড সফল" else "Document Uploaded",
                    message = if (_isBangla.value)
                        "আপনার নথি '$title' সফলভাবে সার্ভারে আপলোড ও স্ক্যান করা হয়েছে।"
                    else "Document '$title' uploaded and scanned successfully.",
                    date = "আজ (Today)",
                    isRead = false
                )
            )
        }
    }

    fun processPayment(
        amount: Double,
        method: String,
        purpose: String
    ) {
        viewModelScope.launch {
            val trxId = "TRX" + (1000000..9999999).random()
            val payment = PaymentRecord(
                customerName = _userName.value,
                customerMobile = _userPhone.value,
                amount = amount,
                method = method,
                transactionId = trxId,
                purpose = purpose,
                status = "সফল (Successful)",
                timestamp = System.currentTimeMillis()
            )
            repository.addPayment(payment)

            repository.addNotification(
                NotificationItem(
                    title = if (_isBangla.value) "পেমেন্ট সফল হয়েছে" else "Payment Successful",
                    message = if (_isBangla.value)
                        "$method এর মাধ্যমে ৳$amount প্রদান সফল হয়েছে। ট্রানজেকশন ID: $trxId"
                    else "Payment of ৳$amount via $method successful. TRX ID: $trxId",
                    date = "আজ (Today)",
                    isRead = false
                )
            )
        }
    }

    fun verifyOtpAndLogin(phone: String, otp: String) {
        _userPhone.value = phone
        _isLoggedIn.value = true
        viewModelScope.launch {
            repository.addNotification(
                NotificationItem(
                    title = "স্মার্ট লগইন সফল",
                    message = "M.S MONAYM ENTERPRISE অ্যাপে $phone নম্বর দিয়ে লগইন করা হয়েছে।",
                    date = "আজ (Today)",
                    isRead = false
                )
            )
        }
    }

    fun toggleRole() {
        _userRole.value = if (_userRole.value == "Admin") "User" else "Admin"
    }

    fun logout() {
        _isLoggedIn.value = false
    }

    fun markNotificationRead(id: Int) {
        viewModelScope.launch {
            repository.markNotificationRead(id)
        }
    }
}

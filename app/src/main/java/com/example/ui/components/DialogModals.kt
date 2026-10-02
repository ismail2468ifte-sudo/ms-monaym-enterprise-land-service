package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContactPage
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.R
import com.example.ui.theme.BkashColor
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.theme.NagadColor
import com.example.ui.theme.RocketColor
import com.example.ui.theme.UpayColor
import androidx.compose.material3.MaterialTheme
import com.example.util.BangladeshPhoneValidator

@Composable
fun AddLandDialog(
    isBangla: Boolean,
    onDismiss: () -> Unit,
    onAdd: (khatian: String, dag: String, mouza: String, district: String, owner: String, area: String, landType: String) -> Unit
) {
    var khatianNo by remember { mutableStateOf("") }
    var dagNo by remember { mutableStateOf("") }
    var mouza by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("ঢাকা") }
    var ownerName by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var landType by remember { mutableStateOf("কৃষি / Nal") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isBangla) "নতুন জমির তথ্য সংরক্ষণ" else "Add New Land Record",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = khatianNo,
                    onValueChange = { khatianNo = it },
                    label = { Text(if (isBangla) "খতিয়ান নম্বর (Khatian No)" else "Khatian No") },
                    modifier = Modifier.fillMaxWidth().testTag("add_khatian_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = dagNo,
                    onValueChange = { dagNo = it },
                    label = { Text(if (isBangla) "দাগ নম্বর (Dag No)" else "Dag No") },
                    modifier = Modifier.fillMaxWidth().testTag("add_dag_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = mouza,
                    onValueChange = { mouza = it },
                    label = { Text(if (isBangla) "মৌজা (Mouza)" else "Mouza") },
                    modifier = Modifier.fillMaxWidth().testTag("add_mouza_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = district,
                    onValueChange = { district = it },
                    label = { Text(if (isBangla) "জেলা (District)" else "District") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    label = { Text(if (isBangla) "মালিকের নাম (Owner Name)" else "Owner Name") },
                    modifier = Modifier.fillMaxWidth().testTag("add_owner_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = area,
                    onValueChange = { area = it },
                    label = { Text(if (isBangla) "জমির পরিমাণ (যেমন: ২৫ শতক)" else "Land Area (e.g., 25 Shotok)") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (khatianNo.isNotBlank() && dagNo.isNotBlank()) {
                        onAdd(
                            khatianNo,
                            dagNo,
                            mouza.ifBlank { "সদর" },
                            district.ifBlank { "ঢাকা" },
                            ownerName.ifBlank { "ইউজার" },
                            area.ifBlank { "১০ শতক" },
                            landType
                        )
                        onDismiss()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                modifier = Modifier.testTag("save_land_record_btn")
            ) {
                Text(if (isBangla) "সংরক্ষণ করুন" else "Save Record")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(if (isBangla) "বাতিল" else "Cancel")
            }
        }
    )
}

@Composable
fun UploadDocumentDialog(
    isBangla: Boolean,
    onDismiss: () -> Unit,
    onUpload: (title: String, docType: String, fileName: String, khatianNo: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var docType by remember { mutableStateOf("মূল দলিল (Deed)") }
    var khatianNo by remember { mutableStateOf("") }
    var isSimulatingCamera by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isBangla) "নথি স্ক্যান ও আপলোড" else "Scan & Upload Document",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(if (isBangla) "নথির শিরোনাম (Document Title)" else "Document Title") },
                    modifier = Modifier.fillMaxWidth().testTag("doc_title_input"),
                    singleLine = true
                )
                OutlinedTextField(
                    value = khatianNo,
                    onValueChange = { khatianNo = it },
                    label = { Text(if (isBangla) "সংশ্লিষ্ট খতিয়ান নং (Khatian No)" else "Associated Khatian No") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Camera Scanner Simulation Box
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clickable { isSimulatingCamera = true },
                    color = MonaymPrimary.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = null,
                                tint = MonaymPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isSimulatingCamera)
                                    (if (isBangla) "✓ দলিল ক্যা্যাপচার সম্পন্ন" else "✓ Document Captured")
                                else
                                    (if (isBangla) "ক্যামেরা দিয়ে ফটো তুলুন / স্ক্যান করুন" else "Tap to Capture via Camera"),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSimulatingCamera) MonaymGreen else MonaymPrimary
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val finalTitle = title.ifBlank { "মূল দলিল স্ক্যান" }
                    val finalKhatian = khatianNo.ifBlank { "১০৫৪" }
                    onUpload(finalTitle, docType, "Scanned_Deed_${System.currentTimeMillis().toString().takeLast(4)}.pdf", finalKhatian)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                modifier = Modifier.testTag("confirm_doc_upload_btn")
            ) {
                Text(if (isBangla) "আপলোড নিশ্চিত করুন" else "Confirm Upload")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(if (isBangla) "বাতিল" else "Cancel")
            }
        }
    )
}

@Composable
fun ProcessPaymentDialog(
    isBangla: Boolean,
    onDismiss: () -> Unit,
    onPay: (amount: Double, method: String, purpose: String) -> Unit
) {
    var amountText by remember { mutableStateOf("1250") }
    var selectedMethod by remember { mutableStateOf("bKash") }
    var purpose by remember { mutableStateOf("ভূমি উন্নয়ন কর (Land Tax 1431 BS)") }
    var walletNumber by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }
    var trxId by remember { mutableStateOf("") }
    var isNumberCopied by remember { mutableStateOf(false) }

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val adminSendMoneyNumber = "01976444504"

    val mfsMethods = listOf(
        Triple("bKash", BkashColor, "01XXXXXXXXX (bKash Wallet)"),
        Triple("Nagad", NagadColor, "01XXXXXXXXX (Nagad Wallet)"),
        Triple("Rocket", RocketColor, "01XXXXXXXXXX (Rocket 12-digit)"),
        Triple("Upay", UpayColor, "01XXXXXXXXX (Upay Wallet)")
    )

    val currentPlaceholder = when (selectedMethod) {
        "bKash" -> if (isBangla) "বিকাশ ওয়ালেট নম্বর (যেমন: 017XXXXXXXX)" else "bKash Wallet No (e.g. 017XXXXXXXX)"
        "Nagad" -> if (isBangla) "নগদ ওয়ালেট নম্বর (যেমন: 018XXXXXXXX)" else "Nagad Wallet No (e.g. 018XXXXXXXX)"
        "Rocket" -> if (isBangla) "রকেট ১২ ডিজিট অ্যাকাউন্ট (যেমন: 019XXXXXXXXX)" else "Rocket 12-digit Account (019XXXXXXXXX)"
        else -> if (isBangla) "উপায় অ্যাকাউন্ট নম্বর (যেমন: 016XXXXXXXX)" else "Upay Wallet No (016XXXXXXXX)"
    }

    val selectedColor = when (selectedMethod) {
        "bKash" -> BkashColor
        "Nagad" -> NagadColor
        "Rocket" -> RocketColor
        "Upay" -> UpayColor
        else -> MonaymPrimary
    }

    val isRocket = selectedMethod == "Rocket"
    val maxPhoneLen = if (isRocket) 12 else 11
    val phoneValidation = remember(walletNumber, isBangla, isRocket) {
        BangladeshPhoneValidator.validate(walletNumber, isBangla, isRocket)
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(selectedColor)
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = selectedColor,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isBangla) "ডিজিটাল ভূমি সেবা ফি পরিশোধ" else "Land Service E-Payment",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (isBangla) "স্মার্ট গেটওয়ে • $selectedMethod" else "Smart Gateway • $selectedMethod",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp
                            )
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_payment_modal_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Quick Fee Presets
                    Text(
                        text = if (isBangla) "সেবার ধরন দ্রুত নির্বাচন করুন" else "Quick Service Preset",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MonaymPrimaryDark
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            Triple(if (isBangla) "খতিয়ান" else "Khatian", "100", "খতিয়ান সার্টিফাইড কপি ফি (Khatian Copy Fee)"),
                            Triple(if (isBangla) "নামজারি" else "Mutation", "1150", "নামজারি আবেদন ও কোর্ট ফি (Mutation Application Fee)"),
                            Triple(if (isBangla) "ভূমি কর" else "Tax", "1250", "ভূমি উন্নয়ন কর (Land Tax 1431 BS)"),
                            Triple(if (isBangla) "নকশা" else "Map", "520", "মৌজা নকশা প্রিন্ট ফি (Mouza Map Fee)")
                        ).forEach { preset ->
                            val isChosen = amountText == preset.second
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChosen) selectedColor.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = if (isChosen) androidx.compose.foundation.BorderStroke(1.dp, selectedColor) else null,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        amountText = preset.second
                                        purpose = preset.third
                                    }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = preset.first, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = if (isChosen) selectedColor else MonaymDarkNavy)
                                    Text(text = "৳${preset.second}", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MonaymPrimaryDark)
                                }
                            }
                        }
                    }

                    // Purpose & Amount Inputs
                    OutlinedTextField(
                        value = purpose,
                        onValueChange = { purpose = it },
                        label = { Text(if (isBangla) "সেবার নাম / বিবরণ" else "Service / Purpose") },
                        modifier = Modifier.fillMaxWidth().testTag("payment_purpose_input"),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text(if (isBangla) "টাকার পরিমাণ (৳)" else "Payable Amount (৳)") },
                        leadingIcon = {
                            Text(
                                text = "৳",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = selectedColor,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        },
                        modifier = Modifier.fillMaxWidth().testTag("payment_amount_input"),
                        singleLine = true
                    )

                    // Payment Provider Selector
                    Text(
                        text = if (isBangla) "মোবাইল ফিন্যান্সিয়াল সার্ভিস (MFS) নির্বাচন করুন" else "Select Mobile Payment Method",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        mfsMethods.forEach { (name, color, _) ->
                            val isSelected = selectedMethod == name
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) color else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedMethod = name }
                                    .testTag("method_option_$name")
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = name,
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Admin Send Money Card in Dialog
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("dialog_admin_send_money_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, selectedColor.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isBangla) "অ্যাডমিন সেন্টমানি নম্বর (বিকাশ/নগদ):" else "Admin Send Money (bKash/Nagad):",
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = adminSendMoneyNumber,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = selectedColor,
                                        letterSpacing = 0.5.sp
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Button(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(adminSendMoneyNumber))
                                            isNumberCopied = true
                                            Toast.makeText(
                                                context,
                                                if (isBangla) "নম্বর কপি হয়েছে: $adminSendMoneyNumber" else "Copied: $adminSendMoneyNumber",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isNumberCopied) MonaymGreen else selectedColor
                                        ),
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isNumberCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = if (isNumberCopied) (if (isBangla) "কপি হয়েছে" else "Copied") else (if (isBangla) "কপি" else "Copy"),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    OutlinedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$adminSendMoneyNumber"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(6.dp),
                                        modifier = Modifier.height(34.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Call, contentDescription = "Dial", modifier = Modifier.size(14.dp))
                                    }
                                }
                            }

                            Text(
                                text = if (isBangla) "💡 বিকাশ বা নগদ থেকে সেন্টমানি (Send Money) করে নিচের ঘরে TrxID দিন।"
                                else "💡 Send Money to $adminSendMoneyNumber via bKash/Nagad & enter TrxID.",
                                fontSize = 10.5.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    // Provider Specific Details Card with Placeholders
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = selectedColor.copy(alpha = 0.08f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = selectedColor,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "$selectedMethod ${if (isBangla) "প্রেরকের তথ্য ও TrxID" else "Sender Details & TrxID"}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = selectedColor
                                )
                            }

                            // Wallet Number Input with Real-time Validation
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                OutlinedTextField(
                                    value = walletNumber,
                                    onValueChange = { input ->
                                        walletNumber = BangladeshPhoneValidator.cleanDigits(input, maxPhoneLen)
                                    },
                                    placeholder = { Text(currentPlaceholder, fontSize = 12.sp, color = Color.Gray) },
                                    label = {
                                        Text(
                                            if (isBangla) "$selectedMethod ওয়ালেট নম্বর (১১ সংখ্যা)"
                                            else "$selectedMethod Wallet Number (11-digits)"
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Phone,
                                            contentDescription = null,
                                            tint = if (phoneValidation.isValid) MonaymGreen else selectedColor
                                        )
                                    },
                                    trailingIcon = {
                                        if (walletNumber.isNotEmpty()) {
                                            if (phoneValidation.isValid) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Valid Number",
                                                    tint = MonaymGreen,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            } else {
                                                Icon(
                                                    imageVector = Icons.Default.ErrorOutline,
                                                    contentDescription = "Invalid Format",
                                                    tint = MaterialTheme.colorScheme.error,
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                        }
                                    },
                                    isError = walletNumber.isNotEmpty() && !phoneValidation.isValid,
                                    modifier = Modifier.fillMaxWidth().testTag("wallet_number_input"),
                                    singleLine = true
                                )

                                // Real-time Helper & Counter
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (walletNumber.isNotEmpty()) {
                                            phoneValidation.message
                                        } else {
                                            if (isBangla) "বাংলাদেশী ফরম্যাট: ০১XXXXXXXXX (১১ ডিজিট)" else "Format: 01XXXXXXXXX (11 digits)"
                                        },
                                        fontSize = 11.sp,
                                        fontWeight = if (phoneValidation.isValid) FontWeight.Bold else FontWeight.Normal,
                                        color = if (walletNumber.isEmpty()) Color.Gray
                                                else if (phoneValidation.isValid) MonaymGreen
                                                else MaterialTheme.colorScheme.error,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Surface(
                                        color = if (phoneValidation.isValid) MonaymGreen.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "${walletNumber.length}/$maxPhoneLen",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (phoneValidation.isValid) MonaymGreen else Color.DarkGray,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }

                            // TrxID Input
                            OutlinedTextField(
                                value = trxId,
                                onValueChange = { trxId = it.uppercase() },
                                placeholder = {
                                    Text(
                                        if (isBangla) "সেন্টমানি ট্রানজেকশন TrxID (যেমন: TRX98274)" else "Send Money TrxID (e.g. TRX98274)",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                },
                                label = { Text(if (isBangla) "লেনদেন আইডি (TrxID) - সেন্টমানি" else "Transaction ID (TrxID)") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Receipt, contentDescription = null, tint = selectedColor)
                                },
                                modifier = Modifier.fillMaxWidth().testTag("dialog_payment_trxid_input"),
                                singleLine = true
                            )

                            // PIN / Security Token Placeholder
                            OutlinedTextField(
                                value = pinCode,
                                onValueChange = { pinCode = it },
                                placeholder = {
                                    Text(
                                        if (isBangla) "৪ বা ৫ সংখ্যার পিন কোড (যেমন: ••••)" else "4-5 digit Secret PIN (e.g. ••••)",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                },
                                label = { Text(if (isBangla) "গোপন পিন / ভেরিফিকেশন কোড" else "Wallet PIN / Token") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = selectedColor)
                                },
                                modifier = Modifier.fillMaxWidth().testTag("payment_pin_input"),
                                singleLine = true
                            )
                        }
                    }

                    // Calculation Summary Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = if (isBangla) "সরকারী নির্ধারিত ফি:" else "Govt. Base Fee:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MonaymPrimaryDark)
                                Text(text = "৳${amountText.ifBlank { "0" }}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MonaymDarkNavy)
                            }
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = if (isBangla) "ই-পেমেন্ট গেটওয়ে চার্জ:" else "E-Gateway Convenience Fee:", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MonaymPrimaryDark)
                                Text(text = "৳০.০০ (ফ্রি)", fontSize = 12.sp, color = MonaymGreen, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(text = if (isBangla) "সর্বমোট প্রদেয়:" else "Total Payable:", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                Text(text = "৳${amountText.ifBlank { "0" }}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = selectedColor)
                            }
                        }
                    }

                    // Pay Button with validation constraint
                    val isPayEnabled = phoneValidation.isValid

                    Button(
                        onClick = {
                            val amt = amountText.toDoubleOrNull() ?: 1250.0
                            val trxTag = if (trxId.isNotBlank()) " [TrxID: $trxId]" else ""
                            val finalPurpose = "$purpose$trxTag"
                            val finalMethod = "$selectedMethod (Send: $adminSendMoneyNumber)"
                            onPay(amt, finalMethod, finalPurpose)
                            onDismiss()
                        },
                        enabled = isPayEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = selectedColor,
                            disabledContainerColor = selectedColor.copy(alpha = 0.4f),
                            disabledContentColor = Color.White.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("confirm_payment_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Payment, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (!phoneValidation.isValid && walletNumber.isNotEmpty()) {
                                if (isBangla) "১১ সংখ্যার সঠিক নম্বর দিন" else "Enter valid 11-digit number"
                            } else {
                                if (isBangla) "$selectedMethod দিয়ে ৳${amountText.ifBlank { "0" }} পরিশোধ করুন"
                                else "Pay ৳${amountText.ifBlank { "0" }} with $selectedMethod"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun OtpLoginModal(
    isBangla: Boolean,
    onDismiss: () -> Unit,
    onLoginSuccess: (phone: String, otp: String) -> Unit
) {
    var phone by remember { mutableStateOf("01700000000") }
    var otp by remember { mutableStateOf("123456") }
    var step by remember { mutableStateOf(1) } // 1: Enter Phone, 2: Enter OTP

    val phoneValidation = remember(phone, isBangla) {
        BangladeshPhoneValidator.validate(phone, isBangla, isRocket = false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isBangla) "স্মার্ট মোবাইল ওটিপি লগইন" else "Mobile OTP Login",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (step == 1) {
                    Text(
                        text = if (isBangla) "M.S MONAYM ENTERPRISE সিস্টেমে প্রবেশের জন্য আপনার ১১ ডিজিটের মোবাইল নম্বর লিখুন"
                        else "Enter your 11-digit mobile number to receive OTP code",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = MonaymDarkNavy
                    )
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { input ->
                            phone = BangladeshPhoneValidator.cleanDigits(input, 11)
                        },
                        label = { Text(if (isBangla) "মোবাইল নম্বর (১১ ডিজিট)" else "Mobile Number (11-digit)") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = null,
                                tint = if (phoneValidation.isValid) MonaymGreen else MonaymPrimary
                            )
                        },
                        trailingIcon = {
                            if (phone.isNotEmpty()) {
                                if (phoneValidation.isValid) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Valid",
                                        tint = MonaymGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.ErrorOutline,
                                        contentDescription = "Invalid",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        },
                        isError = phone.isNotEmpty() && !phoneValidation.isValid,
                        modifier = Modifier.fillMaxWidth().testTag("otp_phone_input"),
                        singleLine = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (phone.isNotEmpty()) phoneValidation.message else "01XXXXXXXXX",
                            fontSize = 11.sp,
                            color = if (phone.isEmpty()) Color.Gray else if (phoneValidation.isValid) MonaymGreen else MaterialTheme.colorScheme.error,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${phone.length}/11",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (phoneValidation.isValid) MonaymGreen else Color.DarkGray
                        )
                    }
                } else {
                    Text(
                        text = if (isBangla) "$phone নম্বরে পাঠানো ৬-সংখ্যার ওটিপি লিখুন"
                        else "Enter 6-digit OTP code sent to $phone",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    OutlinedTextField(
                        value = otp,
                        onValueChange = { otp = it.filter { ch -> ch.isDigit() }.take(6) },
                        label = { Text(if (isBangla) "ওটিপি কোড (OTP Code)" else "OTP Code") },
                        leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("otp_code_input"),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            val isStepValid = if (step == 1) phoneValidation.isValid else otp.length >= 4
            Button(
                onClick = {
                    if (step == 1) {
                        step = 2
                    } else {
                        onLoginSuccess(phone, otp)
                        onDismiss()
                    }
                },
                enabled = isStepValid,
                colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                modifier = Modifier.testTag("otp_submit_btn")
            ) {
                Text(if (step == 1) (if (isBangla) "ওটিপি পাঠান →" else "Send OTP →") else (if (isBangla) "লগইন করুন" else "Verify & Login"))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text(if (isBangla) "বাতিল" else "Cancel")
            }
        }
    )
}

@Composable
fun CoverPosterDialog(
    isBangla: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with close button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MonaymPrimary)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_app_icon_1786066011550),
                                contentDescription = "Logo",
                                modifier = Modifier
                                    .padding(2.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isBangla) "কোম্পানি কভার পোস্টার ও অফিসিয়াল প্রোফাইল" else "Company Cover Poster & Profile",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_cover_poster_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White
                        )
                    }
                }

                // High-resolution Cover Poster Display
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner_1786066025458),
                        contentDescription = "M.S Monaym Enterprise Full Cover Banner",
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Company & Proprietor Details Sheet
                Column(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        color = MonaymGold.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = MonaymPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBangla) "এম. এস. মোনায়েম এন্টারপ্রাইজ" else "M.S MONAYM ENTERPRISE",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MonaymPrimary
                                )
                                Text(
                                    text = if (isBangla) "১ম শ্রেণীর সরকারী ঠিকাদার ও সরবরাহকারী" else "1st Class Govt. Contractor & Supplier",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonaymDarkNavy
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Badge, contentDescription = null, tint = MonaymPrimary, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBangla) "প্রোপাইটর ও সিইও: মোঃ ইসমাইল খান" else "Proprietor & CEO: MD. ISMAIL KHAN",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Call, contentDescription = null, tint = MonaymGreen, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isBangla) "সরাসরি যোগাযোগ (সিম ও WhatsApp):" else "Direct Contact (SIM & WhatsApp):",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MonaymPrimaryDark
                                    )
                                    Text(
                                        text = "01976444504",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp,
                                        color = MonaymPrimaryDark
                                    )
                                }
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.ContactPage, contentDescription = null, tint = MonaymPrimaryDark, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBangla) "ঠিকানা: ঢাকা, বাংলাদেশ (স্মার্ট ল্যান্ড সার্ভিস শাখা)" else "Address: Dhaka, Bangladesh (Smart Land Branch)",
                                    fontSize = 13.sp
                                )
                            }

                            // Direct Contact Action Buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:01976444504"))
                                        context.startActivity(intent)
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = MonaymGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(imageVector = Icons.Default.Call, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = if (isBangla) "সিম কল" else "SIM Call", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }

                                Button(
                                    onClick = {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/8801976444504"))
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Fallback
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(text = "💬 WhatsApp", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (isBangla) "পোস্টার বন্ধ করুন" else "Close Banner")
                    }
                }
            }
        }
    }
}

@Composable
fun KeystoreSigningInfoDialog(
    isBangla: Boolean,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MonaymPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangla) "কীস্টোর ও AAB সাইনিং কি" else "Keystore & AAB Signing Key",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MonaymPrimary
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp).testTag("close_keystore_dialog")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Gray
                        )
                    }
                }

                KeystoreSha256Card(isBangla = isBangla)

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(if (isBangla) "ঠিক আছে (বন্ধ করুন)" else "Close")
                }
            }
        }
    }
}


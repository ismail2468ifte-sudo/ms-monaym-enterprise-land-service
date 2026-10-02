package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.R
import com.example.data.db.PaymentRecord
import com.example.ui.theme.BkashColor
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.theme.NagadColor
import com.example.ui.theme.RocketColor
import com.example.ui.theme.UpayColor
import com.example.ui.viewmodel.LandViewModel
import com.example.util.BangladeshPhoneValidator

data class ServiceFeePreset(
    val titleBn: String,
    val titleEn: String,
    val fee: Double,
    val icon: ImageVector,
    val code: String
)

@Composable
fun PaymentGatewayScreen(
    viewModel: LandViewModel,
    onOpenPaymentDialog: () -> Unit
) {
    val isBangla by viewModel.isBangla.collectAsState()
    val payments by viewModel.allPayments.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()

    var selectedReceiptModal by remember { mutableStateOf<PaymentRecord?>(null) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val adminSendMoneyNumber = "01976444504"
    var isNumberCopied by remember { mutableStateOf(false) }

    // Quick Payment Form States
    var selectedMethod by remember { mutableStateOf("bKash") }
    var selectedFeePreset by remember { mutableStateOf("Khatian") }
    var customAmount by remember { mutableStateOf("100") }
    var purposeText by remember { mutableStateOf("খতিয়ান সার্টিফাইড কপি ফি (Khatian Copy Fee)") }
    var walletNumberInput by remember { mutableStateOf("") }
    var trxIdInput by remember { mutableStateOf("") }
    var pinCodeInput by remember { mutableStateOf("") }
    var referenceInput by remember { mutableStateOf("") }
    var showPaymentSuccessSnackbar by remember { mutableStateOf(false) }

    val feePresets = listOf(
        ServiceFeePreset("খতিয়ান কপি", "Khatian Copy", 100.0, Icons.Default.Description, "Khatian"),
        ServiceFeePreset("নামজারি ফি", "Mutation Fee", 1150.0, Icons.Default.Receipt, "Mutation"),
        ServiceFeePreset("ভূমি কর / খাজনা", "Land Tax", 1250.0, Icons.Default.AccountBalance, "Tax"),
        ServiceFeePreset("মৌজা নকশা", "Mouza Map", 520.0, Icons.Default.Map, "Map")
    )

    val mfsMethods = listOf(
        Triple("bKash", BkashColor, "01XXXXXXXXX"),
        Triple("Nagad", NagadColor, "01XXXXXXXXX"),
        Triple("Rocket", RocketColor, "01XXXXXXXXXX"),
        Triple("Upay", UpayColor, "01XXXXXXXXX")
    )

    val currentMethodColor = when (selectedMethod) {
        "bKash" -> BkashColor
        "Nagad" -> NagadColor
        "Rocket" -> RocketColor
        "Upay" -> UpayColor
        else -> MonaymPrimary
    }

    val currentPlaceholder = when (selectedMethod) {
        "bKash" -> if (isBangla) "বিকাশ ওয়ালেট নম্বর (যেমন: 017XXXXXXXX)" else "bKash Account No (e.g. 017XXXXXXXX)"
        "Nagad" -> if (isBangla) "নগদ ওয়ালেট নম্বর (যেমন: 018XXXXXXXX)" else "Nagad Account No (e.g. 018XXXXXXXX)"
        "Rocket" -> if (isBangla) "রকেট ১২ ডিজিট অ্যাকাউন্ট (যেমন: 019XXXXXXXXX)" else "Rocket 12-digit Account (019XXXXXXXXX)"
        else -> if (isBangla) "উপায় অ্যাকাউন্ট নম্বর (যেমন: 016XXXXXXXX)" else "Upay Account No (016XXXXXXXX)"
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item { Spacer(modifier = Modifier.height(6.dp)) }

        // Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MonaymPrimaryDark)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBangla) "স্মার্ট ভূমি উন্নয়ন কর ও ফি পেমেন্ট" else "Smart Land Tax & Fee Payment",
                            color = Color.White,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isBangla) "বিকাশ, নগদ, রকেটের মাধ্যমে সরকারি ভূমি ফি পরিশোধ"
                            else "Pay Land Service Fees via bKash, Nagad & Rocket",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Quick Service Preset Selector
        item {
            Text(
                text = if (isBangla) "১. সেবার ধরন ও সরকারি ফি নির্বাচন করুন" else "1. Select Land Service & Fee",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                feePresets.forEach { preset ->
                    val isSelected = selectedFeePreset == preset.code
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                selectedFeePreset = preset.code
                                customAmount = preset.fee.toInt().toString()
                                purposeText = if (isBangla) "${preset.titleBn} ফি" else "${preset.titleEn} Fee"
                            }
                            .testTag("fee_preset_${preset.code}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) currentMethodColor.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface
                        ),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, currentMethodColor) else null,
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = preset.icon,
                                contentDescription = null,
                                tint = if (isSelected) currentMethodColor else Color.Gray,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isBangla) preset.titleBn else preset.titleEn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) currentMethodColor else Color.Unspecified
                            )
                            Text(
                                text = "৳${preset.fee.toInt()}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isSelected) currentMethodColor else MonaymGreen
                            )
                        }
                    }
                }
            }
        }

        // Admin Send Money Card (bKash & Nagad)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_send_money_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (selectedMethod == "bKash") BkashColor else if (selectedMethod == "Nagad") NagadColor else MonaymPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = (if (selectedMethod == "bKash") BkashColor else if (selectedMethod == "Nagad") NagadColor else MonaymPrimary).copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = null,
                                        tint = if (selectedMethod == "bKash") BkashColor else if (selectedMethod == "Nagad") NagadColor else MonaymPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBangla) "অ্যাডমিন বিকাশ ও নগদ (Send Money)" else "Admin bKash & Nagad (Send Money)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "M.S MONAYM ENTERPRISE (পার্সোনাল)",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MonaymDarkNavy
                                )
                            }
                        }

                        Surface(
                            color = MonaymGreen.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isBangla) "সেন্টমানি একাউন্ট" else "Send Money",
                                color = MonaymGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Admin Number Display Box with 1-Tap Copy & Dial
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = if (isBangla) "প্রাপক সেন্টমানি নম্বর:" else "Recipient Send Money No:",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MonaymPrimaryDark
                                    )
                                    Text(
                                        text = adminSendMoneyNumber,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (selectedMethod == "bKash") BkashColor else if (selectedMethod == "Nagad") NagadColor else MonaymPrimary,
                                        letterSpacing = 1.sp
                                    )
                                }

                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    // Copy Button
                                    Button(
                                        onClick = {
                                            clipboardManager.setText(AnnotatedString(adminSendMoneyNumber))
                                            isNumberCopied = true
                                            Toast.makeText(
                                                context,
                                                if (isBangla) "অ্যাডমিন নম্বর কপি করা হয়েছে: $adminSendMoneyNumber"
                                                else "Admin number copied: $adminSendMoneyNumber",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isNumberCopied) MonaymGreen else MaterialTheme.colorScheme.primary
                                        ),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("copy_admin_phone_btn")
                                    ) {
                                        Icon(
                                            imageVector = if (isNumberCopied) Icons.Default.Check else Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = if (isNumberCopied) (if (isBangla) "কপি হয়েছে" else "Copied") else (if (isBangla) "কপি করুন" else "Copy"),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Dial Button
                                    OutlinedButton(
                                        onClick = {
                                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$adminSendMoneyNumber"))
                                            context.startActivity(intent)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("dial_admin_phone_btn")
                                    ) {
                                        Icon(imageVector = Icons.Default.Call, contentDescription = "Dial", modifier = Modifier.size(16.dp))
                                    }

                                    // WhatsApp Button
                                    Button(
                                        onClick = {
                                            val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/88$adminSendMoneyNumber"))
                                            try {
                                                context.startActivity(waIntent)
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "WhatsApp $adminSendMoneyNumber", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.testTag("whatsapp_admin_phone_btn")
                                    ) {
                                        Text(text = "💬 WhatsApp", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                            // Step-by-step guide
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        text = if (isBangla) "📌 সেন্টমানি (Send Money) করার নিয়ম:" else "📌 Send Money Instructions:",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (isBangla) "১. বিকাশ বা নগদ অ্যাপে গিয়ে 'Send Money' সিলেক্ট করুন।"
                                        else "1. Open bKash/Nagad app and choose 'Send Money'.",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MonaymDarkNavy
                                    )
                                    Text(
                                        text = if (isBangla) "২. প্রাপক নম্বরে লিখুন: $adminSendMoneyNumber"
                                        else "2. Enter Recipient Number: $adminSendMoneyNumber",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MonaymPrimaryDark
                                    )
                                    Text(
                                        text = if (isBangla) "৩. টাকার পরিমাণ লিখে রেফারেন্সে আপনার খতিয়ান নং দিয়ে সেন্ড করুন।"
                                        else "3. Enter amount & reference (Khatian No), then confirm with PIN.",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MonaymDarkNavy
                                    )
                                    Text(
                                        text = if (isBangla) "৪. লেনদেন শেষে প্রাপ্ত TrxID ও আপনার প্রেরক নম্বর নিচে দিয়ে নিশ্চিত করুন।"
                                        else "4. Enter TrxID and your sender mobile number below to complete.",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MonaymDarkNavy
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. Simplified Payment Form
        item {
            Text(
                text = if (isBangla) "২. পেমেন্ট মাধ্যম ও ওয়ালেট তথ্য" else "2. Payment Method & Wallet Info",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("payment_form_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // MFS Providers Pills
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
                                    .testTag("select_method_$name")
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = name,
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    // Active MFS Provider Info & Placeholders
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = currentMethodColor.copy(alpha = 0.08f)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = currentMethodColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBangla)
                                    "$selectedMethod সিকিউর পেমেন্ট গেটওয়েতে প্রবেশ করেছেন"
                                else
                                    "Connected to $selectedMethod Secure Gateway",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = currentMethodColor
                            )
                        }
                    }

                    // Wallet Phone Number Validation Logic
                    val isRocket = selectedMethod == "Rocket"
                    val maxPhoneLen = if (isRocket) 12 else 11
                    val phoneValidation = remember(walletNumberInput, isBangla, isRocket) {
                        BangladeshPhoneValidator.validate(walletNumberInput, isBangla, isRocket)
                    }

                    // Wallet Phone Number Input with Real-time Validation
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        OutlinedTextField(
                            value = walletNumberInput,
                            onValueChange = { input ->
                                walletNumberInput = BangladeshPhoneValidator.cleanDigits(input, maxPhoneLen)
                            },
                            placeholder = { Text(currentPlaceholder, fontSize = 12.sp, color = Color.Gray) },
                            label = {
                                Text(
                                    if (isBangla) "$selectedMethod ওয়ালেট নম্বর (১১ ডিজিট)"
                                    else "$selectedMethod Account No (11 Digits)"
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    tint = if (phoneValidation.isValid) MonaymGreen else currentMethodColor
                                )
                            },
                            trailingIcon = {
                                if (walletNumberInput.isNotEmpty()) {
                                    if (phoneValidation.isValid) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Valid Bangladesh Number",
                                            tint = MonaymGreen,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    } else {
                                        Icon(
                                            imageVector = Icons.Default.ErrorOutline,
                                            contentDescription = "Invalid Number Format",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                            },
                            isError = walletNumberInput.isNotEmpty() && !phoneValidation.isValid,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("mfs_wallet_input"),
                            singleLine = true
                        )

                        // Real-time Feedback & Counter Row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (walletNumberInput.isNotEmpty()) {
                                    phoneValidation.message
                                } else {
                                    if (isBangla) "বাংলাদেশী ফরম্যাট: ০১XXXXXXXXX (১১ ডিজিট)" else "BD Format: 01XXXXXXXXX (11 digits)"
                                },
                                fontSize = 11.sp,
                                fontWeight = if (phoneValidation.isValid) FontWeight.Bold else FontWeight.Normal,
                                color = if (walletNumberInput.isEmpty()) Color.Gray
                                        else if (phoneValidation.isValid) MonaymGreen
                                        else MaterialTheme.colorScheme.error,
                                modifier = Modifier.weight(1f)
                            )

                            Surface(
                                color = if (phoneValidation.isValid) MonaymGreen.copy(alpha = 0.15f) else Color.Gray.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "${walletNumberInput.length}/$maxPhoneLen",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (phoneValidation.isValid) MonaymGreen else Color.DarkGray,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // TrxID Input (for Send Money tracking)
                    OutlinedTextField(
                        value = trxIdInput,
                        onValueChange = { trxIdInput = it.uppercase() },
                        placeholder = {
                            Text(
                                if (isBangla) "বিকাশ/নগদ সেন্টমানি TrxID (যেমন: TRX9X8Y7Z2)" else "bKash/Nagad Send Money TrxID (e.g. TRX9X8Y7Z2)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        },
                        label = { Text(if (isBangla) "লেনদেন আইডি (TrxID) - সেন্টমানি" else "Transaction ID (TrxID)") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Receipt, contentDescription = null, tint = currentMethodColor)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mfs_trxid_input"),
                        singleLine = true
                    )

                    // Reference / Khatian No Input with Placeholder
                    OutlinedTextField(
                        value = referenceInput,
                        onValueChange = { referenceInput = it },
                        placeholder = {
                            Text(
                                if (isBangla) "খতিয়ান বা আবেদন রেফারেন্স নং (যেমন: ১০৫৪/৮)" else "Khatian or Ref No (e.g. 1054/8)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        },
                        label = { Text(if (isBangla) "রেফারেন্স / খতিয়ান নম্বর" else "Reference / Khatian No") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mfs_reference_input"),
                        singleLine = true
                    )

                    // PIN Code with Placeholder
                    OutlinedTextField(
                        value = pinCodeInput,
                        onValueChange = { pinCodeInput = it },
                        placeholder = {
                            Text(
                                if (isBangla) "৪ বা ৫ সংখ্যার পিন কোড (যেমন: ••••)" else "4-5 digit PIN (e.g. ••••)",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        },
                        label = { Text(if (isBangla) "গোপন পিন কোড (PIN)" else "Secret PIN") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Lock, contentDescription = null, tint = currentMethodColor)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("mfs_pin_input"),
                        singleLine = true
                    )

                    // Payable Summary
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isBangla) "সেবার বিবরণ:" else "Purpose:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MonaymPrimaryDark
                                )
                                Text(
                                    text = purposeText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonaymDarkNavy
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isBangla) "পেমেন্ট মাধ্যম:" else "Method:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MonaymPrimaryDark
                                )
                                Text(
                                    text = "$selectedMethod (Send Money: 01976444504)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = currentMethodColor
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (isBangla) "মোট প্রদেয় ফি:" else "Total Fee:",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "৳$customAmount",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = currentMethodColor
                                )
                            }
                        }
                    }

                    // Confirm Payment Button (with real-time validation check)
                    val isPaymentEnabled = phoneValidation.isValid

                    Button(
                        onClick = {
                            val amt = customAmount.toDoubleOrNull() ?: 100.0
                            val trxTag = if (trxIdInput.isNotBlank()) " [TrxID: $trxIdInput]" else ""
                            val refTag = if (referenceInput.isNotBlank()) " (খতিয়ান/রেফ: $referenceInput)" else ""
                            val finalPurpose = "$purposeText$refTag$trxTag"
                            viewModel.processPayment(amt, "$selectedMethod (Send: $adminSendMoneyNumber)", finalPurpose)
                            walletNumberInput = ""
                            trxIdInput = ""
                            pinCodeInput = ""
                            referenceInput = ""
                            showPaymentSuccessSnackbar = true
                        },
                        enabled = isPaymentEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = currentMethodColor,
                            disabledContainerColor = currentMethodColor.copy(alpha = 0.4f),
                            disabledContentColor = Color.White.copy(alpha = 0.8f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_mfs_payment_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Payment, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (!phoneValidation.isValid && walletNumberInput.isNotEmpty()) {
                                if (isBangla) "১১ ডিজিটের সঠিক নম্বর দিন" else "Enter valid 11-digit mobile"
                            } else {
                                if (isBangla) "$selectedMethod দিয়ে ৳$customAmount পরিশোধ করুন"
                                else "Pay ৳$customAmount with $selectedMethod"
                            },
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Open Custom Dialog Option
                    OutlinedButton(
                        onClick = onOpenPaymentDialog,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("open_custom_fee_dialog_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.AddCard, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (isBangla) "অন্যান্য কাস্টম সেবা ফি প্রদান" else "Custom Service Fee Dialog")
                    }
                }
            }
        }

        // 3. Payment Receipts & History Section
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangla) "৩. ই-পেমেন্ট রসিদ ও লেনদেন ইতিহাস" else "3. E-Receipts & Payment History",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${payments.size} ${if (isBangla) "টি রসিদ" else "Receipts"}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        if (payments.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isBangla) "কোনো পেমেন্ট রেকর্ড পাওয়া যায়নি। উপরের ফর্ম থেকে ফি পরিশোধ করুন।"
                            else "No payment history found. Pay fees using the form above.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else {
            items(payments) { payment ->
                val badgeColor = when (payment.method) {
                    "bKash" -> BkashColor
                    "Nagad" -> NagadColor
                    "Rocket" -> RocketColor
                    "Upay" -> UpayColor
                    else -> MonaymGreen
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedReceiptModal = payment }
                        .testTag("payment_receipt_${payment.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(badgeColor.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = badgeColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = payment.purpose,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "TRX ID: ${payment.transactionId} • ${payment.method}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Text(
                                text = "৳${payment.amount.toInt()}",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MonaymGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${payment.customerName} (${payment.customerMobile})",
                                fontSize = 11.sp,
                                color = Color.DarkGray
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    color = badgeColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = payment.method,
                                        color = badgeColor,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    color = MonaymGreen.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = payment.status,
                                        color = MonaymGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }
    }

    // Receipt Detail Modal
    selectedReceiptModal?.let { item ->
        val methodColor = when (item.method) {
            "bKash" -> BkashColor
            "Nagad" -> NagadColor
            "Rocket" -> RocketColor
            "Upay" -> UpayColor
            else -> MonaymPrimary
        }

        androidx.compose.material3.AlertDialog(
            onDismissRequest = { selectedReceiptModal = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Receipt, contentDescription = null, tint = methodColor)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = if (isBangla) "অফিসিয়াল ই-পেমেন্ট রসিদ" else "Official E-Payment Receipt", fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = methodColor.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "M.S MONAYM ENTERPRISE", fontWeight = FontWeight.Bold, color = MonaymPrimaryDark, fontSize = 14.sp)
                                Surface(color = MonaymGreen.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                    Text(text = "PAID ✓", color = MonaymGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(4.dp))
                                }
                            }
                            Text(text = "স্মার্ট ডিজিটাল ভূমি সেবা ই-রসিদ", fontSize = 11.5.sp, fontWeight = FontWeight.SemiBold, color = MonaymPrimaryDark)
                            Spacer(modifier = Modifier.height(4.dp))
                            Divider()
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = "সেবার নাম: ${item.purpose}", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text(text = "পরিশোধিত অর্থ: ৳${item.amount.toInt()}", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MonaymGreen)
                            Text(text = "ট্রানজেকশন ID: ${item.transactionId}", fontSize = 12.sp)
                            Text(text = "পেমেন্ট মেথড: ${item.method}", fontSize = 12.sp, color = methodColor, fontWeight = FontWeight.Bold)
                            Text(text = "গ্রাহকের নাম: ${item.customerName}", fontSize = 12.sp)
                            Text(text = "মোবাইল নম্বর: ${item.customerMobile}", fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedReceiptModal = null },
                    colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary)
                ) {
                    Text(if (isBangla) "রসিদ ডাউনলোড / প্রিন্ট" else "Download Receipt")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedReceiptModal = null }) {
                    Text(if (isBangla) "বন্ধ করুন" else "Close")
                }
            }
        )
    }
}


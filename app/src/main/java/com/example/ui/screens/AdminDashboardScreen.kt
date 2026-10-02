package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import com.example.R
import com.example.ui.components.KeystoreSha256Card
import com.example.ui.theme.BkashColor
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.theme.NagadColor
import com.example.ui.viewmodel.LandViewModel

@Composable
fun AdminDashboardScreen(viewModel: LandViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val landRecords by viewModel.allLandRecords.collectAsState()
    val payments by viewModel.allPayments.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val adminSendMoneyNumber = "01976444504"

    val totalRevenue = payments.sumOf { it.amount }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Admin Header Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_banner_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MonaymPrimaryDark)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier.size(52.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1786066011550),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(3.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isBangla) "অ্যাডমিন কন্ট্রোল প্যানেল" else "Admin Control Panel",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "M.S MONAYM ENTERPRISE",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        color = MonaymGold,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = userRole,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Toggle Role Button
        item {
            OutlinedButton(
                onClick = { viewModel.toggleRole() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("toggle_role_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBangla) "ভিউ পরিবর্তন করুন (Switch to ${if (userRole == "Admin") "User" else "Admin"})"
                    else "Switch Role (Current: $userRole)",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Key Server Metrics
        item {
            Text(
                text = if (isBangla) "সার্ভার ও ডাটাবেজ সামারি (Metrics)" else "System Metrics",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymPrimary.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(imageVector = Icons.Default.Group, contentDescription = null, tint = MonaymPrimary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "১,৪৫০+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MonaymPrimary)
                        Text(text = if (isBangla) "মোট ইউজার" else "Total Users", fontSize = 12.sp)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymGreen.copy(alpha = 0.08f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(imageVector = Icons.Default.MonetizationOn, contentDescription = null, tint = MonaymGreen)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "৳${totalRevenue.toInt()}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MonaymGreen)
                        Text(text = if (isBangla) "মোট রাজস্ব" else "Total Revenue", fontSize = 12.sp)
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymGold.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Icon(imageVector = Icons.Default.ListAlt, contentDescription = null, tint = Color(0xFFD97706))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "${landRecords.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                        Text(text = if (isBangla) "খতিয়ান এন্ট্রি" else "Land Records", fontSize = 12.sp)
                    }
                }
            }
        }

        // Admin MFS Send Money Receiving Account Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("admin_mfs_receiving_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                            Icon(imageVector = Icons.Default.AccountBalanceWallet, contentDescription = null, tint = BkashColor)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBangla) "অ্যাডমিন পেমেন্ট রিসিভিং অ্যাকাউন্ট" else "Admin MFS Receiving Account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Surface(
                            color = MonaymGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "VERIFIED",
                                color = MonaymGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = if (isBangla) "বিকাশ ও নগদ সেন্টমানি নম্বর:" else "bKash & Nagad Send Money No:",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = adminSendMoneyNumber,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = BkashColor
                                )
                                Text(
                                    text = "M.S MONAYM ENTERPRISE (পার্সোনাল ওয়ালেট)",
                                    fontSize = 10.5.sp,
                                    color = Color.DarkGray
                                )
                            }

                            Button(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(adminSendMoneyNumber))
                                    Toast.makeText(
                                        context,
                                        if (isBangla) "নম্বর কপি হয়েছে: $adminSendMoneyNumber" else "Copied: $adminSendMoneyNumber",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BkashColor),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = if (isBangla) "কপি" else "Copy", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Hardware & Device Diagnostics (vivoY2111 / Baseband / Android Serial)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, tint = MonaymGreen)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBangla) "ডিভাইস ও সিস্টেম ডায়াগনস্টিকস" else "Device & System Diagnostics",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }

                        Surface(
                            color = MonaymGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "AUTH OK",
                                color = MonaymGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(text = "Module: ${LandViewModel.DEVICE_MODEL}", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Baseband: ${LandViewModel.BASEBAND_VERSION}", fontSize = 10.5.sp, color = Color.DarkGray)
                        Text(text = "Serial / Activity: ${LandViewModel.DEVICE_SERIAL}", fontSize = 11.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.SemiBold)
                        Text(text = "Admin Account: ${LandViewModel.ADMIN_ACCOUNT}", fontSize = 11.sp, color = BkashColor, fontWeight = FontWeight.SemiBold)
                        Text(text = "Hotline & WhatsApp: ${LandViewModel.ENTERPRISE_PHONE}", fontSize = 11.sp, color = MonaymGreen, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Keystore & AAB Signing Certificate (SHA-256)
        item {
            KeystoreSha256Card(isBangla = isBangla)
        }

        // Server Status & Database Backup Actions
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Dns, contentDescription = null, tint = MonaymPrimary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "REST API Server Status", fontWeight = FontWeight.Bold)
                        }

                        Surface(
                            color = MonaymGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "ACTIVE (Port 5000)",
                                color = MonaymGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "Database: MySQL (ms_monaym)", fontSize = 12.sp, color = Color.Gray)
                    Text(text = "JWT Security: MS_MONAYM_SECRET_KEY_2026", fontSize = 12.sp, color = Color.Gray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Backup, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isBangla) "ডাটাবেজ ব্যাকআপ" else "Backup SQL", fontSize = 12.sp)
                        }

                        Button(
                            onClick = { },
                            colors = ButtonDefaults.buttonColors(containerColor = MonaymGreen),
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Security, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isBangla) "নিরাপত্তা চেক" else "Security Check", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

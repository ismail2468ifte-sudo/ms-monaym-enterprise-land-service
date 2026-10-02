package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.R
import com.example.ui.components.CoverPosterDialog
import com.example.ui.components.KeystoreSha256Card
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.ui.platform.LocalContext
import com.example.ui.theme.BkashColor
import com.example.ui.theme.LightBackground
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.viewmodel.LandViewModel

@Composable
fun DashboardScreen(
    viewModel: LandViewModel,
    onNavigateToTab: (Int) -> Unit,
    onOpenAddLandDialog: () -> Unit,
    onOpenPaymentDialog: () -> Unit,
    onOpenUploadDialog: () -> Unit
) {
    val context = LocalContext.current
    val isBangla by viewModel.isBangla.collectAsState()
    val landRecords by viewModel.allLandRecords.collectAsState()
    val documents by viewModel.allDocuments.collectAsState()
    val payments by viewModel.allPayments.collectAsState()
    val notifications by viewModel.allNotifications.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showCoverPosterDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }

        // Hero Banner Card with Logo Overlay & Cover Poster View Trigger
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showCoverPosterDialog = true }
                    .testTag("hero_banner_card"),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_hero_banner_1786066025458),
                        contentDescription = "M.S Monaym Enterprise Cover Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.matchParentSize()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.88f)
                                    )
                                )
                            )
                    )

                    // Top Left Logo Overlay
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_app_icon_1786066011550),
                                contentDescription = "Land Service Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(2.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MonaymPrimary,
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = if (isBangla) "ভূমি সেবা (LAND SERVICE)" else "LAND SERVICE",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Bottom Information Overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(14.dp)
                    ) {
                        Surface(
                            color = MonaymGold,
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = if (isBangla) "১ম শ্রেণীর সরকারী ঠিকাদার ও স্মার্ট ল্যান্ড সার্ভিস" else "1ST CLASS GOVT. CONTRACTOR & SMART LAND SERVICE",
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isBangla) "এম. এস. মোনায়েম এন্টারপ্রাইজ" else "M.S MONAYM ENTERPRISE",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isBangla) "খতিয়ান, দাগ, নামজারি, নকশা ও খাজনা সেবা"
                                else "Khatian, Dag, Mutation, Maps & Tax Services",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 12.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                color = Color.White.copy(alpha = 0.25f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Text(
                                    text = if (isBangla) "🔍 ফুল কভার দেখুন" else "🔍 Full Cover",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Enterprise Profile Card (Clarifying Company Info)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("enterprise_profile_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MonaymPrimary.copy(alpha = 0.06f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            shadowElevation = 3.dp,
                            modifier = Modifier.size(52.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_app_icon_1786066011550),
                                contentDescription = "Enterprise Logo",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(3.dp)
                                    .clip(CircleShape)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isBangla) "এম. এস. মোনায়েম এন্টারপ্রাইজ" else "M.S MONAYM ENTERPRISE",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MonaymPrimary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Verified",
                                    tint = MonaymGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = if (isBangla) "প্রোপাইটর ও সিইও: মোঃ ইসমাইল খান (১ম শ্রেণীর সরকারী ঠিকাদার)"
                                else "Proprietor & CEO: MD. ISMAIL KHAN (1st Class Govt. Contractor)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.DarkGray
                            )
                            Text(
                                text = if (isBangla) "ডিজিটাল খতিয়ান, জিপিএস ম্যাপ ও অনলাইন ভূমি উন্নয়ন কর প্ল্যাটফর্ম"
                                else "Digital Khatian, GPS Map & Online Land Development Tax Platform",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Direct Contact Hotline & WhatsApp Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        shadowElevation = 1.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MonaymGreen.copy(alpha = 0.15f),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Call,
                                                contentDescription = null,
                                                tint = MonaymGreen,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isBangla) "সরাসরি যোগাযোগ (সিম ও WhatsApp)" else "Direct Contact (SIM & WhatsApp)",
                                            fontSize = 10.5.sp,
                                            color = Color.DarkGray,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = LandViewModel.ENTERPRISE_PHONE,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MonaymPrimaryDark,
                                            letterSpacing = 0.5.sp
                                        )
                                    }
                                }

                                // Copy Button
                                Surface(
                                    onClick = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val clip = ClipData.newPlainText("Enterprise Phone", LandViewModel.ENTERPRISE_PHONE)
                                        clipboard.setPrimaryClip(clip)
                                        Toast.makeText(
                                            context,
                                            if (isBangla) "যোগাযোগ নম্বর কপি করা হয়েছে: ${LandViewModel.ENTERPRISE_PHONE}"
                                            else "Contact copied: ${LandViewModel.ENTERPRISE_PHONE}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    color = LightBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy",
                                            tint = MonaymPrimary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = if (isBangla) "কপি" else "Copy",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MonaymPrimary
                                        )
                                    }
                                }
                            }

                            // Quick Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                // SIM Call
                                Button(
                                    onClick = {
                                        val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${LandViewModel.ENTERPRISE_PHONE}"))
                                        context.startActivity(dialIntent)
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MonaymGreen),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Call,
                                        contentDescription = "Call",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isBangla) "সিম কল" else "SIM Call",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // WhatsApp Chat
                                Button(
                                    onClick = {
                                        val waIntent = Intent(Intent.ACTION_VIEW, Uri.parse(LandViewModel.ENTERPRISE_WHATSAPP_LINK))
                                        try {
                                            context.startActivity(waIntent)
                                        } catch (e: Exception) {
                                            Toast.makeText(
                                                context,
                                                if (isBangla) "WhatsApp খুলতে পারেনি। নম্বর: ${LandViewModel.ENTERPRISE_WHATSAPP}"
                                                else "Could not open WhatsApp. Number: ${LandViewModel.ENTERPRISE_WHATSAPP}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1f).height(38.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "💬 WhatsApp",
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Keystore SHA-256 Signing Fingerprint Box (Easy Copy for AAB / Play Store)
        item {
            KeystoreSha256Card(isBangla = isBangla)
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        if (isBangla) "খতিয়ান বা দাগ নম্বর লিখুন (যেমন: ১০৫৪, ২৩৪৫)..."
                        else "Search by Khatian or Dag No (e.g. 1054)..."
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MonaymPrimary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dashboard_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MonaymPrimary,
                    unfocusedBorderColor = Color.LightGray
                ),
                singleLine = true
            )
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    title = if (isBangla) "মোট জমি রেকর্ড" else "Land Records",
                    count = "${landRecords.size}",
                    subtitle = if (isBangla) "খতিয়ান রেজিস্ট্রি" else "Khatian Listed",
                    icon = Icons.Default.Assignment,
                    color = MonaymPrimary,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = if (isBangla) "সংরক্ষিত নথি" else "Documents",
                    count = "${documents.size}",
                    subtitle = if (isBangla) "স্ক্যানকৃত ডিজিটাল কপি" else "Digital Vault",
                    icon = Icons.Default.Description,
                    color = MonaymPrimaryDark,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = if (isBangla) "পরিশোধ রসিদ" else "Payments",
                    count = "${payments.size}",
                    subtitle = if (isBangla) "ভূমি উন্নয়ন কর" else "Tax Receipts",
                    icon = Icons.Default.Payment,
                    color = MonaymGreen,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Quick Actions Grid
        item {
            Text(
                text = if (isBangla) "দ্রুত সেবা ও কার্যক্রম (Quick Services)" else "Quick Services",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionButton(
                        title = if (isBangla) "নতুন জমি যোগ" else "Add Land",
                        icon = Icons.Default.Add,
                        backgroundColor = MonaymPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenAddLandDialog
                    )
                    QuickActionButton(
                        title = if (isBangla) "নথি আপলোড" else "Upload Doc",
                        icon = Icons.Default.Description,
                        backgroundColor = MonaymPrimaryDark,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenUploadDialog
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    QuickActionButton(
                        title = if (isBangla) "খাজনা প্রদান" else "Pay Land Tax",
                        icon = Icons.Default.Payment,
                        backgroundColor = MonaymGreen,
                        modifier = Modifier.weight(1f),
                        onClick = onOpenPaymentDialog
                    )
                    QuickActionButton(
                        title = if (isBangla) "ওয়েবভিউ UI মোড" else "WebView UI Mode",
                        icon = Icons.Default.Public,
                        backgroundColor = MonaymPrimary,
                        modifier = Modifier.weight(1f),
                        onClick = { onNavigateToTab(7) }
                    )
                }
                // Deed Registration Calculator Featured Card
                Card(
                    onClick = { onNavigateToTab(8) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("dashboard_reg_fee_calculator_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymPrimaryDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MonaymGold.copy(alpha = 0.2f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Calculate,
                                        contentDescription = null,
                                        tint = MonaymGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (isBangla) "দলিল রেজিস্ট্রি ফি ক্যালকুলেটর" else "Deed Registration Calculator",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MonaymGreen
                                    ) {
                                        Text(
                                            text = if (isBangla) "হালনাগাদ" else "UPDATED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = if (isBangla) "সাফ-কবলা, হেবা ও বণ্টননামা ফি এবং প্রয়োজনীয় কাগজপত্র" else "Saf-Kabla, Heba & Partition Deed Fee Breakdown",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.82f)
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MonaymGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Live Real-Time Web Portals Section (land.gov.bd, eporcha.tech, myactivity.google.com)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("live_web_portals_section_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MonaymPrimary.copy(alpha = 0.12f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Language,
                                        contentDescription = null,
                                        tint = MonaymPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isBangla) "লাইভ পোর্টাল ও রিয়েলটাইম সেবা" else "Live Portals & Realtime Services",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isBangla) "সরকারি ওয়েবসাইট ও একাউন্ট সমন্বয়" else "Govt Web Portals & Account Sync",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Surface(
                            color = MonaymGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "LIVE 24/7",
                                color = MonaymGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // 1. Ministry of Land (land.gov.bd)
                    PortalItemCard(
                        title = if (isBangla) "ভূমি মন্ত্রণালয় (Ministry of Land)" else "Ministry of Land",
                        urlLabel = "www.land.gov.bd",
                        description = if (isBangla) "অনলাইন ভূমি উন্নয়ন কর, নামজারি, খতিয়ান ও নাগরিক সেবা পোর্টাল।" else "Govt. Land Development Tax, Mutation & Citizen Portal.",
                        badgeText = "land.gov.bd",
                        badgeColor = MonaymGreen,
                        onOpenLive = {
                            viewModel.setActiveWebUrl(LandViewModel.URL_LAND_GOV)
                            onNavigateToTab(7)
                        },
                        onOpenExternal = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LandViewModel.URL_LAND_GOV))
                            context.startActivity(intent)
                        }
                    )

                    // 2. E-Porcha Tech (eporcha.tech)
                    PortalItemCard(
                        title = if (isBangla) "ই-পর্চা অনলাইন (E-Porcha Portal)" else "E-Porcha Portal",
                        urlLabel = "eporcha.tech",
                        description = if (isBangla) "ডিজিটাল খতিয়ান অনুসন্ধান, মৌজা ম্যাপ ডাউনলোড ও সার্টিফাইড পর্চা কপি।" else "Digital Khatian Search, Mouza Map & Certified Copies.",
                        badgeText = "eporcha.tech",
                        badgeColor = MonaymGold,
                        onOpenLive = {
                            viewModel.setActiveWebUrl(LandViewModel.URL_EPORCHA)
                            onNavigateToTab(7)
                        },
                        onOpenExternal = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LandViewModel.URL_EPORCHA))
                            context.startActivity(intent)
                        }
                    )

                    // 3. Google Activity (myactivity.google.com)
                    PortalItemCard(
                        title = if (isBangla) "গুগল মাই অ্যাক্টিভিটি (Google My Activity)" else "Google My Activity",
                        urlLabel = "myactivity.google.com",
                        description = if (isBangla) "গুগল অ্যাকাউন্ট নিরাপত্তা, ব্যবহারের ইতিহাস, সার্চ অ্যাক্টিভিটি ও নিয়ন্ত্রণ।" else "Google account security, search history & data control.",
                        badgeText = "Google Activity",
                        badgeColor = BkashColor,
                        onOpenLive = {
                            viewModel.setActiveWebUrl(LandViewModel.URL_GOOGLE_ACTIVITY)
                            onNavigateToTab(7)
                        },
                        onOpenExternal = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LandViewModel.URL_GOOGLE_ACTIVITY))
                            context.startActivity(intent)
                        }
                    )

                    // 4. Google Support (support.google.com)
                    PortalItemCard(
                        title = if (isBangla) "গুগল সাপোর্ট ও হেল্প (Google Support)" else "Google Support & Help",
                        urlLabel = "support.google.com",
                        description = if (isBangla) "অফিসিয়াল গুগল অ্যাকাউন্ট সহায়তা, সিকিউরিটি ভেরিফিকেশন ও সমাধান।" else "Official Google account assistance, security & troubleshooting.",
                        badgeText = "support.google.com",
                        badgeColor = MonaymPrimaryDark,
                        onOpenLive = {
                            viewModel.setActiveWebUrl(LandViewModel.URL_GOOGLE_SUPPORT)
                            onNavigateToTab(7)
                        },
                        onOpenExternal = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(LandViewModel.URL_GOOGLE_SUPPORT))
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }

        // Hardware & Device Activity Diagnostics Section (vivoY2111 / Baseband / Serial)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("device_diagnostics_card"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
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
                                color = MonaymGreen.copy(alpha = 0.12f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.VerifiedUser,
                                        contentDescription = null,
                                        tint = MonaymGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = if (isBangla) "ডিভাইস ও সিস্টেম ডায়াগনস্টিকস" else "Device & System Diagnostics",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isBangla) "সুরক্ষিত হার্ডওয়্যার ও অ্যাক্টিভিটি মডিউল" else "Secure Hardware & Activity Module",
                                    fontSize = 10.5.sp,
                                    color = Color.Gray
                                )
                            }
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
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    // Diagnostic Details List
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                RoundedCornerShape(10.dp)
                            )
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Device / Module:", fontSize = 11.5.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.SemiBold)
                            Text(text = LandViewModel.DEVICE_MODEL, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = MonaymDarkNavy)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Baseband Version:", fontSize = 11.5.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.SemiBold)
                            Text(text = LandViewModel.BASEBAND_VERSION, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MonaymDarkNavy)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Android Activity Serial:", fontSize = 11.5.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.SemiBold)
                            Text(text = LandViewModel.DEVICE_SERIAL, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MonaymGreen)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Enterprise Account:", fontSize = 11.5.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.SemiBold)
                            Text(text = LandViewModel.ENTERPRISE_NAME, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MonaymGold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Google Verified Admin:", fontSize = 11.5.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.SemiBold)
                            Text(text = LandViewModel.ADMIN_ACCOUNT, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = BkashColor)
                        }
                    }

                    // Quick Action button for Google Support
                    Button(
                        onClick = {
                            viewModel.setActiveWebUrl(LandViewModel.URL_GOOGLE_SUPPORT)
                            onNavigateToTab(7)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimaryDark)
                    ) {
                        Icon(imageVector = Icons.Default.Public, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isBangla) "গুগল সাপোর্ট ও অ্যাকাউন্ট ভেরিফিকেশন খুলুন" else "Open Google Support & Verification",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Land Records Header & Recent List
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangla) "সর্বশেষ জমি রেকর্ড (Land Records)" else "Recent Land Records",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isBangla) "সবগুলো দেখুন →" else "View All →",
                    color = MonaymPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .clickable { onNavigateToTab(1) }
                        .padding(4.dp)
                )
            }
        }

        items(landRecords.take(3)) { land ->
            LandRecordItemCard(
                land = land,
                isBangla = isBangla,
                onClick = {
                    viewModel.selectLandRecord(land)
                    onNavigateToTab(3) // Jump to map view for this land
                }
            )
        }

        // Recent Notifications section
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangla) "সাম্প্রতিক নোটিফিকেশন" else "Recent Notifications",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = null,
                    tint = MonaymPrimary
                )
            }
        }

        items(notifications.take(2)) { notif ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(MonaymPrimary.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MonaymPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = notif.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = notif.message,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }

    if (showCoverPosterDialog) {
        CoverPosterDialog(
            isBangla = isBangla,
            onDismiss = { showCoverPosterDialog = false }
        )
    }
}

@Composable
fun StatCard(
    title: String,
    count: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.08f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = count, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
            Text(text = title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MonaymDarkNavy)
            Text(text = subtitle, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MonaymPrimaryDark)
        }
    }
}

@Composable
fun QuickActionButton(
    title: String,
    icon: ImageVector,
    backgroundColor: Color,
    contentColor: Color = Color.White,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor, contentColor = contentColor)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}

@Composable
fun LandRecordItemCard(
    land: com.example.data.db.LandRecord,
    isBangla: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("land_record_item_${land.id}"),
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
                            .size(36.dp)
                            .background(MonaymPrimary.copy(alpha = 0.12f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = MonaymPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "${if (isBangla) "খতিয়ান নং" else "Khatian No"}: ${land.khatianNo}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "${if (isBangla) "দাগ নং" else "Dag No"}: ${land.dagNo}",
                            fontSize = 13.sp,
                            color = MonaymPrimaryDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Surface(
                    color = if (land.status.contains("Verified") || land.status.contains("যাচাইকৃত")) MonaymGreen.copy(alpha = 0.15f) else MonaymGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = land.status,
                        color = if (land.status.contains("Verified") || land.status.contains("যাচাইকৃত")) MonaymGreen else Color(0xFFD97706),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${if (isBangla) "মৌজা" else "Mouza"}: ${land.mouza}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MonaymPrimaryDark
                )
                Text(
                    text = "${if (isBangla) "জেলা" else "District"}: ${land.district}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MonaymPrimaryDark
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${if (isBangla) "মালিক" else "Owner"}: ${land.ownerName}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${if (isBangla) "পরিমাণ" else "Area"}: ${land.area}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MonaymPrimary
                )
            }
        }
    }
}

@Composable
fun PortalItemCard(
    title: String,
    urlLabel: String,
    description: String,
    badgeText: String,
    badgeColor: Color,
    onOpenLive: () -> Unit,
    onOpenExternal: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onOpenLive() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor.copy(alpha = 0.3f))
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.5.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = urlLabel,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = badgeColor
                    )
                }

                Surface(
                    color = badgeColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = badgeText,
                        color = badgeColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = description,
                fontSize = 11.sp,
                color = Color.DarkGray,
                lineHeight = 15.sp
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenLive,
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = badgeColor)
                ) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "অ্যাপে লাইভ দেখুন", fontSize = 11.5.sp, fontWeight = FontWeight.Bold)
                }

                Surface(
                    onClick = onOpenExternal,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, badgeColor.copy(alpha = 0.5f)),
                    color = Color.Transparent,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Launch,
                            contentDescription = "Open in Browser",
                            tint = badgeColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

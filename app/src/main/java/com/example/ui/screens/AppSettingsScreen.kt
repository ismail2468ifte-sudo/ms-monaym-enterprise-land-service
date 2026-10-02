package com.example.ui.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Security
import com.example.util.RidmikKeyboardManager
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.ui.components.KeystoreSha256Card
import com.example.ui.theme.LightBackground
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.viewmodel.LandViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    viewModel: LandViewModel,
    isBangla: Boolean,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
    }

    val context = LocalContext.current

    // Runtime Permission State Tracker
    fun checkPerm(perm: String): Boolean {
        return ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_GRANTED
    }

    var cameraGranted by remember { mutableStateOf(checkPerm(Manifest.permission.CAMERA)) }
    var locationGranted by remember { mutableStateOf(checkPerm(Manifest.permission.ACCESS_FINE_LOCATION)) }
    var notificationGranted by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                checkPerm(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                true
            }
        )
    }

    // Permission Launchers for required runtime resources
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        cameraGranted = granted
        val msg = if (granted) {
            if (isBangla) "ক্যামেরা পারমিশন সক্রিয় করা হয়েছে!" else "Camera permission granted!"
        } else {
            if (isBangla) "ক্যামেরা পারমিশন দেওয়া হয়নি।" else "Camera permission denied."
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    val locationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        locationGranted = granted
        val msg = if (granted) {
            if (isBangla) "জিপিএস লোকেশন পারমিশন সক্রিয় করা হয়েছে!" else "Location permission granted!"
        } else {
            if (isBangla) "লোকেশন পারমিশন দেওয়া হয়নি।" else "Location permission denied."
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        notificationGranted = granted
        val msg = if (granted) {
            if (isBangla) "নোটিফিকেশন পারমিশন সক্রিয় করা হয়েছে!" else "Notification permission granted!"
        } else {
            if (isBangla) "নোটিফিকেশন পারমিশন দেওয়া হয়নি।" else "Notification permission denied."
        }
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    // Text Scale / Font Size Selection state (বড় ফন্ট সাইজ অপশন)
    var selectedFontSizeOption by remember { mutableStateOf(1) } // 0: Normal, 1: Large (Default), 2: Extra Large
    var showPrivacyPolicyDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isBangla) "অ্যাপ পারমিশন ও সেটিং" else "App Permissions & Settings",
                            fontWeight = FontWeight.Bold,
                            fontSize = 19.sp,
                            color = Color.White
                        )
                        Text(
                            text = "M.S MONAYM ENTERPRISE",
                            fontSize = 13.sp,
                            color = MonaymGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("settings_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    Surface(
                        onClick = { viewModel.toggleLanguage() },
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("settings_language_toggle_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isBangla) "বাংলা" else "ENG",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MonaymPrimary
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightBackground)
                .testTag("settings_screen_list"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Overview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymDarkNavy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MonaymGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = MonaymGold,
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isBangla) "সকল সিস্টেম পারমিশন নিয়ন্ত্রণ" else "All System Permission Control",
                                fontWeight = FontWeight.Bold,
                                fontSize = 17.sp,
                                color = Color.White
                            )
                            Text(
                                text = if (isBangla) "অ্যাপের ক্যামেরা, জিপিএস, ফাইল ও নোটিফিকেশন নিরাপত্তা"
                                else "Manage Camera, GPS Location, Files & Notifications",
                                fontSize = 13.sp,
                                color = Color.White,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            // Permissions Section
            item {
                Text(
                    text = if (isBangla) "অ্যাপ্লিকেশন পারমিশন সমূহ (Permissions)" else "Application Permissions",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = MonaymPrimaryDark
                )
            }

            // 1. Camera Permission Card
            item {
                PermissionItemCard(
                    icon = Icons.Default.CameraAlt,
                    title = if (isBangla) "ক্যামেরা পারমিশন (Camera)" else "Camera Permission",
                    description = if (isBangla) "জমির খতিয়ান, দলিল ও কাগজপত্র সরাসরি স্ক্যান করার জন্য।"
                    else "Required to scan land documents, Porcha and Khatians.",
                    isGranted = cameraGranted,
                    isBangla = isBangla,
                    onRequest = {
                        cameraLauncher.launch(Manifest.permission.CAMERA)
                    }
                )
            }

            // 2. GPS Location Permission Card
            item {
                PermissionItemCard(
                    icon = Icons.Default.LocationOn,
                    title = if (isBangla) "জিপিএস লোকেশন পারমিশন (GPS Map)" else "GPS Location Permission",
                    description = if (isBangla) "মৌজা ম্যাপে আপনার বর্তমান অবস্থান ও দাগ নম্বর চিহ্নিত করতে।"
                    else "Used to pinpoint your current location on Mouza cadastral maps.",
                    isGranted = locationGranted,
                    isBangla = isBangla,
                    onRequest = {
                        locationLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                )
            }

            // 3. Storage / Files Card (Google Play Console Zero-Permission Scoped Storage)
            item {
                PermissionItemCard(
                    icon = Icons.Default.Folder,
                    title = if (isBangla) "ফাইল ও স্টোরেজ (Scoped Storage)" else "Files & Storage (Scoped Storage)",
                    description = if (isBangla) "গুগল প্লে স্টোরের পলিসি অনুযায়ী কোনো ক্ষতিকর স্টোরেজ পারমিশন ছাড়াই সুরক্ষিত Scoped Storage ও ফটো পিকার দিয়ে ফাইল ও রসিদ সংরক্ষণ করা হয়।"
                    else "Zero-permission Scoped Storage and Photo Picker fully compliant with Google Play Console policies.",
                    isGranted = true,
                    isBangla = isBangla,
                    onRequest = {
                        Toast.makeText(
                            context,
                            if (isBangla) "প্লে স্টোর নীতি অনুযায়ী সুরক্ষিত ফাইল স্টোরেজ সার্বক্ষণিক সক্রিয়।" else "Play Store compliant safe storage is always active.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    canToggle = false
                )
            }

            // 4. Notifications Permission Card
            item {
                PermissionItemCard(
                    icon = Icons.Default.Notifications,
                    title = if (isBangla) "নোটিফিকেশন পারমিশন (Alerts)" else "Notification Permission",
                    description = if (isBangla) "খাজনা পেমেন্ট নিশ্চিতকরণ ও ভূমি সেবার আপডেট নোটিশ পেতে।"
                    else "Receive instant alerts for tax payments and land service notifications.",
                    isGranted = notificationGranted,
                    isBangla = isBangla,
                    onRequest = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            Toast.makeText(
                                context,
                                if (isBangla) "নোটিফিকেশন পারমিশন পূর্বনির্ধারিতভাবে সক্রিয়।" else "Notifications already enabled.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                )
            }

            // 5. Internet & Network Card (Install-time)
            item {
                PermissionItemCard(
                    icon = Icons.Default.Wifi,
                    title = if (isBangla) "ইন্টারনেট ও নেটওয়ার্ক (Internet)" else "Internet & Connectivity",
                    description = if (isBangla) "ভূমি মন্ত্রণালয়ের পোর্টাল, ই-পর্চা ও অনলাইন পেমেন্টের জন্য প্রয়োজন।"
                    else "Required for online land portals, Porcha search and payment verification.",
                    isGranted = true,
                    isBangla = isBangla,
                    onRequest = {
                        Toast.makeText(
                            context,
                            if (isBangla) "ইন্টারনেট পারমিশন সার্বক্ষণিক সক্রিয় রয়েছে।" else "Internet permission is active.",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    canToggle = false
                )
            }

            // Open Android System Settings Button
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = null,
                                tint = MonaymPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = if (isBangla) "ডিভাইসের সিস্টেম অ্যাপ সেটিং" else "Device System App Settings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = MonaymDarkNavy
                            )
                        }

                        Text(
                            text = if (isBangla) "কোনো পারমিশন ব্লক থাকলে সরাসরি ফোনের মূল সেটিংসে গিয়ে এক ক্লিকে অনুমতি দিতে পারেন।"
                            else "If any permission was denied permanently, you can grant it directly from system app settings.",
                            fontSize = 13.5.sp,
                            color = MonaymPrimaryDark,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Button(
                            onClick = {
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.parse("package:${context.packageName}")
                                }
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_system_settings_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenInNew,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBangla) "সিস্টেম অ্যাপ সেটিংসে যান" else "Open Phone App Settings",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Font & Text Size Preference Section (User request: সকল লেখার সাইজ আরও বড় বানিয়ে দিবেন)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = null,
                                tint = MonaymPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (isBangla) "লেখার সাইজ ও প্রদর্শন স্কেল" else "Text Size & Display Scale",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MonaymDarkNavy
                                )
                                Text(
                                    text = if (isBangla) "আরামদায়ক ও স্পষ্ট পড়ার জন্য বড় ফন্ট সক্রিয়" else "Large legible font enabled by default",
                                    fontSize = 12.sp,
                                    color = MonaymGreen,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val options = if (isBangla) listOf("সাধারণ (15sp)", "বড় (17sp) ✓", "অতিরিক্ত বড় (20sp)")
                            else listOf("Normal (15sp)", "Large (17sp) ✓", "Extra Large (20sp)")

                            options.forEachIndexed { index, title ->
                                val isSelected = selectedFontSizeOption == index
                                Surface(
                                    onClick = {
                                        selectedFontSizeOption = index
                                        Toast.makeText(
                                            context,
                                            if (isBangla) "লেখার আকার সফলভাবে নির্বাচন করা হয়েছে!" else "Text size updated!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) MonaymPrimary else LightBackground,
                                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("font_scale_btn_$index")
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = title,
                                            fontSize = if (index == 2) 13.sp else 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) Color.White else MonaymDarkNavy
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Official Ridmik Keyboard Integration Card (Ridmik Labs: 8115188161983387290)
            item {
                val isRidmikInstalled = RidmikKeyboardManager.isRidmikInstalled(context)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ridmik_keyboard_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MonaymPrimary.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MonaymPrimary.copy(alpha = 0.12f),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Keyboard,
                                            contentDescription = null,
                                            tint = MonaymPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isBangla) "রিদ্মিক কীবোর্ড (Ridmik Keyboard)" else "Ridmik Keyboard (Bangla)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.5.sp,
                                        color = MonaymDarkNavy
                                    )
                                    Text(
                                        text = "Ridmik Labs (Dev ID: 8115188161983387290)",
                                        fontSize = 12.sp,
                                        color = MonaymPrimaryDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isRidmikInstalled) MonaymGreen.copy(alpha = 0.2f) else MonaymGold.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = if (isRidmikInstalled) (if (isBangla) "সক্রিয় ✓" else "Active ✓")
                                    else (if (isBangla) "ইনস্টল নেই" else "Install"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isRidmikInstalled) MonaymGreen else MonaymDarkNavy,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = if (isBangla)
                                "এই অ্যাপে জমি-জমা, খতিয়ান ও দলিলের সকল তথ্য সহজে বাংলা ফনেটিক ও প্রভাত লেআউটে টাইপ করার জন্য অফিশিয়াল রিদ্মিক কীবোর্ড (Ridmik Keyboard) ব্যবহৃত হবে। পূর্বের সাধারণ কীবোর্ড বাদ দিয়ে এক ক্লিকে রিদ্মিক কীবোর্ড নির্বাচন করুন।"
                            else
                                "Ridmik Keyboard by Ridmik Labs is the designated official Bengali typing keyboard for this application. Switch to Ridmik Keyboard for the best typing experience.",
                            fontSize = 13.5.sp,
                            color = MonaymDarkNavy,
                            lineHeight = 19.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    RidmikKeyboardManager.promptSwitchOrInstall(context, isBangla)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBangla) "কীবোর্ড বদলান" else "Switch Keyboard",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    RidmikKeyboardManager.openRidmikPlayStore(context)
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBangla) "প্লে স্টোর লিংক" else "Play Store",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Official Non-Governmental App & Government URL Source Disclaimer Card (Play Policy)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("govt_disclaimer_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MonaymPrimary.copy(alpha = 0.35f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MonaymPrimary.copy(alpha = 0.12f),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = MonaymPrimary,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = if (isBangla) "অ্যাপের ধরন ও সরকারি সোর্স ঘোষণা" else "App Nature & Govt URL Source",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.5.sp,
                                        color = MonaymDarkNavy
                                    )
                                    Text(
                                        text = if (isBangla) "বেসরকারি অ্যাপ নীতি ও স্বচ্ছতা" else "Non-Governmental App Transparency",
                                        fontSize = 12.sp,
                                        color = MonaymPrimaryDark,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MonaymGold.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = if (isBangla) "ডিসক্লেইমার" else "Disclaimer",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonaymDarkNavy,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Prominent Primary Disclaimer Box (User Specified Text)
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = LightBackground,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MonaymPrimary.copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "📌 " + LandViewModel.DISCLAIMER_NON_GOVERNMENT_BN,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonaymPrimaryDark,
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = LandViewModel.DISCLAIMER_NON_GOVERNMENT_EN,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MonaymDarkNavy,
                                    lineHeight = 18.sp
                                )
                            }
                        }

                        // Government URL Sources Details
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                text = if (isBangla) "🌐 অ্যাপে ব্যবহৃত সরকারি লিংক সোর্সসমূহ:" else "🌐 Official Government Sources Used:",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MonaymDarkNavy
                            )

                            val sources = listOf(
                                Triple(
                                    if (isBangla) "ভূমি মন্ত্রণালয় বাংলাদেশ" else "Ministry of Land, Bangladesh",
                                    "https://www.land.gov.bd",
                                    "ভূমি সংক্রান্ত গেজেট ও সেবা নীতিমালা"
                                ),
                                Triple(
                                    if (isBangla) "জাতীয় ই-পর্চা সেবা পোর্টাল" else "National e-Porcha Portal",
                                    "https://eporcha.gov.bd",
                                    "অনলাইন খতিয়ান ও পর্চা যাচাই সেবা"
                                ),
                                Triple(
                                    if (isBangla) "ই-নামজারি ও মিউটেশন পোর্টাল" else "e-Mutation Service Portal",
                                    "https://mutation.land.gov.bd",
                                    "নামজারি আবেদন ও ট্র্যাকিং তথ্য"
                                )
                            )

                            sources.forEach { (name, url, desc) ->
                                Surface(
                                    onClick = {
                                        viewModel.setActiveWebUrl(url)
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Handle
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    color = LightBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = name,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MonaymPrimaryDark
                                            )
                                            Text(
                                                text = url,
                                                fontSize = 11.5.sp,
                                                color = MonaymPrimary,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                            Text(
                                                text = desc,
                                                fontSize = 11.sp,
                                                color = Color(0xFF475569)
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.OpenInNew,
                                            contentDescription = "Open Source",
                                            tint = MonaymPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // Clarification Note
                        Text(
                            text = if (isBangla)
                                "⚠️ স্পষ্টীকরণ: M.S MONAYM ENTERPRISE কোনো সরকারি দপ্তর নয়। ব্যবহারকারীর সুবিধার জন্য পাবলিক সরকারি ওয়েবসাইটের সোর্স লিংক ও তথ্য প্রদর্শিত হয়েছে।"
                            else
                                "⚠️ Notice: M.S MONAYM ENTERPRISE is an independent private enterprise. Public government portal links are provided solely for user reference.",
                            fontSize = 12.sp,
                            color = MonaymDarkNavy,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Keystore SHA-256 Fingerprint Card
            item {
                KeystoreSha256Card(isBangla = isBangla)
            }

            // Official Privacy Policy Card (গোপনীয়তা নীতি - Google Play Store Mandatory Policy)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("privacy_policy_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, MonaymPrimary.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = MonaymPrimary.copy(alpha = 0.12f),
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Security,
                                            contentDescription = null,
                                            tint = MonaymPrimary,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isBangla) "গোপনীয়তা নীতি (Privacy Policy)" else "Privacy Policy",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MonaymDarkNavy
                                    )
                                    Text(
                                        text = if (isBangla) "গুগল প্লে স্টোর পলিসি অনুসৃত" else "Google Play Policy Compliant",
                                        fontSize = 12.sp,
                                        color = MonaymGreen,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (isBangla)
                                "ব্যবহারকারীর তথ্যের সর্বোচ্চ গোপনীয়তা ও সুরক্ষা নিশ্চিত করা হয়। অ্যাপটিতে কোনো ব্রড স্টোরেজ পারমিশন নেওয়া হয় না এবং কোনো ব্যক্তিগত তথ্য তৃতীয় পক্ষের কাছে বিক্রি বা শেয়ার করা হয় না।"
                            else
                                "User privacy and data security are strictly protected. No broad storage access is requested and no personal data is shared with third parties.",
                            fontSize = 13.sp,
                            color = MonaymDarkNavy,
                            lineHeight = 18.sp
                        )

                        Button(
                            onClick = { showPrivacyPolicyDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isBangla) "সম্পূর্ণ গোপনীয়তা নীতি পড়ুন" else "Read Full Privacy Policy",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }
            }

            // Enterprise Support Card with 01976444504
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymPrimaryDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = if (isBangla) "প্রতিষ্ঠানের অফিসিয়াল সাপোর্ট ও যোগাযোগ" else "Enterprise Official Support",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            text = if (isBangla) "সিম ও WhatsApp: ${LandViewModel.ENTERPRISE_PHONE}" else "SIM & WhatsApp: ${LandViewModel.ENTERPRISE_PHONE}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MonaymGold
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${LandViewModel.ENTERPRISE_PHONE}"))
                                    context.startActivity(dial)
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = MonaymGreen),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(if (isBangla) "সিম কল" else "SIM Call", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }

                            Button(
                                onClick = {
                                    val wa = Intent(Intent.ACTION_VIEW, Uri.parse(LandViewModel.ENTERPRISE_WHATSAPP_LINK))
                                    try {
                                        context.startActivity(wa)
                                    } catch (e: Exception) {
                                        // Ignore
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("💬 WhatsApp", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.White)
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showPrivacyPolicyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyPolicyDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = MonaymPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isBangla) "গোপনীয়তা নীতি (Privacy Policy)" else "Privacy Policy",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MonaymGold.copy(alpha = 0.2f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "📌 " + LandViewModel.DISCLAIMER_NON_GOVERNMENT_BN,
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MonaymPrimaryDark,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Text(
                        text = if (isBangla)
                            "১. তথ্য সংগ্রহ ও ব্যবহার:\n• আমরা কোনো ক্ষতিকর ব্রড স্টোরেজ পারমিশন নিই না।\n• ক্যামেরা শুধুমাত্র আপনার জমির দলিল ও পর্চা স্ক্যানের জন্য ব্যবহৃত হয়।\n• জিপিএস লোকেশন শুধুমাত্র মৌজা ম্যাপে দাগ দেখার জন্য ব্যবহৃত হয়।\n• সংরক্ষিত খতিয়ান ও দলিলের তথ্য ডিভাইসের সুরক্ষিত লোকাল ডাটাবেজে এনক্রিপ্ট থাকে।\n\n২. ডেটা নিরাপত্তা ও গোপনীয়তা:\n• কোনো ব্যক্তিগত তথ্য তৃতীয় পক্ষের কাছে বিক্রি বা শেয়ার করা হয় না।\n• আপনি যেকোনো সময় নিজের সংরক্ষিত ডেটা নিজে মুছে ফেলতে পারবেন।\n\n৩. অফিশিয়াল যোগাযোগ:\n• প্রতিষ্ঠান: M.S MONAYM ENTERPRISE\n• স্বত্বাধিকারী: মোঃ ইসমাইল খান / মোঃ মোনায়েম\n• মোবাইল ও WhatsApp: 01976444504\n• ইমেইল: ismail2468ifte@gmail.com\n• ঠিকানা: ঢাকা, বাংলাদেশ"
                        else
                            "1. Data Collection & Use:\n• No broad storage permission is requested.\n• Camera is used only for scanning documents.\n• GPS is used solely for cadastral mapping.\n• All land records remain stored encrypted in your local device.\n\n2. Security:\n• No data is sold or shared with third parties.\n• You can delete your records anytime.\n\n3. Contact:\n• Entity: M.S MONAYM ENTERPRISE\n• Mobile: 01976444504\n• Email: ismail2468ifte@gmail.com",
                        fontSize = 13.sp,
                        color = MonaymDarkNavy,
                        lineHeight = 18.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showPrivacyPolicyDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary)
                ) {
                    Text(if (isBangla) "ঠিক আছে" else "Close")
                }
            }
        )
    }
}

@Composable
private fun PermissionItemCard(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    isBangla: Boolean,
    onRequest: () -> Unit,
    canToggle: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("permission_item_${title.take(10)}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isGranted) MonaymGreen.copy(alpha = 0.15f) else Color(0xFFFFEBEE),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isGranted) MonaymGreen else Color(0xFFD32F2F),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = MonaymDarkNavy
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (isGranted) MonaymGreen.copy(alpha = 0.15f) else Color(0xFFFFEBEE)
                            ) {
                                Text(
                                    text = if (isGranted) (if (isBangla) "অনুমোদিত (Active)" else "Granted")
                                    else (if (isBangla) "অনুমতি প্রয়োজন" else "Needs Permission"),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isGranted) MonaymGreen else Color(0xFFD32F2F),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                if (canToggle) {
                    if (isGranted) {
                        Surface(
                            shape = CircleShape,
                            color = MonaymGreen.copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Active",
                                    tint = MonaymGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = onRequest,
                            colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isBangla) "অনুমতি দিন" else "Grant",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Text(
                text = description,
                fontSize = 13.5.sp,
                color = MonaymDarkNavy,
                lineHeight = 18.5.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

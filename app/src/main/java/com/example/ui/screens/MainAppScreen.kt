package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.AddLandDialog
import com.example.ui.components.KeystoreSigningInfoDialog
import com.example.ui.components.OtpLoginModal
import com.example.ui.components.ProcessPaymentDialog
import com.example.ui.components.UploadDocumentDialog
import com.example.ui.screens.AppSettingsScreen
import com.example.ui.screens.FileHubScreen
import com.example.ui.screens.LandAiAssistantScreen
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.viewmodel.LandViewModel
import com.msmonaym.land.ui.registration.RegistrationScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(viewModel: LandViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val userRole by viewModel.userRole.collectAsState()
    val userPhone by viewModel.userPhone.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Tab state:
    // 0: Page 1 - Smart Circular File Hub
    // 10: Page 2 - Digital Land AI Assistant
    // 1: LandRecordsScreen
    // 2: DocumentVaultScreen
    // 3: MapLocationScreen
    // 4: PaymentGatewayScreen
    // 5: NotificationsScreen
    // 6: AdminDashboardScreen
    // 7: AppWebViewScreen
    // 8: RegistrationScreen
    // 99: AppSettingsScreen
    var currentTab by remember { mutableIntStateOf(0) }

    // Dialog state
    var showAddLandDialog by remember { mutableStateOf(false) }
    var showUploadDocDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showOtpModal by remember { mutableStateOf(false) }
    var showSigningKeyModal by remember { mutableStateOf(false) }

    // Intercept back button from any sub-screen or secondary page to return to File Hub
    BackHandler(enabled = currentTab != 0) {
        currentTab = 0
    }

    Scaffold(
        topBar = {
            // RegistrationScreen and AppSettingsScreen render their own TopAppBars
            if (currentTab != 8 && currentTab != 99) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (currentTab != 0 && currentTab != 10) {
                                IconButton(
                                    onClick = { currentTab = 0 },
                                    modifier = Modifier.testTag("subscreen_back_btn")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back to File Hub",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                            }
                            Text(
                                text = if (currentTab == 10) {
                                    if (isBangla) "ডিজিটাল ল্যান্ড AI সহকারী" else "Digital Land AI Assistant"
                                } else {
                                    "M.S MONAYM ENTERPRISE"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = Color.White,
                                modifier = Modifier.testTag("main_app_title_text")
                            )
                        }
                    },
                    actions = {
                        // Per user request, the top of the first page has no extra files or cluttered buttons
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MonaymPrimary
                    )
                )
            }
        },
        bottomBar = {
            // Two Primary Pages Navigation Bar: Page 1 (File Hub) and Page 2 (AI Assistant)
            if (currentTab == 0 || currentTab == 10) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 10.dp,
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .testTag("primary_two_page_nav_bar")
                ) {
                    val primaryPages = listOf(
                        Triple(0, if (isBangla) "📁 স্মার্ট ফাইল হাব" else "📁 Smart File Hub", Icons.Default.Folder),
                        Triple(10, if (isBangla) "🤖 ডিজিটাল AI সহকারী" else "🤖 Digital AI Assistant", Icons.Default.SmartToy)
                    )

                    primaryPages.forEach { (index, label, icon) ->
                        val isSelected = currentTab == index
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = index },
                            icon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (isSelected) MonaymPrimary else MonaymPrimaryDark,
                                    modifier = Modifier.size(26.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 13.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) MonaymPrimary else MonaymPrimaryDark
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = if (index == 10) MonaymGold.copy(alpha = 0.25f) else MonaymPrimary.copy(alpha = 0.15f)
                            ),
                            modifier = Modifier.testTag("two_page_tab_$index")
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                // Page 1: Smart Circular File Hub
                0 -> FileHubScreen(
                    viewModel = viewModel,
                    isBangla = isBangla,
                    onNavigateToFile = { currentTab = it },
                    onOpenSettings = { currentTab = 99 }
                )
                // Page 2: Digital Land AI Assistant
                10 -> LandAiAssistantScreen(
                    viewModel = viewModel,
                    isBangla = isBangla,
                    onNavigateToFile = { currentTab = it }
                )
                // Detail Sub-screens
                1 -> LandRecordsScreen(
                    viewModel = viewModel,
                    onNavigateToMap = { currentTab = 3 },
                    onOpenAddLandDialog = { showAddLandDialog = true }
                )
                2 -> DocumentVaultScreen(
                    viewModel = viewModel,
                    onOpenUploadDialog = { showUploadDocDialog = true }
                )
                3 -> MapLocationScreen(viewModel = viewModel)
                4 -> PaymentGatewayScreen(
                    viewModel = viewModel,
                    onOpenPaymentDialog = { showPaymentDialog = true }
                )
                5 -> NotificationsScreen(viewModel = viewModel)
                6 -> AdminDashboardScreen(viewModel = viewModel)
                7 -> AppWebViewScreen(viewModel = viewModel)
                8 -> RegistrationScreen(
                    viewModel = viewModel,
                    isBangla = isBangla,
                    onBack = { currentTab = 0 },
                    onOpenPaymentDialog = { _, _ -> showPaymentDialog = true }
                )
                99 -> AppSettingsScreen(
                    viewModel = viewModel,
                    isBangla = isBangla,
                    onBack = { currentTab = 0 }
                )
            }
        }
    }

    // Interactive Dialogs
    if (showAddLandDialog) {
        AddLandDialog(
            isBangla = isBangla,
            onDismiss = { showAddLandDialog = false },
            onAdd = { khatian, dag, mouza, district, owner, area, landType ->
                viewModel.addLandRecord(khatian, dag, mouza, district, owner, area, landType)
            }
        )
    }

    if (showUploadDocDialog) {
        UploadDocumentDialog(
            isBangla = isBangla,
            onDismiss = { showUploadDocDialog = false },
            onUpload = { title, docType, fileName, khatianNo ->
                viewModel.addDocument(title, docType, fileName, khatianNo)
            }
        )
    }

    if (showPaymentDialog) {
        ProcessPaymentDialog(
            isBangla = isBangla,
            onDismiss = { showPaymentDialog = false },
            onPay = { amount, method, purpose ->
                viewModel.processPayment(amount, method, purpose)
            }
        )
    }

    if (showOtpModal) {
        OtpLoginModal(
            isBangla = isBangla,
            onDismiss = { showOtpModal = false },
            onLoginSuccess = { phone, otp ->
                viewModel.verifyOtpAndLogin(phone, otp)
            }
        )
    }

    // Keystore Signing Certificate Modal (AAB / SHA-256)
    if (showSigningKeyModal) {
        KeystoreSigningInfoDialog(
            isBangla = isBangla,
            onDismiss = { showSigningKeyModal = false }
        )
    }
}

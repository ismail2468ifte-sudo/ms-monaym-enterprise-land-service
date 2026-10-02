package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BkashColor
import com.example.ui.theme.LightBackground
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.viewmodel.LandViewModel

data class CircularFileItem(
    val id: Int,
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val subtitleEn: String,
    val icon: ImageVector,
    val primaryColor: Color,
    val gradientColors: List<Color>,
    val badge: String? = null
)

@Composable
fun FileHubScreen(
    viewModel: LandViewModel,
    isBangla: Boolean,
    onNavigateToFile: (Int) -> Unit,
    onOpenSettings: () -> Unit
) {
    val context = LocalContext.current
    val landRecords by viewModel.allLandRecords.collectAsState()
    val documents by viewModel.allDocuments.collectAsState()
    val payments by viewModel.allPayments.collectAsState()

    // 10 Gorgeous Circular Large File Hub Items
    val fileItems = listOf(
        CircularFileItem(
            id = 1,
            titleBn = "খতিয়ান ফাইল",
            titleEn = "Khatian File",
            subtitleBn = "${landRecords.size}টি রেকর্ড সংরক্ষিত",
            subtitleEn = "${landRecords.size} Records Saved",
            icon = Icons.Default.Assignment,
            primaryColor = MonaymPrimary,
            gradientColors = listOf(Color(0xFF0066FF), Color(0xFF003399)),
            badge = "${landRecords.size}"
        ),
        CircularFileItem(
            id = 8,
            titleBn = "দলিল ফি ফাইল",
            titleEn = "Deed Fee File",
            subtitleBn = "সাব-রেজিস্ট্রি ক্যালকুলেটর",
            subtitleEn = "Sub-Registry Fees",
            icon = Icons.Default.Calculate,
            primaryColor = Color(0xFF1B5E20),
            gradientColors = listOf(Color(0xFF2E7D32), Color(0xFF0D3E10)),
            badge = if (isBangla) "নতুন" else "NEW"
        ),
        CircularFileItem(
            id = 2,
            titleBn = "ডিজিটাল নথি",
            titleEn = "Document Vault",
            subtitleBn = "${documents.size}টি স্ক্যানকৃত ফাইল",
            subtitleEn = "${documents.size} Digital Files",
            icon = Icons.Default.Folder,
            primaryColor = Color(0xFF7B1FA2),
            gradientColors = listOf(Color(0xFF8E24AA), Color(0xFF4A148C))
        ),
        CircularFileItem(
            id = 3,
            titleBn = "মৌজা ম্যাপ",
            titleEn = "Mouza Map",
            subtitleBn = "জিপিএস দাগ লোকেশন",
            subtitleEn = "GPS Land Plotting",
            icon = Icons.Default.Map,
            primaryColor = Color(0xFFE65100),
            gradientColors = listOf(Color(0xFFF57C00), Color(0xFFBF360C))
        ),
        CircularFileItem(
            id = 4,
            titleBn = "খাজনা পেমেন্ট",
            titleEn = "Land Tax Pay",
            subtitleBn = "বিকাশ ও নগদ সেন্টমানি",
            subtitleEn = "bKash & Nagad",
            icon = Icons.Default.Payment,
            primaryColor = BkashColor,
            gradientColors = listOf(Color(0xFFE2136E), Color(0xFF9C0B48)),
            badge = if (isBangla) "অনলাইন" else "LIVE"
        ),
        CircularFileItem(
            id = 7,
            titleBn = "ভূমি পোর্টাল",
            titleEn = "Govt Portal",
            subtitleBn = "ই-পর্চা ও ভূমি সেবা",
            subtitleEn = "eporcha.gov.bd",
            icon = Icons.Default.Public,
            primaryColor = Color(0xFF0288D1),
            gradientColors = listOf(Color(0xFF03A9F4), Color(0xFF01579B))
        ),
        CircularFileItem(
            id = 5,
            titleBn = "নোটিশ ফাইল",
            titleEn = "Notices File",
            subtitleBn = "সার্ভার ও সেবা আপডেট",
            subtitleEn = "Alerts & Updates",
            icon = Icons.Default.Notifications,
            primaryColor = Color(0xFFF9A825),
            gradientColors = listOf(Color(0xFFFBC02D), Color(0xFFE65100))
        ),
        CircularFileItem(
            id = 6,
            titleBn = "অ্যাডমিন ফাইল",
            titleEn = "Admin Control",
            subtitleBn = "সার্ভার ও ডায়াগনস্টিকস",
            subtitleEn = "System Diagnostics",
            icon = Icons.Default.AdminPanelSettings,
            primaryColor = MonaymDarkNavy,
            gradientColors = listOf(Color(0xFF1E293B), Color(0xFF0A192F))
        ),
        CircularFileItem(
            id = 99, // Settings
            titleBn = "পারমিশন সেটিং",
            titleEn = "Permissions",
            subtitleBn = "ক্যামেরা, জিপিএস ও ফন্ট",
            subtitleEn = "All Permissions",
            icon = Icons.Default.Settings,
            primaryColor = Color(0xFF455A64),
            gradientColors = listOf(Color(0xFF607D8B), Color(0xFF263238)),
            badge = "⚙️"
        ),
        CircularFileItem(
            id = 98, // Hotline
            titleBn = "সরাসরি যোগাযোগ",
            titleEn = "Direct Contact",
            subtitleBn = "সিম ও WhatsApp",
            subtitleEn = "Call & WhatsApp",
            icon = Icons.Default.Call,
            primaryColor = MonaymGreen,
            gradientColors = listOf(Color(0xFF00C853), Color(0xFF1B5E20)),
            badge = "24/7"
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("file_hub_screen_grid"),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Section Title
        item(span = { GridItemSpan(2) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangla) "📁 সকল স্মার্ট ফাইল ও সেবা (File Hub)" else "📁 All Land Service Files",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MonaymDarkNavy
                )
                Text(
                    text = if (isBangla) "১০টি বিভাগ" else "10 Categories",
                    fontSize = 12.5.sp,
                    color = MonaymPrimaryDark,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Large Circular File Items Grid
        items(fileItems) { item ->
            CircularFileCard(
                item = item,
                isBangla = isBangla,
                onClick = {
                    when (item.id) {
                        99 -> onOpenSettings()
                        98 -> {
                            val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${LandViewModel.ENTERPRISE_PHONE}"))
                            context.startActivity(dial)
                        }
                        else -> onNavigateToFile(item.id)
                    }
                }
            )
        }

        // Bottom Helpline Card
        item(span = { GridItemSpan(2) }) {
            Surface(
                onClick = {
                    val wa = Intent(Intent.ACTION_VIEW, Uri.parse(LandViewModel.ENTERPRISE_WHATSAPP_LINK))
                    try {
                        context.startActivity(wa)
                    } catch (e: Exception) {
                        val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${LandViewModel.ENTERPRISE_PHONE}"))
                        context.startActivity(dial)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFF25D366),
                shadowElevation = 3.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("file_hub_whatsapp_banner")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "💬 " + if (isBangla) "সরাসরি WhatsApp-এ পরামর্শ নিন: ${LandViewModel.ENTERPRISE_PHONE}"
                        else "Chat on WhatsApp: ${LandViewModel.ENTERPRISE_PHONE}",
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CircularFileCard(
    item: CircularFileItem,
    isBangla: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .testTag("circular_file_btn_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Big Circular Icon with Gradient and Glow Shadow
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(86.dp)
            ) {
                // Outer subtle ring
                Surface(
                    shape = CircleShape,
                    color = item.primaryColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(86.dp)
                ) {}

                // Main Circular Gradient Icon
                Surface(
                    shape = CircleShape,
                    color = Color.Transparent,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .size(72.dp)
                        .background(
                            brush = Brush.verticalGradient(item.gradientColors),
                            shape = CircleShape
                        )
                        .border(
                            width = 2.5.dp,
                            color = Color.White.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.titleEn,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }

                // Optional Floating Badge
                if (item.badge != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MonaymGold,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 2.dp, end = 2.dp)
                    ) {
                        Text(
                            text = item.badge,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MonaymDarkNavy,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Large, Extra Legible Title
            Text(
                text = if (isBangla) item.titleBn else item.titleEn,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MonaymDarkNavy,
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Subtitle - Deep colored text on white background for high legibility
            Text(
                text = if (isBangla) item.subtitleBn else item.subtitleEn,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = MonaymPrimaryDark,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}


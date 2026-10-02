package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.viewmodel.LandViewModel

@Composable
fun LandRecordsScreen(
    viewModel: LandViewModel,
    onNavigateToMap: () -> Unit,
    onOpenAddLandDialog: () -> Unit
) {
    val isBangla by viewModel.isBangla.collectAsState()
    val filteredRecords by viewModel.filteredLandRecords.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var expandedLandId by remember { mutableStateOf<Int?>(null) }
    var showAreaConverter by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onOpenAddLandDialog,
                containerColor = MonaymPrimary,
                contentColor = Color.White,
                modifier = Modifier.testTag("add_land_fab")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Land Record")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Search Bar & Filter Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            if (isBangla) "খতিয়ান / দাগ / মালিকের নাম দিয়ে খুঁজুন..."
                            else "Search Khatian, Dag, or Owner..."
                        )
                    },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = MonaymPrimary)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("land_search_field"),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MonaymPrimary,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { showAreaConverter = !showAreaConverter },
                    modifier = Modifier
                        .size(52.dp)
                        .background(
                            if (showAreaConverter) MonaymGold else MonaymPrimary.copy(alpha = 0.1f),
                            RoundedCornerShape(14.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = "Land Area Converter",
                        tint = if (showAreaConverter) Color.Black else MonaymPrimary
                    )
                }
            }

            // Area Calculator Assistant Tool
            AnimatedVisibility(visible = showAreaConverter) {
                AreaCalculatorCard(isBangla = isBangla)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "${if (isBangla) "মোট সংরক্ষিত খতিয়ান" else "Total Khatian Records"}: ${filteredRecords.size}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MonaymPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (filteredRecords.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MonaymPrimary,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isBangla) "কোনো খতিয়ান রেকর্ড পাওয়া যায়নি" else "No land records found",
                            color = MonaymDarkNavy,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredRecords) { land ->
                        val isExpanded = expandedLandId == land.id
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    expandedLandId = if (isExpanded) null else land.id
                                }
                                .testTag("land_item_${land.id}"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${if (isBangla) "খতিয়ান নম্বর" else "Khatian No"}: ${land.khatianNo}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Text(
                                            text = "${if (isBangla) "দাগ নম্বর" else "Dag No"}: ${land.dagNo}",
                                            fontSize = 14.sp,
                                            color = MonaymPrimary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    Surface(
                                        color = if (land.status.contains("Verified") || land.status.contains("যাচাইকৃত")) MonaymGreen.copy(
                                            alpha = 0.15f
                                        ) else MonaymGold.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = land.status,
                                            color = if (land.status.contains("Verified") || land.status.contains("যাচাইকৃত")) MonaymGreen else Color(
                                                0xFFD97706
                                            ),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "📍 ${land.mouza}, ${land.district}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MonaymDarkNavy
                                    )
                                    Text(
                                        text = "📐 ${land.area}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MonaymPrimaryDark
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${if (isBangla) "মালিক" else "Owner"}: ${land.ownerName}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MonaymDarkNavy
                                )

                                AnimatedVisibility(visible = isExpanded) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(1.dp)
                                                .background(Color.LightGray)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = "${if (isBangla) "জমির শ্রেণী" else "Land Type"}: ${land.landType}",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MonaymPrimaryDark
                                        )
                                        Text(
                                            text = "GPS Coordinates: Lat ${land.lat}, Lng ${land.lng}",
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MonaymPrimaryDark
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Button(
                                                onClick = {
                                                    viewModel.selectLandRecord(land)
                                                    onNavigateToMap()
                                                },
                                                colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Map,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (isBangla) "ম্যাপে দেখুন" else "View on Map",
                                                    fontSize = 12.sp
                                                )
                                            }

                                            OutlinedButton(
                                                onClick = { },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Share,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = if (isBangla) "শেয়ার" else "Share",
                                                    fontSize = 12.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item { Spacer(modifier = Modifier.height(60.dp)) }
                }
            }
        }
    }
}

@Composable
fun AreaCalculatorCard(isBangla: Boolean) {
    var shotokInput by remember { mutableStateOf("10") }
    val shotokVal = shotokInput.toDoubleOrNull() ?: 0.0
    val katha = shotokVal / 1.65
    val acre = shotokVal / 100.0

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MonaymGold.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = if (isBangla) "📐 জমির পরিমাপ ক্যালকুলেটর (Area Converter)" else "Land Area Converter",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = shotokInput,
                onValueChange = { shotokInput = it },
                label = { Text(if (isBangla) "শতক / ডেসিমেল (Shotok)" else "Shotok / Decimal") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "কাঠা (Katha): String.format(\"%.2f\", katha)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MonaymPrimaryDark
                )
                Text(
                    text = "একর (Acre): String.format(\"%.3f\", acre)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = MonaymGreen
                )
            }
        }
    }
}

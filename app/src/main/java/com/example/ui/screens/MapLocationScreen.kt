package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.LandRecord
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.viewmodel.LandViewModel

@Composable
fun MapLocationScreen(viewModel: LandViewModel) {
    val isBangla by viewModel.isBangla.collectAsState()
    val landRecords by viewModel.allLandRecords.collectAsState()
    val selectedLand by viewModel.selectedLandRecord.collectAsState()

    var activeLand by remember(selectedLand) { mutableStateOf(selectedLand ?: landRecords.firstOrNull()) }
    var zoomLevel by remember { mutableStateOf(15f) }
    var isSatelliteView by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Horizontal Selector for Land Records
        Text(
            text = if (isBangla) "জমির লোকেশন নির্বাচন করুন" else "Select Land Location Pinpoint",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = MonaymPrimary
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(landRecords) { land ->
                val isSelected = activeLand?.id == land.id
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MonaymPrimary else MaterialTheme.colorScheme.surface,
                    shadowElevation = 3.dp,
                    modifier = Modifier
                        .clickable { activeLand = land }
                        .testTag("map_select_land_${land.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = if (isSelected) MonaymGold else MonaymPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "খতিয়ান: ${land.khatianNo}",
                                color = if (isSelected) Color.White else Color.Unspecified,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = land.mouza,
                                color = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Canvas Interactive Map Visualizer Box
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .testTag("map_canvas_container"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSatelliteView) Color(0xFF1E293B) else Color(0xFFE2E8F0)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                // Custom Canvas Map Simulation
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height

                    // Draw Grid lines (Map coordinate grid)
                    val gridStep = 60f
                    var x = 0f
                    while (x < width) {
                        drawLine(
                            color = if (isSatelliteView) Color.White.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.2f),
                            start = Offset(x, 0f),
                            end = Offset(x, height),
                            strokeWidth = 1f
                        )
                        x += gridStep
                    }
                    var y = 0f
                    while (y < height) {
                        drawLine(
                            color = if (isSatelliteView) Color.White.copy(alpha = 0.1f) else Color.Gray.copy(alpha = 0.2f),
                            start = Offset(0f, y),
                            end = Offset(width, y),
                            strokeWidth = 1f
                        )
                        y += gridStep
                    }

                    // Draw Rivers / Roads simulation
                    val roadPath = Path().apply {
                        moveTo(0f, height * 0.3f)
                        cubicTo(width * 0.3f, height * 0.2f, width * 0.6f, height * 0.5f, width, height * 0.4f)
                    }
                    drawPath(
                        path = roadPath,
                        color = Color(0xFF0288D1).copy(alpha = 0.6f),
                        style = Stroke(width = 16f)
                    )

                    // Draw Land Boundary Polygon for Active Land
                    val centerX = width * 0.5f
                    val centerY = height * 0.5f
                    val parcelPath = Path().apply {
                        moveTo(centerX - 100f, centerY - 80f)
                        lineTo(centerX + 120f, centerY - 60f)
                        lineTo(centerX + 140f, centerY + 100f)
                        lineTo(centerX - 80f, centerY + 110f)
                        close()
                    }

                    drawPath(
                        path = parcelPath,
                        color = MonaymGreen.copy(alpha = 0.35f)
                    )
                    drawPath(
                        path = parcelPath,
                        color = MonaymGreen,
                        style = Stroke(width = 4f)
                    )

                    // Draw Map Marker Pin in Center
                    drawCircle(
                        color = MonaymPrimary,
                        radius = 16f,
                        center = Offset(centerX, centerY)
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 6f,
                        center = Offset(centerX, centerY)
                    )
                }

                // Floating Controls on Map
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp
                    ) {
                        IconButton(onClick = { isSatelliteView = !isSatelliteView }) {
                            Icon(
                                imageVector = Icons.Default.Layers,
                                contentDescription = "Satellite Layer",
                                tint = if (isSatelliteView) MonaymGold else MonaymPrimary
                            )
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp
                    ) {
                        IconButton(onClick = { zoomLevel = (zoomLevel + 1f).coerceAtMost(20f) }) {
                            Icon(imageVector = Icons.Default.ZoomIn, contentDescription = "Zoom In")
                        }
                    }
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp
                    ) {
                        IconButton(onClick = { zoomLevel = (zoomLevel - 1f).coerceAtLeast(10f) }) {
                            Icon(imageVector = Icons.Default.ZoomOut, contentDescription = "Zoom Out")
                        }
                    }
                }

                // Active Land Info Overlay at Bottom of Map
                activeLand?.let { land ->
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                        shadowElevation = 6.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "📍 খতিয়ান: ${land.khatianNo} | দাগ: ${land.dagNo}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "মৌজা: ${land.mouza}, ${land.district}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "GPS: Lat ${land.lat}, Lng ${land.lng} | Zoom: ${zoomLevel.toInt()}x",
                                    fontSize = 11.sp,
                                    color = MonaymPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = { },
                                colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(if (isBangla) "আমার অবস্থান" else "My GPS", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

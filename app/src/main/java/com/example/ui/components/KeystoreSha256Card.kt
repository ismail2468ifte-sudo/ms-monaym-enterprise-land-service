package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Security
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BkashColor
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.util.KeystoreSignatureHelper

@Composable
fun KeystoreSha256Card(
    modifier: Modifier = Modifier,
    isBangla: Boolean = true
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    var showFullDetails by remember { mutableStateOf(false) }

    // Active signature from device runtime or keystore definition
    val sha256 = remember { KeystoreSignatureHelper.getActiveSha256(context) }
    val sha1 = remember { KeystoreSignatureHelper.getActiveSha1(context) }
    val sha256NoColons = remember(sha256) {
        sha256.replace(":", "").lowercase()
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("keystore_sha256_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, MonaymPrimary.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MonaymPrimary.copy(alpha = 0.12f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Key,
                                contentDescription = "Keystore Signing Key",
                                tint = MonaymPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isBangla) "AAB সাইনিং কী ফিঙ্গারপ্রিন্ট" else "AAB Signing Keystore Key",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "SHA-256 Certificate Fingerprint",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MonaymPrimaryDark
                        )
                    }
                }

                Surface(
                    color = MonaymGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MonaymGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MonaymGreen,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isBangla) "সাইন্ড ও ভেরিফাইড" else "SIGNED & READY",
                            color = MonaymGreen,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Description Label - Colored text on white background
            Text(
                text = if (isBangla)
                    "এই অ্যাপের AAB / APK যে Keystore ফাইল দিয়ে সাইন করা হয়েছে তার অফিশিয়াল SHA-256 ফিঙ্গারপ্রিন্ট নিচে দেওয়া হলো। এক ক্লিকে কপি করুন:"
                else
                    "Official SHA-256 fingerprint of the Keystore used to sign this AAB / APK build. Tap below to copy:",
                fontSize = 12.sp,
                color = MonaymDarkNavy,
                fontWeight = FontWeight.Medium,
                lineHeight = 17.sp
            )

            // SHA-256 Display Box (Highlighted, Monospaced & Copyable)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .border(1.dp, MonaymPrimaryDark.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                    .clickable {
                        clipboardManager.setText(AnnotatedString(sha256))
                        Toast.makeText(
                            context,
                            if (isBangla) "SHA-256 ক্লিপবোর্ডে কপি হয়েছে!" else "SHA-256 copied to clipboard!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .testTag("sha256_display_box"),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = null,
                                tint = MonaymPrimaryDark,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SHA-256:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MonaymPrimaryDark
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MonaymGold.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = if (isBangla) "ট্যাপ করে কপি করুন" else "Tap to copy",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    SelectionContainer {
                        Text(
                            text = sha256,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.5.sp,
                            color = MonaymPrimaryDark,
                            lineHeight = 18.sp,
                            modifier = Modifier.testTag("sha256_text_content")
                        )
                    }
                }
            }

            // Primary Copy Button
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(sha256))
                    Toast.makeText(
                        context,
                        if (isBangla) "✅ SHA-256 সফলভাবে কপি হয়েছে!" else "✅ SHA-256 copied successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .testTag("copy_sha256_button"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "Copy SHA-256",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isBangla) "SHA-256 ফিঙ্গারপ্রিন্ট কপি করুন" else "Copy SHA-256 Fingerprint",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Quick Additional Formats Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(sha256NoColons))
                        Toast.makeText(
                            context,
                            if (isBangla) "কোলন ছাড়া SHA-256 কপি হয়েছে: $sha256NoColons" else "Copied without colons: $sha256NoColons",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isBangla) "কোলন ছাড়া (Hex)" else "No Colons",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = {
                        clipboardManager.setText(AnnotatedString(sha1))
                        Toast.makeText(
                            context,
                            if (isBangla) "SHA-1 কপি হয়েছে: $sha1" else "SHA-1 copied: $sha1",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (isBangla) "SHA-1 কপি" else "Copy SHA-1",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Toggle Full Details
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showFullDetails = !showFullDetails }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isBangla) "কীস্টোর বিস্তারিত বিবরণ (Alias, Owner, Serial)" else "Keystore Details & Play Console Info",
                    fontSize = 11.sp,
                    color = MonaymPrimaryDark,
                    fontWeight = FontWeight.SemiBold
                )
                Icon(
                    imageVector = if (showFullDetails) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MonaymPrimaryDark,
                    modifier = Modifier.size(18.dp)
                )
            }

            // Expanded Keystore Details
            AnimatedVisibility(visible = showFullDetails) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    DetailRow(
                        label = if (isBangla) "কী অ্যালিয়াস (Key Alias):" else "Key Alias:",
                        value = KeystoreSignatureHelper.SIGNING_KEY_ALIAS
                    )
                    DetailRow(
                        label = if (isBangla) "কীস্টোর ফাইল:" else "Keystore File:",
                        value = KeystoreSignatureHelper.KEYSTORE_NAME
                    )
                    DetailRow(
                        label = if (isBangla) "অ্যাপ্লিকেশন আইডি:" else "Application ID:",
                        value = "com.aistudio.msmonaym.enterprise.kxmpzq"
                    )
                    DetailRow(
                        label = if (isBangla) "সার্টিফিকেট সিরিয়াল:" else "Serial:",
                        value = KeystoreSignatureHelper.CERT_SERIAL
                    )
                    DetailRow(
                        label = if (isBangla) "ইস্যুয়ার / ওনার:" else "Issuer / Owner:",
                        value = KeystoreSignatureHelper.CERT_OWNER
                    )

                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // AAB Bundle Government Information Policy Declaration
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MonaymPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (isBangla) "AAB বান্ডিল সরকারি নীতি ডিসক্লেইমার:" else "AAB Bundle Govt Policy Notice:",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonaymPrimaryDark
                                )

                                Surface(
                                    onClick = {
                                        clipboardManager.setText(AnnotatedString(
                                            "এই এ্যাপটি একটি বেসরকারি এ্যাপ,শুধু এ্যাপের মধ্যে থাকা কিছু কাজের সুবিধার জন্য সরকারি URL লিংক সোর্স ব্যবহার করা হবে। (Govt Sources: https://www.land.gov.bd, https://eporcha.gov.bd, https://mutation.land.gov.bd)"
                                        ))
                                        Toast.makeText(
                                            context,
                                            if (isBangla) "ডিসক্লেইমার কপি হয়েছে!" else "Disclaimer copied!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    },
                                    shape = RoundedCornerShape(6.dp),
                                    color = MonaymGold.copy(alpha = 0.25f)
                                ) {
                                    Text(
                                        text = if (isBangla) "কপি করুন" else "Copy Notice",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "এই এ্যাপটি একটি বেসরকারি এ্যাপ,শুধু এ্যাপের মধ্যে থাকা কিছু কাজের সুবিধার জন্য সরকারি URL লিংক সোর্স ব্যবহার করা হবে।",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MonaymDarkNavy,
                                lineHeight = 16.sp
                            )
                        }
                    }
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.5.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.SemiBold)
        Text(text = value, fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

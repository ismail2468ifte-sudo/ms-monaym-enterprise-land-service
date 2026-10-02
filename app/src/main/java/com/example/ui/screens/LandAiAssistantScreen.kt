package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import com.example.util.RidmikKeyboardManager
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BkashColor
import com.example.ui.theme.LightBackground
import com.example.ui.theme.MonaymDarkNavy
import com.example.ui.theme.MonaymGold
import com.example.ui.theme.MonaymGreen
import com.example.ui.theme.MonaymPrimary
import com.example.ui.theme.MonaymPrimaryDark
import com.example.ui.viewmodel.LandViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class AiMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val time: String,
    val suggestedAction: String? = null // "REGISTRATION", "PAYMENT", "MAP", "RECORDS", "CALL"
)

@Composable
fun LandAiAssistantScreen(
    viewModel: LandViewModel,
    isBangla: Boolean,
    onNavigateToFile: (Int) -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var userQueryInput by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    // Initial Welcoming Messages
    val messages = remember {
        mutableStateListOf(
            AiMessage(
                text = if (isBangla)
                    "আসসালামু আলাইকুম! আমি M.S MONAYM ENTERPRISE-এর ডিজিটাল ল্যান্ড AI সহকারী। এই অ্যাপে থাকা খতিয়ান, ই-পর্চা, নামজারি, দলিল রেজিস্ট্রি ফি, খাজনা পরিশোধ ও জমি মাপজোখের যেকোনো ফাইল এবং আইনি কাজে আমি আপনাকে তাৎক্ষণিক সহায়তা করতে পারি। নিচের যেকোনো বিষয়ে প্রশ্ন করুন বা লিখে জানান।"
                else
                    "Hello! I am the Digital Land AI Assistant of M.S MONAYM ENTERPRISE. I can help you with Khatians, e-Porcha, Mutation (Namjari), Deed Registration Fees, Land Development Tax, and Cadastral Survey calculations. How may I assist you today?",
                isUser = false,
                time = "AI Assistant"
            )
        )
    }

    // Quick Prompt Templates
    val promptTemplatesBangla = listOf(
        "🔍 খতিয়ান ও ই-পর্চা যাচাইয়ের নিয়ম কী?",
        "⚖️ দলিল রেজিস্ট্রেশন ফি ও কর কত শতাংশ?",
        "📝 ই-নামজারি (Mutation) করার সহজ ধাপসমূহ",
        "💰 অনলাইনে ভূমি উন্নয়ন কর (খাজনা) কীভাবে দেব?",
        "📐 শতাংশ, কাঠা ও বিঘা পরিমাপের সূত্র কী?",
        "⚠️ জাল দলিল ও জমির প্রতারণা চেনার উপায়",
        "📜 হেবা (Heba) দলিলের খরচ ও নিয়মাবলী"
    )

    val promptTemplatesEnglish = listOf(
        "🔍 How to verify Khatian & e-Porcha online?",
        "⚖️ Deed Registration fees & tax breakdown",
        "📝 Step-by-step e-Namjari Mutation guide",
        "💰 How to pay Land Development Tax online?",
        "📐 Decimal, Katha & Bigha measurement rules",
        "⚠️ How to detect fake land deeds & scams?",
        "📜 Heba deed rules & nominal govt fees"
    )

    fun sendAiPrompt(query: String) {
        if (query.isBlank() || isThinking) return

        messages.add(
            AiMessage(
                text = query,
                isUser = true,
                time = if (isBangla) "আপনি" else "You"
            )
        )
        userQueryInput = ""
        isThinking = true

        scope.launch {
            delay(600) // Natural thinking delay

            val (responseText, action) = generateIntelligentLandResponse(query, isBangla)

            messages.add(
                AiMessage(
                    text = responseText,
                    isUser = false,
                    time = if (isBangla) "ডিজিটাল AI সহকারী" else "Digital AI Assistant",
                    suggestedAction = action
                )
            )
            isThinking = false
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightBackground)
            .testTag("ai_assistant_screen")
    ) {
        // AI Hero Banner
        Surface(
            color = MonaymDarkNavy,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MonaymGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = MonaymGold,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isBangla) "ডিজিটাল ভূমি AI সহকারী" else "Digital Land AI Assistant",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MonaymGreen
                                ) {
                                    Text(
                                        text = if (isBangla) "সক্রিয়" else "LIVE",
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = if (isBangla) "সকল ফাইলের আইনি ও হিসাব বিষয়ক স্মার্ট গাইডলাইন"
                                else "Smart guidance for all land files & deeds",
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }

                    // Clear chat button
                    IconButton(
                        onClick = {
                            if (messages.size > 1) {
                                val first = messages.first()
                                messages.clear()
                                messages.add(first)
                            }
                        },
                        modifier = Modifier.testTag("ai_clear_chat_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear Chat",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Quick Prompt Chips Scroll
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val templates = if (isBangla) promptTemplatesBangla else promptTemplatesEnglish
                    templates.forEach { prompt ->
                        Surface(
                            onClick = { sendAiPrompt(prompt) },
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White.copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.25f))
                        ) {
                            Text(
                                text = prompt,
                                fontSize = 12.5.sp,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }

        // Messages Feed
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg ->
                AiMessageBubble(
                    message = msg,
                    isBangla = isBangla,
                    onNavigateToFile = onNavigateToFile,
                    onCallHotline = {
                        val dial = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${LandViewModel.ENTERPRISE_PHONE}"))
                        context.startActivity(dial)
                    },
                    onCopyText = {
                        val cb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        cb.setPrimaryClip(ClipData.newPlainText("AI Note", msg.text))
                        Toast.makeText(context, if (isBangla) "লেখাটি কপি করা হয়েছে!" else "Copied!", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            if (isThinking) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MonaymGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = MonaymGold, modifier = Modifier.size(14.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBangla) "AI সহকারী তথ্য বিশ্লেষণ করছে..." else "AI Assistant is analyzing...",
                            fontSize = 13.sp,
                            color = MonaymPrimaryDark,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Bottom Input Area
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                // Ridmik Keyboard Quick Switch / Bangla Typing Helper
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isBangla) "⌨️ বাংলা টাইপ:" else "⌨️ Bangla Typing:",
                        fontSize = 11.5.sp,
                        color = MonaymDarkNavy,
                        fontWeight = FontWeight.SemiBold
                    )
                    Surface(
                        onClick = {
                            RidmikKeyboardManager.promptSwitchOrInstall(context, isBangla)
                        },
                        shape = RoundedCornerShape(12.dp),
                        color = MonaymPrimary.copy(alpha = 0.1f),
                        modifier = Modifier.testTag("ridmik_keyboard_quick_chip")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Keyboard,
                                contentDescription = null,
                                tint = MonaymPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "রিদ্মিক কীবোর্ড (Ridmik)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MonaymPrimaryDark
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, bottom = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                OutlinedTextField(
                    value = userQueryInput,
                    onValueChange = { userQueryInput = it },
                    placeholder = {
                        Text(
                            text = if (isBangla) "যেকোনো ফাইল, খতিয়ান বা দলিলের প্রশ্ন লিখুন..." else "Ask any question about land files or deed...",
                            fontSize = 14.sp
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_query_input_field"),
                    shape = RoundedCornerShape(24.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MonaymPrimary,
                        unfocusedBorderColor = Color.LightGray
                    ),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Send Button
                Button(
                    onClick = { sendAiPrompt(userQueryInput) },
                    enabled = userQueryInput.isNotBlank() && !isThinking,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("ai_send_query_btn"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
}

@Composable
private fun AiMessageBubble(
    message: AiMessage,
    isBangla: Boolean,
    onNavigateToFile: (Int) -> Unit,
    onCallHotline: () -> Unit,
    onCopyText: () -> Unit
) {
    val isUser = message.isUser

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Surface(
                shape = CircleShape,
                color = MonaymPrimary,
                modifier = Modifier
                    .size(34.dp)
                    .padding(top = 2.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = null,
                        tint = MonaymGold,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier.widthIn(max = 310.dp),
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
        ) {
            Surface(
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomStart = if (isUser) 16.dp else 4.dp,
                    bottomEnd = if (isUser) 4.dp else 16.dp
                ),
                color = if (isUser) MonaymPrimary else Color.White,
                shadowElevation = if (isUser) 1.dp else 2.dp,
                border = if (isUser) null else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = message.text,
                        fontSize = 15.sp,
                        lineHeight = 22.sp,
                        color = if (isUser) Color.White else MonaymDarkNavy,
                        fontWeight = if (isUser) FontWeight.Medium else FontWeight.Normal
                    )

                    // Suggested Deep Action Buttons (If applicable)
                    if (!isUser && message.suggestedAction != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        when (message.suggestedAction) {
                            "REGISTRATION" -> {
                                Button(
                                    onClick = { onNavigateToFile(8) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MonaymGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isBangla) "দলিল ক্যালকুলেটর খুলুন" else "Open Registration Calculator", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            "PAYMENT" -> {
                                Button(
                                    onClick = { onNavigateToFile(4) },
                                    colors = ButtonDefaults.buttonColors(containerColor = BkashColor),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isBangla) "অনলাইন পেমেন্ট গেটওয়েতে যান" else "Open Payment Gateway", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            "RECORDS" -> {
                                Button(
                                    onClick = { onNavigateToFile(1) },
                                    colors = ButtonDefaults.buttonColors(containerColor = MonaymPrimary),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Folder, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isBangla) "সংরক্ষিত খতিয়ান ও রেকর্ড দেখুন" else "View Land Records", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            "CALL" -> {
                                Button(
                                    onClick = onCallHotline,
                                    colors = ButtonDefaults.buttonColors(containerColor = MonaymGreen),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("01976444504 " + if (isBangla) "হটলাইনে কল করুন" else "Call Helpline", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    // Copy action on AI bubble
                    if (!isUser) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                onClick = onCopyText,
                                shape = RoundedCornerShape(6.dp),
                                color = LightBackground
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = null, tint = MonaymPrimaryDark, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(if (isBangla) "কপি" else "Copy", fontSize = 10.5.sp, color = MonaymPrimaryDark, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = message.time,
                fontSize = 11.sp,
                color = MonaymPrimaryDark,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 3.dp, start = 4.dp, end = 4.dp)
            )
        }
    }
}

/**
 * Intelligent Bangladesh Land Law and Service Knowledge Engine
 */
private fun generateIntelligentLandResponse(query: String, isBangla: Boolean): Pair<String, String?> {
    val q = query.lowercase()

    return when {
        q.contains("খতিয়ান") || q.contains("পর্চা") || q.contains("khatian") || q.contains("porcha") -> {
            val text = if (isBangla)
                "📄 খতিয়ান ও ই-পর্চা যাচাইয়ের নিয়মাবলী:\n\n১. বাংলাদেশে প্রচলিত খতিয়ান সমূহ: সিএস (CS), এসএ (SA), আরএস (RS), এবং সর্বশেষ সিটি/বিএস (BS) খতিয়ান।\n২. খতিয়ান যাচাইয়ের জন্য জেলা, উপজেলা ও মৌজা নম্বর জেনে ভূমি মন্ত্রণালয়ের eporcha.gov.bd পোর্টালে সার্চ করুন।\n৩. আমাদের অ্যাপের 'খতিয়ান' ফাইলে গিয়ে আপনি তাৎক্ষণিকভাবে সংরক্ষিত খতিয়ান ও দাগ সার্চ করতে পারবেন।"
            else
                "📄 Khatian & Porcha Verification Guide:\n\n1. Khatian types in Bangladesh: CS, SA, RS, and latest BS/City survey.\n2. Search on the official portal using District, Upazila & Mouza.\n3. You can also view and manage all your stored Khatians in our app's Land Records file."
            Pair(text, "RECORDS")
        }

        q.contains("রেজিস্ট্রেশন") || q.contains("ফি") || q.contains("দলিল") || q.contains("deed") || q.contains("fee") -> {
            val text = if (isBangla)
                "⚖️ দলিল রেজিস্ট্রেশন ফি ও ট্যাক্স বিধান:\n\n১. সাফ-কবলা দলিল: রেজিস্ট্রেশন ফি ১%, স্ট্যাম্প শুল্ক ১.৫%, স্থানীয় সরকার কর ২-৩% এবং উৎস কর (AIT) ১.৫-৩%।\n২. হেবা (রক্তের আত্মীয়ের দান): সরকারি ফি মাত্র ১০০ টাকা + স্ট্যাম্প ২০০ টাকা।\n৩. বণ্টননামা: ফিক্সড নামমাত্র সরকারি ফি।\n\nআপনি আমাদের 'দলিল রেজিস্ট্রেশন ক্যালকুলেটর' ফাইলে জমির মূল্য লিখলেই সম্পূর্ণ হিসাব দেখতে পারবেন।"
            else
                "⚖️ Deed Registration & Govt Fee Slabs:\n\n1. Saf-Kabla (Sale): 1% Registration, 1.5% Stamp, 2-3% Local Tax, and Source Tax.\n2. Heba (Blood Relative): Nominal ৳ 100 Reg fee + ৳ 200 Stamp duty.\n3. Partition Deed: Flat nominal statutory charge.\n\nUse our built-in Registration Fee Calculator to see the exact breakdown instantly."
            Pair(text, "REGISTRATION")
        }

        q.contains("নামজারি") || q.contains("খারিজ") || q.contains("mutation") || q.contains("namjari") -> {
            val text = if (isBangla)
                "🏛️ ই-নামজারি (Mutation) করার প্রয়োজনীয় ধাপ:\n\n১. দলিল রেজিস্ট্রির ৩০ দিনের মধ্যে সহকারী কমিশনার (ভূমি) অফিসে আবেদন করতে হবে।\n২. আবেদন ফি: ২০ টাকা কোর্ট ফি ও ৫০ টাকা নোটিশ জারি ফি।\n৩. মঞ্জুরের পর ডিসিআর (DCR) ফি ১,১০০ টাকা দিয়ে অনলাইনে কিউআর কোডযুক্ত খতিয়ান সংগ্রহ করা যায়।\n৪. কোনো জটিলতা থাকলে সরাসরি আমাদের প্রতিষ্ঠানে যোগাযোগ করুন।"
            else
                "🏛️ Step-by-Step e-Namjari Mutation Guide:\n\n1. Apply online via mutation.land.gov.bd within 30 days of deed execution.\n2. Application fee: ৳ 20 court fee + ৳ 50 notice fee.\n3. Upon approval, pay ৳ 1,100 DCR fee to download the QR-verified e-Namjari Khatian.\n4. Call our hotline for full assistance."
            Pair(text, "CALL")
        }

        q.contains("খাজনা") || q.contains("ট্যাক্স") || q.contains("কর") || q.contains("tax") || q.contains("khajna") -> {
            val text = if (isBangla)
                "💰 ভূমি উন্নয়ন কর (খাজনা) পরিশোধ নিয়ম:\n\n১. এখন সম্পূর্ণভাবে ldtax.gov.bd এবং বিকাশ/নগদের মাধ্যমে অনলাইনে খাজনা পরিশোধ করা যায়।\n২. কৃষি জমি ২৫ বিঘা পর্যন্ত সাধারণত করমুক্ত (তবে দাখিলা নিতে হয়)।\n৩. অকৃষি বাণিজ্যিক ও আবাসিক জমির খাজনা প্রতি শতকের মৌজা ভিত্তিক হারে নির্ধারিত হয়।\n৪. আমাদের পেমেন্ট ফাইলে গিয়ে সরাসরি সেন্টমানি করে দাখিলা রসিদ নিতে পারেন।"
            else
                "💰 Land Development Tax Payment:\n\n1. Pay seamlessly online via ldtax.gov.bd and MFS wallets.\n2. Agricultural land up to 25 Bighas is exempt from base taxes.\n3. Non-agricultural land tax is assessed per decimal based on Mouza valuation.\n4. Visit our Payment Gateway file to pay land fees."
            Pair(text, "PAYMENT")
        }

        q.contains("পরিমাপ") || q.contains("শতাংশ") || q.contains("কাঠা") || q.contains("বিঘা") || q.contains("measure") -> {
            val text = if (isBangla)
                "📐 জমির পরিমাপ ও রূপান্তর সূত্র:\n\n• ১ শতাংশ (ডেসিমাল) = ৪৩৫.৬ বর্গফুট (sq ft)\n• ১ কাঠা = ১.৬৫ শতাংশ = ৭২০ বর্গফুট\n• ১ বিঘা = ২০ কাঠা = ৩৩ শতাংশ\n• ১ একর = ১০০ শতাংশ = ৩ বিঘা ৮ ছটাক\n• ১ হেক্টর = ২.৪৭ একর = ২৪৭ শতাংশ"
            else
                "📐 Land Measurement Standards:\n\n• 1 Decimal = 435.6 Square Feet\n• 1 Katha = 1.65 Decimals = 720 Sq Ft\n• 1 Bigha = 20 Kathas = 33 Decimals\n• 1 Acre = 100 Decimals = 3 Bighas 8 Chhatak"
            Pair(text, null)
        }

        q.contains("জাল") || q.contains("প্রতারণা") || q.contains("fraud") || q.contains("fake") -> {
            val text = if (isBangla)
                "⚠️ জাল দলিল ও জমি ক্রয়ের পূর্বে ৫টি সতর্কতা:\n\n১. বিক্রেতার সর্বশেষ হালনাগাদ ই-নামজারি ও ডিআরসি (DCR) খতিয়ান যাচাই করুন।\n২. বিগত ২৫ বছরের বায়া দলিলের ধারাবাহিকতা মিলিয়ে দেখুন।\n৩. সরেজমিনে জমিতে গিয়ে দখল ও সীমানা নিশ্চিত হোন।\n৪. সাব-রেজিস্ট্রি অফিসে গিয়ে তল্লাশি (Search) দিয়ে দলিলের সত্যতা নিশ্চিত করুন।\n৫. সন্দেহ হলে M.S MONAYM এন্টারপ্রাইজের আইনি টিমের পরামর্শ নিন।"
            else
                "⚠️ 5 Safety Checks Before Buying Land:\n\n1. Verify updated e-Namjari Khatian and DCR in seller's name.\n2. Trace 25-year title chain of prior parent deeds (Baya Dalil).\n3. Physically inspect ground possession and boundaries.\n4. Conduct title search at local Sub-Registry Office.\n5. Consult M.S MONAYM legal experts."
            Pair(text, "CALL")
        }

        else -> {
            val text = if (isBangla)
                "ধন্যবাদ আপনার প্রশ্নের জন্য। M.S MONAYM ENTERPRISE-এর এই অ্যাপের মাধ্যমে আপনি খতিয়ান দেখা, ডিজিটাল নথি সংরক্ষণ, জিপিএস ম্যাপ দেখা, দলিল রেজিস্ট্রেশন ফি হিসাব ও খাজনা পেমেন্ট করতে পারবেন।\n\nসরাসরি যেকোনো জরুরি প্রয়োজনে আমাদের অফিশিয়াল সিম ও WhatsApp নম্বরে যোগাযোগ করুন: 01976444504।"
            else
                "Thank you for reaching out. M.S MONAYM ENTERPRISE offers full digital land management—Khatian records, deed fee calculator, GPS maps, and tax payments.\n\nFor direct advisory, call or WhatsApp our official hotline: 01976444504."
            Pair(text, "CALL")
        }
    }
}

package com.msmonaym.land.ui.registration

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
import com.msmonaym.land.data.LandDataRepository
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    viewModel: LandViewModel? = null,
    isBangla: Boolean = true,
    onBack: () -> Unit,
    onOpenPaymentDialog: ((Double, String) -> Unit)? = null
) {
    // Intercept hardware and gesture back navigation
    BackHandler {
        onBack()
    }

    val context = LocalContext.current
    val landRecords = viewModel?.allLandRecords?.collectAsState()?.value ?: emptyList()

    // Deed types
    val deedTypesBangla = listOf("সাফ-কবলা", "হেবা (রক্তের সম্পর্ক)", "বণ্টননামা", "দানপত্র", "বায়নানামা")
    val deedTypesEnglish = listOf("Sale Deed", "Heba (Blood Relation)", "Partition Deed", "Gift Deed", "Agreement to Sale")
    var selectedDeedTypeIndex by remember { mutableIntStateOf(0) }

    // Area types
    val areaTypesBangla = listOf("সিটি কর্পোরেশন", "পৌরসভা এলাকা", "ইউনিয়ন পরিষদ / গ্রাম")
    val areaTypesEnglish = listOf("City Corporation", "Municipality", "Union Parishad")
    var selectedAreaTypeIndex by remember { mutableIntStateOf(1) } // Default municipality

    // Price input
    var landPriceInput by remember { mutableStateOf("1000000") } // Default 10 Lac BDT
    var selectedRecordKhatian by remember { mutableStateOf<String?>(null) }

    val parsedPrice = landPriceInput.toDoubleOrNull() ?: 0.0
    val result = remember(parsedPrice, selectedDeedTypeIndex, selectedAreaTypeIndex) {
        LandDataRepository.calculateRegistrationFee(
            price = parsedPrice,
            deedTypeIndex = selectedDeedTypeIndex,
            areaTypeIndex = selectedAreaTypeIndex
        )
    }

    val bdtFormatter = remember {
        if (isBangla) {
            NumberFormat.getNumberInstance(Locale("bn", "BD"))
        } else {
            NumberFormat.getNumberInstance(Locale.US)
        }
    }

    // Interactive checklist states for required documents
    val checkedDocuments = remember { mutableStateListOf<Int>() }

    val requiredDocs = remember(isBangla) {
        if (isBangla) {
            listOf(
                "ক্রেতা ও বিক্রেতার জাতীয় পরিচয়পত্র (NID) ও পাসপোর্ট সাইজ রঙিন ছবি",
                "সর্বশেষ সিএস, এসএ, আরএস ও বিএস খতিয়ানের মূল বা সার্টিফাইড কপি",
                "বিক্রেতার নামে হালনাগাদ ই-নামজারি ও জমাভাগ খতিয়ান এবং ডিসিআর (DCR)",
                "হাল সন পর্যন্ত পরিশোধিত ভূমি উন্নয়ন কর (খাজনা) দাখিলা রশিদ",
                "বিক্রেতা ওয়ারিশ সূত্রে মালিক হলে ওয়ারিশান সনদ ও পারিবারিক ফরায়েজ নামা",
                "পূর্ববর্তী ২৫ বছরের বায়া দলিল ও মূল দলিলের কপি",
                "বায়না দলিল (যদি পূর্বে রেজিস্ট্রি সম্পন্ন হয়ে থাকে)",
                "ক্রেতা-বিক্রেতার ই-টিন (e-TIN) সার্টিফিকেট ও প্রয়োজনীয় হলফনামা"
            )
        } else {
            listOf(
                "Buyer and seller National ID (NID) and passport size photos",
                "Latest certified copies of CS, SA, RS, and BS Khatian records",
                "Updated e-Namjari Mutation Khatian and DCR in seller's name",
                "Up-to-date Land Development Tax (Khajna) payment receipt",
                "Succession Certificate and Farayez deed (if inherited land)",
                "Previous 25 years via/parent deeds and certified copies",
                "Bayna deed (if registered agreement was executed prior)",
                "Buyer-Seller e-TIN certificate, affidavit and stamp papers"
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (isBangla) "দলিল রেজিস্ট্রি ও ফি ক্যালকুলেটর" else "Deed Registration Calculator",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp,
                            color = Color.White
                        )
                        Text(
                            text = "M.S MONAYM ENTERPRISE • ল্যান্ড সার্ভিস",
                            fontSize = 11.sp,
                            color = MonaymGold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("reg_screen_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = if (isBangla) "ফিরে যান" else "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Language switcher button inside Registration Screen
                    if (viewModel != null) {
                        Surface(
                            onClick = { viewModel.toggleLanguage() },
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .testTag("reg_language_toggle_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Language,
                                    contentDescription = "Language",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isBangla) "বাংলা" else "ENG",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            }
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
                .testTag("registration_screen_scroll"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Enterprise Banner Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymDarkNavy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MonaymGold.copy(alpha = 0.2f),
                                    modifier = Modifier.size(34.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Calculate,
                                            contentDescription = null,
                                            tint = MonaymGold,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = if (isBangla) "বাংলাদেশ সাব-রেজিস্ট্রি ফি গণক" else "Sub-Registry Fee Engine",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (isBangla) "নিবন্ধন অধিদপ্তর ও রাজস্ব বোর্ড বিধিমালা অনুসারে" else "As per Govt Land Registration Rules",
                                        fontSize = 11.sp,
                                        color = Color.White.copy(alpha = 0.75f)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MonaymGreen.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = if (isBangla) "হালনাগাদ ২০২৬" else "Updated 2026",
                                    fontSize = 10.sp,
                                    color = MonaymGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Saved Land Records Selector (if available)
            if (landRecords.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Assignment,
                                    contentDescription = null,
                                    tint = MonaymPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBangla) "সংরক্ষিত জমি নির্বাচন করে হিসাব করুন:" else "Or select from your saved lands:",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MonaymPrimaryDark
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(landRecords) { record ->
                                    val isSelected = selectedRecordKhatian == record.khatianNo
                                    Surface(
                                        onClick = {
                                            selectedRecordKhatian = if (isSelected) null else record.khatianNo
                                            if (!isSelected) {
                                                // Generate estimated valuation based on area or default
                                                val areaNum = record.area.filter { it.isDigit() }.toDoubleOrNull() ?: 10.0
                                                val estimatedVal = (areaNum * 150000).toLong().toString()
                                                landPriceInput = estimatedVal
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) MonaymPrimary else LightBackground,
                                        border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                                        modifier = Modifier.testTag("land_chip_${record.khatianNo}")
                                    ) {
                                        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                            Text(
                                                text = "${if (isBangla) "খতিয়ান" else "Khatian"}: ${record.khatianNo}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                color = if (isSelected) Color.White else Color.Black
                                            )
                                            Text(
                                                text = "${record.mouza} (${record.area})",
                                                fontSize = 11.sp,
                                                color = if (isSelected) MonaymGold else Color.DarkGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Deed Type & Jurisdiction Selection Card
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
                        Text(
                            text = if (isBangla) "১. দলিলের ধরণ বাছাই করুন:" else "1. Select Deed Category:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MonaymDarkNavy
                        )

                        // Deed Types Horizontal Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            deedTypesBangla.forEachIndexed { index, nameBn ->
                                val label = if (isBangla) nameBn else deedTypesEnglish[index]
                                val selected = selectedDeedTypeIndex == index
                                FilterChip(
                                    selected = selected,
                                    onClick = { selectedDeedTypeIndex = index },
                                    label = { Text(label, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MonaymPrimary,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("deed_type_chip_$index")
                                )
                            }
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                        Text(
                            text = if (isBangla) "২. জমির ভৌগোলিক অবস্থান / এলাকা:" else "2. Property Location Type:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MonaymDarkNavy
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            areaTypesBangla.forEachIndexed { index, nameBn ->
                                val label = if (isBangla) nameBn else areaTypesEnglish[index]
                                val selected = selectedAreaTypeIndex == index
                                FilterChip(
                                    selected = selected,
                                    onClick = { selectedAreaTypeIndex = index },
                                    label = { Text(label, fontSize = 12.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MonaymGreen,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("area_type_chip_$index")
                                )
                            }
                        }
                    }
                }
            }

            // Land Valuation Input Card
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
                        Text(
                            text = if (isBangla) "৩. দলিলের উল্লেখিত বা সরকারি বাজার মূল্য:" else "3. Declared or Sub-Registry Market Price:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MonaymDarkNavy
                        )

                        OutlinedTextField(
                            value = landPriceInput,
                            onValueChange = { input ->
                                landPriceInput = input.filter { it.isDigit() }
                            },
                            label = {
                                Text(if (isBangla) "জমির মূল্য (টাকায়)" else "Land Price (in BDT)")
                            },
                            suffix = {
                                Text(if (isBangla) "টাকা" else "BDT", fontWeight = FontWeight.Bold, color = MonaymPrimary)
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AttachMoney,
                                    contentDescription = null,
                                    tint = MonaymPrimary
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("land_price_input_field"),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MonaymPrimary,
                                focusedLabelColor = MonaymPrimary
                            ),
                            singleLine = true
                        )

                        // Quick Price Increment Chips
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val presetValues = listOf(
                                Pair(500000L, if (isBangla) "৫ লক্ষ" else "500K"),
                                Pair(1000000L, if (isBangla) "১০ লক্ষ" else "1 Million"),
                                Pair(2500000L, if (isBangla) "২৫ লক্ষ" else "2.5M"),
                                Pair(5000000L, if (isBangla) "৫০ লক্ষ" else "5M"),
                                Pair(10000000L, if (isBangla) "১ কোটি" else "10M")
                            )

                            presetValues.forEach { (amount, label) ->
                                Surface(
                                    onClick = { landPriceInput = amount.toString() },
                                    shape = RoundedCornerShape(16.dp),
                                    color = LightBackground,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MonaymPrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Calculation Breakdown Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fee_breakdown_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ReceiptLong,
                                    contentDescription = null,
                                    tint = MonaymPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isBangla) "সরকারি ফি হিসাবের ব্রেকডাউন:" else "Govt Fee Calculation Breakdown:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = MonaymDarkNavy
                                )
                            }

                            // Copy button
                            IconButton(
                                onClick = {
                                    val summaryText = buildString {
                                        append("🏛️ M.S MONAYM ENTERPRISE - দলিল রেজিস্ট্রেশন ফি হিসাব\n")
                                        append("দলিলের ধরণ: ${result.deedType}\n")
                                        append("এলাকা: ${result.areaType}\n")
                                        append("জমির মূল্য: ৳ ${bdtFormatter.format(parsedPrice.toLong())}\n")
                                        append("-----------------------------------\n")
                                        append("১. রেজিস্ট্রেশন ফি: ৳ ${bdtFormatter.format(result.registrationFee.toLong())}\n")
                                        append("২. স্ট্যাম্প শুল্ক: ৳ ${bdtFormatter.format(result.stampDuty.toLong())}\n")
                                        append("৩. স্থানীয় সরকার কর: ৳ ${bdtFormatter.format(result.localGovtTax.toLong())}\n")
                                        append("৪. উৎস কর (AIT): ৳ ${bdtFormatter.format(result.sourceTax.toLong())}\n")
                                        append("৫. অন্যান্য সরকারি ফি: ৳ ${bdtFormatter.format(result.otherFees.toLong())}\n")
                                        append("-----------------------------------\n")
                                        append("সর্বমোট প্রদেয় সরকারি ফি: ৳ ${bdtFormatter.format(result.totalFee.toLong())}\n")
                                        append("সহায়তায় (সিম ও WhatsApp): 01976444504")
                                    }
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Registration Fee", summaryText)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(
                                        context,
                                        if (isBangla) "হিসাব বিবরণী কপি করা হয়েছে!" else "Calculation copied to clipboard!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                },
                                modifier = Modifier.testTag("copy_reg_fee_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy Summary",
                                    tint = MonaymPrimary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MonaymPrimary.copy(alpha = 0.05f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FeeLineItem(
                                    title = if (isBangla) "১. রেজিস্ট্রেশন ফি (Registration Fee):" else "1. Registration Fee:",
                                    percent = if (result.registrationPercent > 0) "${result.registrationPercent}%" else if (isBangla) "ফিক্সড" else "Fixed",
                                    amount = "৳ ${bdtFormatter.format(result.registrationFee.toLong())}"
                                )

                                FeeLineItem(
                                    title = if (isBangla) "২. স্ট্যাম্প শুল্ক (Stamp Duty):" else "2. Stamp Duty:",
                                    percent = if (result.stampDutyPercent > 0) "${result.stampDutyPercent}%" else if (isBangla) "ফিক্সড" else "Fixed",
                                    amount = "৳ ${bdtFormatter.format(result.stampDuty.toLong())}"
                                )

                                FeeLineItem(
                                    title = if (isBangla) "৩. স্থানীয় সরকার কর (Local Govt Tax):" else "3. Local Govt Tax:",
                                    percent = if (result.localGovtTaxPercent > 0) "${result.localGovtTaxPercent}%" else "০%",
                                    amount = "৳ ${bdtFormatter.format(result.localGovtTax.toLong())}"
                                )

                                FeeLineItem(
                                    title = if (isBangla) "৪. উৎস কর / AIT (Source Tax):" else "4. Source Tax (AIT):",
                                    percent = if (result.sourceTaxPercent > 0) "${result.sourceTaxPercent}%" else "০%",
                                    amount = "৳ ${bdtFormatter.format(result.sourceTax.toLong())}"
                                )

                                FeeLineItem(
                                    title = if (isBangla) "৫. অন্যান্য ফি (ই-ফি, এন-ফি, কোর্ট ফি, হলফনামা):" else "5. Other Govt Fees (E-Fee, N-Fee):",
                                    percent = if (isBangla) "সরকারি রসিদ" else "Standard",
                                    amount = "৳ ${bdtFormatter.format(result.otherFees.toLong())}"
                                )

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = MonaymPrimary.copy(alpha = 0.2f)
                                )

                                // Total Fee Highlight Box
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MonaymPrimaryDark,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 14.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text(
                                                text = if (isBangla) "সর্বমোট সরকারি ফি:" else "Total Govt Registration Fee:",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color.White
                                            )
                                            Text(
                                                text = "${result.deedType} • ${result.areaType}",
                                                fontSize = 10.sp,
                                                color = MonaymGold
                                            )
                                        }

                                        Text(
                                            text = "৳ ${bdtFormatter.format(result.totalFee.toLong())}",
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 18.sp,
                                            color = MonaymGold
                                        )
                                    }
                                }
                            }
                        }

                        // Action Buttons: Pay via Gateway & Copy
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    if (onOpenPaymentDialog != null) {
                                        onOpenPaymentDialog(result.totalFee, "দলিল রেজিস্ট্রেশন ফি - ${result.deedType}")
                                    } else {
                                        Toast.makeText(
                                            context,
                                            if (isBangla) "পেমেন্ট উইন্ডো খোলা হচ্ছে..." else "Opening Payment Dialog...",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("pay_reg_fee_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = BkashColor),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Payment,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isBangla) "ফি পরিশোধ করুন" else "Pay Registration Fee",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = Color.White
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    val summary = "M.S MONAYM: দলিল ফি ৳ ${bdtFormatter.format(result.totalFee.toLong())} (${result.deedType})"
                                    val sendIntent: Intent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, summary)
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("share_reg_fee_btn")
                            ) {
                                Text(
                                    text = if (isBangla) "শেয়ার" else "Share",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MonaymPrimary
                                )
                            }
                        }
                    }
                }
            }

            // Interactive Document Checklist Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("required_documents_checklist_card"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = MonaymPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = if (isBangla) "দলিল রেজিস্ট্রেশনে প্রয়োজনীয় ডকুমেন্টস" else "Required Documents Checklist",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = MonaymDarkNavy
                                    )
                                    Text(
                                        text = if (isBangla) "যেগুলো প্রস্তুত হয়েছে টিক দিন (${checkedDocuments.size}/${requiredDocs.size})"
                                        else "Mark prepared documents (${checkedDocuments.size}/${requiredDocs.size})",
                                        fontSize = 11.sp,
                                        color = MonaymGreen
                                    )
                                }
                            }
                        }

                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                        requiredDocs.forEachIndexed { index, docText ->
                            val isChecked = checkedDocuments.contains(index)
                            Surface(
                                onClick = {
                                    if (isChecked) {
                                        checkedDocuments.remove(index)
                                    } else {
                                        checkedDocuments.add(index)
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                color = if (isChecked) MonaymGreen.copy(alpha = 0.08f) else LightBackground,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("doc_checkbox_row_$index")
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                            if (checked) checkedDocuments.add(index) else checkedDocuments.remove(index)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MonaymGreen,
                                            checkmarkColor = Color.White
                                        ),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = docText,
                                        fontSize = 12.sp,
                                        color = if (isChecked) MonaymDarkNavy else Color.DarkGray,
                                        lineHeight = 17.sp,
                                        fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Legal Advice & Sub-Registry Guidelines Card
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
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MonaymGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isBangla) "রেজিস্ট্রি বিষয়ক গুরুত্বপূর্ণ আইনি তথ্য:" else "Important Legal & Registration Rules:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MonaymDarkNavy
                            )
                        }

                        val legalTips = if (isBangla) listOf(
                            "দলিল রেজিস্ট্রির পর অবশ্যই সাব-রেজিস্ট্রার কর্তৃক দেওয়া প্রাপ্তি স্বীকার রসিদ (৫২ ধারা রশিদ) সংগ্রহ করুন।",
                            "রেজিস্ট্রি সম্পন্ন হওয়ার ৩০ কার্যদিবসের মধ্যে সহকারী কমিশনার (ভূমি) অফিসে ই-নামজারির আবেদন করতে হবে।",
                            "দলিলে উল্লিখিত টাকার পরিমাণ অবশ্যই ব্যাংক পে-অর্ডার বা এ-চালানের মাধ্যমে সরকারি কোষাগারে জমা হতে হবে।",
                            "সাব-রেজিস্ট্রি অফিসে উভয় পক্ষের আঙুলের ছাপ (Biometric) ও ডিজিটাল ছবি গ্রহণ বাধ্যতামূলক।"
                        ) else listOf(
                            "Always collect Section 52 delivery token after deed execution in Sub-Registry office.",
                            "Apply for e-Namjari Mutation within 30 working days from AC Land office.",
                            "Govt fees must be paid through bank pay order or Automated e-Challan.",
                            "Biometric fingerprinting and digital photograph of both parties are mandatory."
                        )

                        legalTips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 3.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("📌 ", fontSize = 12.sp)
                                Text(
                                    text = tip,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }

            // Helpline & Direct Contact Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MonaymPrimaryDark),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = if (isBangla) "দলিল ও জমি রেজিস্ট্রেশন সরাসরি সহায়তা" else "Deed & Land Direct Support",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = if (isBangla) "প্রতিষ্ঠানের অফিসিয়াল সিম ও WhatsApp নাম্বার" else "Enterprise Official SIM & WhatsApp",
                                    fontSize = 12.sp,
                                    color = MonaymGold
                                )
                                Text(
                                    text = "01976444504",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color.White,
                                    letterSpacing = 1.sp
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MonaymGreen.copy(alpha = 0.25f)
                            ) {
                                Text(
                                    text = if (isBangla) "সরাসরি সাপোর্ট" else "Live Support",
                                    fontSize = 10.sp,
                                    color = MonaymGreen,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // Action Buttons: SIM Call & WhatsApp
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Direct SIM Call Button
                            Button(
                                onClick = {
                                    val callIntent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse("tel:01976444504")
                                    }
                                    context.startActivity(callIntent)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MonaymGreen),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("hotline_call_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "SIM Call",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isBangla) "সিম কল" else "SIM Call",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }

                            // Direct WhatsApp Chat Button
                            Button(
                                onClick = {
                                    val waIntent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://wa.me/8801976444504")
                                    }
                                    try {
                                        context.startActivity(waIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            if (isBangla) "WhatsApp খুলতে ব্যর্থ হয়েছে। নম্বর: 01976444504"
                                            else "Cannot open WhatsApp. Number: 01976444504",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("hotline_whatsapp_btn")
                            ) {
                                Text(
                                    text = "💬 " + if (isBangla) "WhatsApp" else "WhatsApp",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Spacing
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FeeLineItem(
    title: String,
    percent: String,
    amount: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontSize = 12.sp, color = Color.DarkGray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = RoundedCornerShape(6.dp),
                color = MonaymPrimary.copy(alpha = 0.1f),
                modifier = Modifier.padding(end = 6.dp)
            ) {
                Text(
                    text = percent,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MonaymPrimary,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Text(
                text = amount,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MonaymDarkNavy
            )
        }
    }
}

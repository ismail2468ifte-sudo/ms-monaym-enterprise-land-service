package com.msmonaym.land.data

data class RegistrationFeeResult(
    val registrationFee: Double,
    val stampDuty: Double,
    val localGovtTax: Double,
    val sourceTax: Double,
    val otherFees: Double = 1200.0,
    val totalFee: Double,
    val deedType: String = "সাফ-কবলা",
    val areaType: String = "পৌরসভা",
    val registrationPercent: Double = 1.0,
    val stampDutyPercent: Double = 1.5,
    val localGovtTaxPercent: Double = 2.0,
    val sourceTaxPercent: Double = 2.0
)

object LandDataRepository {
    /**
     * Calculates Bangladesh Land Registration Fees with Deed Type and Location variations:
     * 1. সাফ-কবলা (Sale Deed)
     * 2. হেবা (Heba / Blood Relation Gift) - Fixed nominal fees
     * 3. বণ্টননামা (Partition Deed) - Fixed stamp + nominal fee
     * 4. দানপত্র (Gift to Others)
     * 5. বায়নানামা (Agreement to Sell)
     */
    fun calculateRegistrationFee(
        price: Double,
        deedTypeIndex: Int = 0, // 0: Sale (সাফ-কবলা), 1: Heba (হেবা), 2: Partition (বণ্টননামা), 3: Gift (দানপত্র), 4: Agreement (বায়নানামা)
        areaTypeIndex: Int = 1  // 0: City Corp, 1: Pourashava (Municipality), 2: Union Parishad
    ): RegistrationFeeResult {
        if (price <= 0.0) {
            return RegistrationFeeResult(
                registrationFee = 0.0,
                stampDuty = 0.0,
                localGovtTax = 0.0,
                sourceTax = 0.0,
                otherFees = 0.0,
                totalFee = 0.0
            )
        }

        when (deedTypeIndex) {
            1 -> {
                // Heba Deed (হেবা দলিল - রক্তের আত্মীয়ের মধ্যে হস্তান্তর)
                val regFee = 100.0
                val stamp = 200.0
                val localTax = 0.0
                val sourceTax = 0.0
                val other = 750.0
                return RegistrationFeeResult(
                    registrationFee = regFee,
                    stampDuty = stamp,
                    localGovtTax = localTax,
                    sourceTax = sourceTax,
                    otherFees = other,
                    totalFee = regFee + stamp + localTax + sourceTax + other,
                    deedType = "হেবা (রক্তের সম্পর্ক)",
                    areaType = "সকল এলাকা",
                    registrationPercent = 0.0,
                    stampDutyPercent = 0.0,
                    localGovtTaxPercent = 0.0,
                    sourceTaxPercent = 0.0
                )
            }
            2 -> {
                // Partition Deed (আপস বণ্টননামা দলিল)
                val regFee = 500.0
                val stamp = 100.0
                val localTax = 0.0
                val sourceTax = 0.0
                val other = 800.0
                return RegistrationFeeResult(
                    registrationFee = regFee,
                    stampDuty = stamp,
                    localGovtTax = localTax,
                    sourceTax = sourceTax,
                    otherFees = other,
                    totalFee = regFee + stamp + localTax + sourceTax + other,
                    deedType = "বণ্টননামা",
                    areaType = "সকল এলাকা",
                    registrationPercent = 0.0,
                    stampDutyPercent = 0.0,
                    localGovtTaxPercent = 0.0,
                    sourceTaxPercent = 0.0
                )
            }
            3 -> {
                // Gift Deed (সাধারণ দানপত্র দলিল)
                val regPercent = 1.0
                val stampPercent = 1.5
                val localPercent = when (areaTypeIndex) {
                    0 -> 3.0 // City Corporation
                    1 -> 2.0 // Municipality
                    else -> 1.5 // Union Parishad
                }
                val sourcePercent = 0.0 // Gift generally exempt from source tax if qualified
                val regFee = price * (regPercent / 100.0)
                val stamp = price * (stampPercent / 100.0)
                val localTax = price * (localPercent / 100.0)
                val other = 1200.0
                return RegistrationFeeResult(
                    registrationFee = regFee,
                    stampDuty = stamp,
                    localGovtTax = localTax,
                    sourceTax = 0.0,
                    otherFees = other,
                    totalFee = regFee + stamp + localTax + other,
                    deedType = "দানপত্র",
                    areaType = if (areaTypeIndex == 0) "সিটি কর্পোরেশন" else if (areaTypeIndex == 1) "পৌরসভা" else "ইউনিয়ন পরিষদ",
                    registrationPercent = regPercent,
                    stampDutyPercent = stampPercent,
                    localGovtTaxPercent = localPercent,
                    sourceTaxPercent = 0.0
                )
            }
            4 -> {
                // Bayna Deed (বায়না দলিল)
                val stamp = if (price <= 500000) 500.0 else if (price <= 5000000) 1000.0 else 2000.0
                val regFee = 500.0
                val other = 600.0
                return RegistrationFeeResult(
                    registrationFee = regFee,
                    stampDuty = stamp,
                    localGovtTax = 0.0,
                    sourceTax = 0.0,
                    otherFees = other,
                    totalFee = regFee + stamp + other,
                    deedType = "বায়না দলিল",
                    areaType = "সকল এলাকা",
                    registrationPercent = 0.0,
                    stampDutyPercent = 0.0,
                    localGovtTaxPercent = 0.0,
                    sourceTaxPercent = 0.0
                )
            }
            else -> {
                // Standard Saf Kabla (সাফ-কবলা বিক্রয় দলিল)
                val regPercent = 1.0
                val stampPercent = 1.5
                val localPercent = when (areaTypeIndex) {
                    0 -> 3.0 // City Corp (৩%)
                    1 -> 2.0 // Pourashava (২%)
                    else -> 1.5 // Union Parishad (১.৫%)
                }
                val sourcePercent = when (areaTypeIndex) {
                    0 -> 3.0 // City Corp (৩%)
                    1 -> 2.0 // Pourashava (২%)
                    else -> 1.5 // Union Parishad (১.৫%)
                }
                val regFee = price * (regPercent / 100.0)
                val stamp = price * (stampPercent / 100.0)
                val localTax = price * (localPercent / 100.0)
                val sourceTax = price * (sourcePercent / 100.0)
                val other = 1200.0
                val total = regFee + stamp + localTax + sourceTax + other

                return RegistrationFeeResult(
                    registrationFee = regFee,
                    stampDuty = stamp,
                    localGovtTax = localTax,
                    sourceTax = sourceTax,
                    otherFees = other,
                    totalFee = total,
                    deedType = "সাফ-কবলা",
                    areaType = if (areaTypeIndex == 0) "সিটি কর্পোরেশন" else if (areaTypeIndex == 1) "পৌরসভা" else "ইউনিয়ন পরিষদ",
                    registrationPercent = regPercent,
                    stampDutyPercent = stampPercent,
                    localGovtTaxPercent = localPercent,
                    sourceTaxPercent = sourcePercent
                )
            }
        }
    }

    /**
     * Backward-compatible default overload matching the original signature.
     */
    fun calculateRegistrationFee(price: Double): RegistrationFeeResult {
        return calculateRegistrationFee(price, deedTypeIndex = 0, areaTypeIndex = 1)
    }
}

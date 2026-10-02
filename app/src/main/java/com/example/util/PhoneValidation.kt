package com.example.util

data class PhoneValidationState(
    val isValid: Boolean,
    val isComplete: Boolean,
    val message: String,
    val operatorName: String? = null,
    val currentLength: Int = 0,
    val targetLength: Int = 11
)

object BangladeshPhoneValidator {
    private val validPrefixes = setOf("013", "014", "015", "016", "017", "018", "019")

    fun getOperator(phone: String): String? {
        val digits = phone.filter { it.isDigit() }
        if (digits.length < 3) return null
        return when (digits.substring(0, 3)) {
            "013" -> "Grameenphone (Skitto)"
            "017" -> "Grameenphone"
            "014", "019" -> "Banglalink"
            "015" -> "Teletalk"
            "016" -> "Airtel"
            "018" -> "Robi"
            else -> null
        }
    }

    fun cleanDigits(input: String, maxLen: Int = 11): String {
        return input.filter { it.isDigit() }.take(maxLen)
    }

    fun validate(phone: String, isBangla: Boolean, isRocket: Boolean = false): PhoneValidationState {
        val digits = phone.filter { it.isDigit() }
        val maxLen = if (isRocket) 12 else 11

        if (digits.isEmpty()) {
            return PhoneValidationState(
                isValid = false,
                isComplete = false,
                message = if (isBangla) "১১ ডিজিটের বাংলাদেশী মোবাইল নম্বর লিখুন" else "Enter 11-digit Bangladesh mobile number",
                currentLength = 0,
                targetLength = 11
            )
        }

        if (!digits.startsWith("0")) {
            return PhoneValidationState(
                isValid = false,
                isComplete = false,
                message = if (isBangla) "মোবাইল নম্বরটি অবশ্যই '০' (0) দিয়ে শুরু হতে হবে" else "Mobile number must start with '0'",
                currentLength = digits.length,
                targetLength = 11
            )
        }

        if (digits.length >= 2 && !digits.startsWith("01")) {
            return PhoneValidationState(
                isValid = false,
                isComplete = false,
                message = if (isBangla) "বাংলাদেশী নম্বর অবশ্যই '০১' (01) দিয়ে শুরু হতে হবে" else "BD mobile number must start with '01'",
                currentLength = digits.length,
                targetLength = 11
            )
        }

        if (digits.length >= 3) {
            val prefix = digits.substring(0, 3)
            if (prefix !in validPrefixes) {
                return PhoneValidationState(
                    isValid = false,
                    isComplete = false,
                    message = if (isBangla) "অকার্যকর প্রিফিক্স '$prefix'। সঠিক অপারেটর কোড: ০১৩-০১৯" else "Invalid prefix '$prefix'. Valid operator codes: 013-019",
                    currentLength = digits.length,
                    targetLength = 11
                )
            }
        }

        val operator = getOperator(digits)

        if (digits.length < 11) {
            val remaining = 11 - digits.length
            val opText = if (operator != null) " ($operator)" else ""
            return PhoneValidationState(
                isValid = false,
                isComplete = false,
                message = if (isBangla) "${digits.length}/১১ ডিজিট$opText • আরো $remaining টি ডিজিট প্রয়োজন" 
                          else "${digits.length}/11 digits$opText • $remaining more needed",
                operatorName = operator,
                currentLength = digits.length,
                targetLength = 11
            )
        }

        if (!isRocket && digits.length > 11) {
            return PhoneValidationState(
                isValid = false,
                isComplete = false,
                message = if (isBangla) "১১ ডিজিটের বেশি অনুমোদিত নয়" else "Cannot exceed 11 digits",
                operatorName = operator,
                currentLength = digits.length,
                targetLength = 11
            )
        }

        // Complete and valid
        return PhoneValidationState(
            isValid = true,
            isComplete = true,
            message = if (isBangla) "✓ সঠিক বাংলাদেশী মোবাইল নম্বর ${operator?.let { "($it)" } ?: ""}" 
                      else "✓ Valid Bangladesh mobile number ${operator?.let { "($it)" } ?: ""}",
            operatorName = operator,
            currentLength = digits.length,
            targetLength = if (isRocket && digits.length == 12) 12 else 11
        )
    }
}

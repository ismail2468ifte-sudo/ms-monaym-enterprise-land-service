package com.example.util

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object RidmikKeyboardManager {

    const val RIDMIK_DEV_ID = "8115188161983387290"
    const val RIDMIK_PACKAGE = "ridmik.keyboard"
    const val RIDMIK_CLASSIC_PACKAGE = "ridmik.keyboard.classic"
    const val RIDMIK_PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=ridmik.keyboard"
    const val RIDMIK_DEV_URL = "https://play.google.com/store/apps/dev?id=8115188161983387290"

    /**
     * Checks if Ridmik Keyboard or Ridmik Keyboard Classic is installed on device
     */
    fun isRidmikInstalled(context: Context): Boolean {
        val pm = context.packageManager
        return try {
            pm.getPackageInfo(RIDMIK_PACKAGE, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            try {
                pm.getPackageInfo(RIDMIK_CLASSIC_PACKAGE, 0)
                true
            } catch (_: PackageManager.NameNotFoundException) {
                false
            }
        }
    }

    /**
     * Opens Android System Input Method Picker so user can select Ridmik Keyboard
     */
    fun showInputMethodPicker(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.showInputMethodPicker()
    }

    /**
     * Opens Android Language & Input / Keyboard settings
     */
    fun openKeyboardSettings(context: Context) {
        try {
            val intent = Intent(Settings.ACTION_INPUT_METHOD_SETTINGS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            Toast.makeText(context, "কীবোর্ড সেটিংস খুলতে ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Opens Google Play Store page for Ridmik Keyboard (Ridmik Labs: 8115188161983387290)
     */
    fun openRidmikPlayStore(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$RIDMIK_PACKAGE")).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(RIDMIK_PLAY_STORE_URL)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(webIntent)
        }
    }

    /**
     * If Ridmik is installed, opens the Input Method Picker to select Ridmik Keyboard.
     * If not installed, prompts user and opens Play Store link.
     */
    fun promptSwitchOrInstall(context: Context, isBangla: Boolean) {
        if (isRidmikInstalled(context)) {
            showInputMethodPicker(context)
            Toast.makeText(
                context,
                if (isBangla) "রিদ্মিক কীবোর্ড (Ridmik Keyboard) নির্বাচন করুন"
                else "Select Ridmik Keyboard from the list",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                context,
                if (isBangla) "রিদ্মিক কীবোর্ড ইনস্টল করতে গুগল প্লে স্টোরে নিয়ে যাওয়া হচ্ছে..."
                else "Opening Google Play Store to install Ridmik Keyboard...",
                Toast.LENGTH_LONG
            ).show()
            openRidmikPlayStore(context)
        }
    }
}

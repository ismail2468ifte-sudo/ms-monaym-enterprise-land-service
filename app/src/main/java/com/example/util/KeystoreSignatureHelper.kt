package com.example.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest

/**
 * Utility helper to extract and provide Keystore signing fingerprints (SHA-256, SHA-1, MD5)
 * for the current AAB / APK build.
 */
object KeystoreSignatureHelper {

    // Known fingerprints from the project's signing keystore (debug.keystore / release config)
    const val SIGNING_KEY_SHA256 = "26:B0:5F:2D:95:23:7C:CE:26:90:A4:C2:5A:15:F8:F4:28:B3:95:29:D1:62:85:C7:97:98:67:F1:11:2D:5A:AE"
    const val SIGNING_KEY_SHA256_NO_COLONS = "26b05f2d95237cce2690a4c25a15f8f428b39529d16285c7979867f1112d5aae"
    const val SIGNING_KEY_SHA1 = "7E:30:CF:36:E9:A1:35:E3:A2:36:DA:0C:25:3A:F4:82:F9:B0:30:39"
    const val SIGNING_KEY_MD5 = "69:F3:D5:26:3D:00:48:D5:FA:96:4E:72:BA:3A:08:D6"
    const val SIGNING_KEY_ALIAS = "androiddebugkey"
    const val KEYSTORE_NAME = "debug.keystore"
    const val CERT_OWNER = "CN=Android Debug, O=Android, C=US"
    const val CERT_SERIAL = "74b3615ceb88781c"

    /**
     * Dynamically reads the active signing certificate SHA-256 from the running application package.
     * Falls back to the known keystore fingerprint if not available.
     */
    fun getActiveSha256(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                packageInfo.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }

            if (!signatures.isNullOrEmpty()) {
                val cert = signatures[0].toByteArray()
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(cert)
                digest.joinToString(":") { String.format("%02X", it) }
            } else {
                SIGNING_KEY_SHA256
            }
        } catch (_: Exception) {
            SIGNING_KEY_SHA256
        }
    }

    /**
     * Dynamically reads the active signing certificate SHA-1.
     */
    fun getActiveSha1(context: Context): String {
        return try {
            val packageManager = context.packageManager
            val packageName = context.packageName
            val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNING_CERTIFICATES
                )
                packageInfo.signingInfo?.apkContentsSigners
            } else {
                @Suppress("DEPRECATION")
                val packageInfo = packageManager.getPackageInfo(
                    packageName,
                    PackageManager.GET_SIGNATURES
                )
                @Suppress("DEPRECATION")
                packageInfo.signatures
            }

            if (!signatures.isNullOrEmpty()) {
                val cert = signatures[0].toByteArray()
                val md = MessageDigest.getInstance("SHA-1")
                val digest = md.digest(cert)
                digest.joinToString(":") { String.format("%02X", it) }
            } else {
                SIGNING_KEY_SHA1
            }
        } catch (_: Exception) {
            SIGNING_KEY_SHA1
        }
    }
}

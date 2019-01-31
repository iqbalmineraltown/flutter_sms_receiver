package com.oval.smsreceiver

import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.util.Log
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import android.util.Base64
import java.util.Arrays


class AppSignatureHelper(context: Context): ContextWrapper(context) {

    companion object {
        val TAG = AppSignatureHelper::class.java.simpleName
        private val HASH_TYPE = "SHA-256"
        val NUM_HASHED_BYTES = 9
        val NUM_BASE64_CHAR = 11
    }

    fun getAppSignatures(): ArrayList<String> {
        val appCodes = ArrayList<String>()

        return try {
            // Get all package signatures for the current package
            val packageName = packageName
            val packageManager = packageManager
            val signatures = packageManager.getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES).signatures

            // For each signature create a compatible hash
            signatures
                    .mapNotNull { hash(packageName, it.toCharsString()) }
                    .mapTo(appCodes) { it }
            return appCodes
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e(TAG, "Unable to find package to obtain hash.", e)
            ArrayList<String>()
        }
    }

    private fun hash(packageName: String, signature: String): String? {
        val appInfo = packageName + " " + signature
        return try {
            val messageDigest = MessageDigest.getInstance(HASH_TYPE)
            messageDigest.update(appInfo.toByteArray(Charsets.UTF_8))
            var hashSignature = messageDigest.digest()

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, NUM_HASHED_BYTES)
            // encode into Base64
            var base64Hash = Base64.encodeToString(hashSignature, Base64.NO_PADDING or Base64.NO_WRAP)
            base64Hash = base64Hash.substring(0, NUM_BASE64_CHAR)

            Log.d(TAG, "pkg: $packageName -- hash: $base64Hash")
            base64Hash
        } catch (e: NoSuchAlgorithmException) {
            Log.e(TAG, "hash:NoSuchAlgorithm", e)
            null
        }
    }

}
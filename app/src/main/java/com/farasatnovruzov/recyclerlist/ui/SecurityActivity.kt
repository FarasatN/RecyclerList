package com.farasatnovruzov.recyclerlist.ui

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.farasatnovruzov.recyclerlist.R

class SecurityActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security)

        val fingerprintAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
        val faceIdAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)

        // Check if we're running on Android 6.0 (Marshmallow) or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Fingerprint API is only available from Android 6.0 (Marshmallow)
            val fingerprintManager = this.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

            if (fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
                // Fingerprint hardware is detected and at least one fingerprint is enrolled
                // Fingerprint authentication is activated
                // You can perform further actions here
            } else {
                // Fingerprint hardware is not detected or no fingerprints are enrolled
                // Fingerprint authentication is not activated
                // You can handle this scenario accordingly
            }
        }

        val hasFaceBiometric = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
        if (hasFaceBiometric) {
            // Face biometric is supported on the user's device
            // You can perform further actions here
        } else {
            // Face biometric is not supported on the user's device
            // You can handle this scenario accordingly
        }

    }

    private val authenticationCallback = object : FingerprintManager.AuthenticationCallback() {
        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
            if (isFingerprintEnabled) {
                // Fingerprint authentication succeeded, continue with desired actions
            } else {
                // Fingerprint authentication is disabled, prevent further actions
            }
        }

        // Implement other methods of the AuthenticationCallback as per your requirements
    }

    private var isFingerprintEnabled = true

    private fun disableFingerprint() {
        isFingerprintEnabled = false
        // Show a message or prompt the user for an alternative authentication method
    }

    private fun enableFingerprint() {
        isFingerprintEnabled = true
        // Reactivate the fingerprint functionality
    }
}
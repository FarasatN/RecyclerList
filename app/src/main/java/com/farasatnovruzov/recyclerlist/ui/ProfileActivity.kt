package com.farasatnovruzov.recyclerlist.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.webkit.WebView
import androidx.biometric.BiometricManager
import androidx.cardview.widget.CardView
import com.farasatnovruzov.recyclerlist.R

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val customerInfoCard = findViewById<CardView>(R.id.customerInfoCard)
        val securityCard = findViewById<CardView>(R.id.securityCard)
        val aboutAppCard = findViewById<CardView>(R.id.aboutAppCard)

        customerInfoCard.setOnClickListener {
            val intent = Intent(this, CustomerInfoActivity::class.java)
            startActivity(intent)
        }
        securityCard.setOnClickListener {
            val intent = Intent(this, SecurityActivity::class.java)
            startActivity(intent)
        }
        aboutAppCard.setOnClickListener {
            val intent = Intent(this, AboutAppActivity::class.java)
            startActivity(intent)
        }



//        checkDeviceHasBiometric()
//        authenticateWithBiometric()

//        val MANUFACTURER = Build.MANUFACTURER
//        val BRAND = Build.BRAND
//        val MODEL = Build.MODEL
//        val PRODUCT = Build.PRODUCT
//        val DEVICE = Build.DEVICE
//        val HARDWARE = Build.HARDWARE
//        val FINGERPRINT = Build.FINGERPRINT
//        val HOST = Build.HOST
//        val BOARD = Build.BOARD
//        val TYPE = Build.TYPE
//        val SOC_MANUFACTURER = Build.SOC_MANUFACTURER
//        val SOC_MODEL = Build.SOC_MODEL
//        val USER = Build.USER
//        val ID = Build.ID
//        val SKU = Build.SKU
//        val ODM_SKU = Build.ODM_SKU
//
//        val VERSION_NAME = BuildConfig.VERSION_NAME
//        val VERSION_CODES = BuildConfig.VERSION_CODE
//
//        println("$MANUFACTURER/$BRAND/$MODEL/$PRODUCT/$DEVICE/$HARDWARE/$FINGERPRINT/$HOST/$BOARD/$TYPE/$SOC_MANUFACTURER/$SOC_MODEL/$USER/$ID/$SKU/$ODM_SKU/$VERSION_NAME/$VERSION_CODES")


        val userAgent = WebView(this).settings.userAgentString
        val deviceID = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)

        println("userAgent: "+userAgent.toString())
        println("deviceID: "+deviceID)

    }





//    private fun checkDeviceHasBiometric(){
//        val biometricManager = BiometricManager.from(this)
//        when(biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL)){
//            BiometricManager.BIOMETRIC_SUCCESS -> Toast.makeText(this.applicationContext,"Device have biometric",Toast.LENGTH_SHORT).show()
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Toast.makeText(this.applicationContext,"Device doesn't have biometric",Toast.LENGTH_SHORT).show()
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Toast.makeText(this.applicationContext,"Not working",Toast.LENGTH_SHORT).show()
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Toast.makeText(this.applicationContext,"No biometric assigned",Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun authenticateWithBiometric() {
//        val biometricPrompt:  BiometricPrompt
//        val executor = ContextCompat.getMainExecutor(this)
//        biometricPrompt =
//            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    super.onAuthenticationSucceeded(result)
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Authentication succeeded",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    super.onAuthenticationError(errorCode, errString)
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Authentication error: $errString",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    Toast.makeText(this@MainActivity, "Authentication failed", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            })
//        val promtInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Biometric")
//            .setSubtitle("Biometric authentication")
//            .setNegativeButtonText("Cancel")
////            .setAllowedAuthenticators(BIOMETRIC_WEAK or BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
//            .build()
//        biometricPrompt.authenticate(promtInfo)
//    }

}
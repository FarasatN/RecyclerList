package com.farasatnovruzov.recyclerlist.ui

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R
import com.farasatnovruzov.recyclerlist.ScreenshotUtil
import com.farasatnovruzov.recyclerlist.ScreenshotUtil.screenshotIsAllowed
import com.farasatnovruzov.recyclerlist.databinding.ActivitySecurityBinding

class SecurityActivity : BaseActivity() {

    lateinit var binding: ActivitySecurityBinding
    var deviceName = "unknown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


//            else if (Settings.Secure.getString(contentResolver,"bluetooth_name")!=null){
//                deviceName = Settings.Secure.getString(contentResolver,"bluetooth_name")
//            }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            if (Settings.Global.getString(contentResolver,Settings.Global.DEVICE_NAME)!=null){
                deviceName = Settings.Global.getString(contentResolver,Settings.Global.DEVICE_NAME)
            }else{
                if (Build.MODEL.lowercase().startsWith(Build.MANUFACTURER.lowercase())) {
                    deviceName = Build.MODEL.capitalize()
                } else {
                    deviceName = Build.MANUFACTURER.capitalize()+" "+Build.MODEL
                }
            }
        }else{
            if (Build.MODEL.lowercase().startsWith(Build.MANUFACTURER.lowercase())) {
                deviceName = Build.MODEL.capitalize()
            } else {
                deviceName = Build.MANUFACTURER.capitalize()+" "+Build.MODEL
            }
        }

        binding.devicesTxt.text = deviceName

        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            binding.touchIdCard.visibility = View.VISIBLE
            val touchId = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("touchId",false)
            println("touchId : "+touchId)
            if(touchId){
                binding.touchIdTxt.text = getString(R.string.applied_profile)
                binding.touchIdSwitch.isChecked = true
            }
        }

        if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)){
            println("face")
            binding.faceIdCard.visibility = View.VISIBLE
            val faceId = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("faceId",false)
            if(faceId){
                binding.faceIdTxt.text = getString(R.string.applied_profile)
                binding.faceIdSwitch.isChecked = true
            }
        }

        if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)){
            println("iris")
            binding.irisIdCard.visibility = View.VISIBLE
            val irisId = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("irisId",false)
            if(irisId){
                binding.irisIdTxt.text = getString(R.string.applied_profile)
                binding.faceIdSwitch.isChecked = true
            }
        }

        val screenshotIsAllowed = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("screenshotIsAllowed",false)
        println("screenshotIsAllowed : "+screenshotIsAllowed)
        if(screenshotIsAllowed==true){
                binding.screenshotTxt.text = getString(R.string.applied_profile)
                binding.screenshotSwitch.isChecked = true
        }


        binding.touchIdSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("touchId",true)
                    apply()
                }
                binding.touchIdTxt.text = getString(R.string.applied_profile)
                Toast.makeText(this,"Touch ID applied",Toast.LENGTH_SHORT).show()
            }else{
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("touchId",false)
                    apply()
                }
                binding.touchIdTxt.text = getString(R.string.not_applied_profile)
                Toast.makeText(this,"Touch ID not applied",Toast.LENGTH_SHORT).show()
            }
        }

        binding.faceIdSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("faceId",true)
                    apply()
                }
                binding.faceIdTxt.text = getString(R.string.applied_profile)
                Toast.makeText(this,"Face ID applied",Toast.LENGTH_SHORT).show()
            }else{
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("faceId",false)
                    apply()
                }
                binding.faceIdTxt.text = getString(R.string.not_applied_profile)
                Toast.makeText(this,"Face ID not applied",Toast.LENGTH_SHORT).show()
            }
        }

        binding.irisIdSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("irisId",true)
                    apply()
                }
                binding.irisIdTxt.text = getString(R.string.applied_profile)
                Toast.makeText(this,"Iris ID applied",Toast.LENGTH_SHORT).show()
            }else{
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("irisId",false)
                    apply()
                }
                binding.irisIdTxt.text = getString(R.string.not_applied_profile)
                Toast.makeText(this,"Iris ID not applied",Toast.LENGTH_SHORT).show()
            }
        }


        binding.screenshotSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("screenshotIsAllowed",true)
                    apply()
                }
//                ScreenshotUtil.screenshotIsAllowed = true
                binding.screenshotTxt.text = getString(R.string.applied_profile)
                Toast.makeText(this,"Screenshot is allowed",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SecurityActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }else{
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("screenshotIsAllowed",false)
                    apply()
                }
//                ScreenshotUtil.screenshotIsAllowed = false
                binding.screenshotTxt.text = getString(R.string.not_applied_profile)
                Toast.makeText(this,"Screenshot is not allowed",Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SecurityActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
















//        val promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setTitle("Biometric login for my app")
//            .setSubtitle("Log in using your biometric credential")
//            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
//            .build()
//        val biometricPrompt = BiometricPrompt(this, mainExecutor, authenticationCallback)
//// Enable biometric authentication
//        biometricPrompt.authenticate(promptInfo)
//// Disable biometric authentication by not invoking the biometricPrompt.authenticate method
//// Alternatively, you can remove UI components triggering the authentication flow


//        val fingerprintAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)
//        val faceIdAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
//
//        // Check if we're running on Android 6.0 (Marshmallow) or higher
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Fingerprint API is only available from Android 6.0 (Marshmallow)
//            val fingerprintManager = this.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
//
//            if (fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
//                // Fingerprint hardware is detected and at least one fingerprint is enrolled
//                // Fingerprint authentication is activated
//                // You can perform further actions here
//
//            } else {
//                // Fingerprint hardware is not detected or no fingerprints are enrolled
//                // Fingerprint authentication is not activated
//                // You can handle this scenario accordingly
//
//            }
//        }
//
//        val hasFaceBiometric = packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)
//        if (hasFaceBiometric) {
//            // Face biometric is supported on the user's device
//            // You can perform further actions here
//        } else {
//            // Face biometric is not supported on the user's device
//            // You can handle this scenario accordingly
//        }

    }













//    private val authenticationCallback = object : FingerprintManager.AuthenticationCallback() {
//        override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
//            if (isFingerprintEnabled) {
//                // Fingerprint authentication succeeded, continue with desired actions
//            } else {
//                // Fingerprint authentication is disabled, prevent further actions
//            }
//        }
//
//        // Implement other methods of the AuthenticationCallback as per your requirements
//    }

//    private var isFingerprintEnabled = true
//
//    private fun disableFingerprint() {
//        isFingerprintEnabled = false
//        // Show a message or prompt the user for an alternative authentication method
//    }
//
//    private fun enableFingerprint() {
//        isFingerprintEnabled = true
//        // Reactivate the fingerprint functionality
//    }
}
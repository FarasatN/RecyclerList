package com.farasatnovruzov.recyclerlist.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R
import com.farasatnovruzov.recyclerlist.Utils
import com.farasatnovruzov.recyclerlist.databinding.ActivitySecurityBinding
import com.farasatnovruzov.recyclerlist.databinding.MlbToolbarBinding

var isFinishAffinityCalled = false
class SecurityActivity : BaseActivity() {

    lateinit var binding: ActivitySecurityBinding
    var changed = false

//    override fun onResume() {
//        super.onResume()
//        if (changed) {
//            restartApp()
//        }
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        isFinishAffinityCalled = false

        val toolbar_back_button = findViewById<LinearLayout>(R.id.toolbar_back_button)
        toolbar_back_button.setOnClickListener {
            finish()
        }

        binding.devicesTxt.text = Utils.deviceName()

        if (getSharedPreferences("user", MODE_PRIVATE).getBoolean("firstTimeLaunched",false)){
            println("firstTimeLaunched : ${getSharedPreferences("user", MODE_PRIVATE).getBoolean("firstTimeLaunched",false)}")

//            val fingerprintManager = FingerprintManagerCompat.from(applicationContext)
//            if (fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
//                // Fingerprint authentication is available and fingerprints are enrolled
//                println("Fingerprint authentication")
//                binding.touchIdCard.visibility = View.VISIBLE
//                println("touchId")
//                binding.touchIdTxt.text = getString(R.string.applied_profile)
//                binding.touchIdSwitch.isChecked = true
//            }

            if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
                binding.touchIdCard.visibility = View.VISIBLE
                println("touchId")
                    binding.touchIdTxt.text = getString(R.string.applied_profile)
                    binding.touchIdSwitch.isChecked = true
            }else if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)){
                println("face")
                binding.faceIdCard.visibility = View.VISIBLE
                    binding.faceIdTxt.text = getString(R.string.applied_profile)
                    binding.faceIdSwitch.isChecked = true
            }else if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)){
                println("iris")
                binding.irisIdCard.visibility = View.VISIBLE
                    binding.irisIdTxt.text = getString(R.string.applied_profile)
                    binding.faceIdSwitch.isChecked = true
            }

            getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                putBoolean("firstTimeLaunched",false)
                apply()
            }

            println("firstTimeLaunched : ${getSharedPreferences("user", MODE_PRIVATE).getBoolean("firstTimeLaunched",false)}")

        }else{
//            val fingerprintManager = FingerprintManagerCompat.from(applicationContext)
//            if (fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
//                // Fingerprint authentication is available and fingerprints are enrolled
//                println("Fingerprint authentication")
//                binding.touchIdCard.visibility = View.VISIBLE
//                println("touchId")
//                binding.touchIdTxt.text = getString(R.string.applied_profile)
//                binding.touchIdSwitch.isChecked = true
//            }
        }


        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            binding.touchIdCard.visibility = View.VISIBLE
            val touchId = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("touchId",false)
            println("touchId")
            if(touchId){
                binding.touchIdTxt.text = getString(R.string.applied_profile)
                binding.touchIdSwitch.isChecked = true
            }
        }else if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_FACE)){
            println("faceId")
            binding.faceIdCard.visibility = View.VISIBLE
            val faceId = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("faceId",false)
            if(faceId){
                binding.faceIdTxt.text = getString(R.string.applied_profile)
                binding.faceIdSwitch.isChecked = true
            }
        }else if(this.packageManager.hasSystemFeature(PackageManager.FEATURE_IRIS)){
            println("irisId")
            binding.irisIdCard.visibility = View.VISIBLE
            val irisId = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("irisId",false)
            if(irisId){
                binding.irisIdTxt.text = getString(R.string.applied_profile)
                binding.faceIdSwitch.isChecked = true
            }
        }

        val screenshotIsAllowed = getSharedPreferences("user",Context.MODE_PRIVATE).getBoolean("screenshotIsAllowed",false)
        println("screenshotIsAllowed : "+screenshotIsAllowed)
        if(screenshotIsAllowed){
                binding.screenshotTxt.text = getString(R.string.applied_profile)
                binding.screenshotSwitch.isChecked = true
        }else{
            binding.screenshotTxt.text = getString(R.string.not_applied_profile)
            binding.screenshotSwitch.isChecked = false
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
                binding.screenshotTxt.text = getString(R.string.applied_profile)
                Toast.makeText(this,"Screenshot is allowed",Toast.LENGTH_SHORT).show()
                changed = true
//                val intent = Intent(applicationContext, SecurityActivity::class.java)
//                val mPendingIntentId = 123456
//                val mPendingIntent = PendingIntent.getActivity(
//                    applicationContext,
//                    mPendingIntentId,
//                    intent,
//                    PendingIntent.FLAG_IMMUTABLE
//                )
//                val mgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 0, mPendingIntent)
//                System.exit(0)

//                restartApp()

//                startActivity(
//                    Intent(applicationContext, SecurityActivity::class.java)
//                )
//                System.exit(0)

//                navigateUpTo(Intent(this@SecurityActivity, SecurityActivity::class.java))
//                startActivity(getIntent())

                restartApp2()

            }else{
                getSharedPreferences("user",Context.MODE_PRIVATE).edit().apply{
                    putBoolean("screenshotIsAllowed",false)
                    apply()
                }
                binding.screenshotTxt.text = getString(R.string.not_applied_profile)
                Toast.makeText(this,"Screenshot is not allowed",Toast.LENGTH_SHORT).show()
                changed = true
//                val intent = Intent(applicationContext, SecurityActivity::class.java)
//                val mPendingIntentId = 123456
//                val mPendingIntent = PendingIntent.getActivity(
//                    applicationContext,
//                    mPendingIntentId,
//                    intent,
//                    PendingIntent.FLAG_IMMUTABLE
//                )
//                val mgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 0, mPendingIntent)
//                System.exit(0)

//                restartApp()

//                startActivity(
//                    Intent(applicationContext, SecurityActivity::class.java)
//                )
//                System.exit(0)

//                navigateUpTo(Intent(this@SecurityActivity, SecurityActivity::class.java))
//                startActivity(getIntent())

                restartApp2()

            }


//            val restartRequest = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//                if (result.resultCode == Activity.RESULT_OK) {
//                    // Handle the result data here
//                }
//            }
//            val intent = Intent(this, SecurityActivity::class.java)
//            restartRequest.launch(intent)

            changed = false
        }

    }

    private fun restartApp2() {
//        val intent = Intent(this, SecurityActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        startActivity(intent)
//        finish()
////        System.exit(0)
////        Runtime.getRuntime().exit(0)
//        kotlin.system.exitProcess(0)

//        ProcessPhoenix.triggerRebirth(this,intent)

//        val intent1 = Intent(this, ProfileActivity::class.java)
//        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(intent1)

//        val intent2 = Intent(this, SecurityActivity::class.java)
//        val stackBuilder: TaskStackBuilder = TaskStackBuilder.create(this)
//        stackBuilder.addNextIntentWithParentStack(intent2)
//        stackBuilder.startActivities()

//        val intent3 = Intent(this, ThirdActivity::class.java)
//        stackBuilder.addNextIntent(intent3)
//        stackBuilder.startActivities()


        finishAffinity()
        startActivities(
            arrayOf(
                Intent(applicationContext, ProfileActivity::class.java),
                Intent(applicationContext, SecurityActivity::class.java),
            )
        )
        isFinishAffinityCalled = true
    }

    private fun restartApp() {
        val pm = applicationContext.packageManager
        val intent = pm.getLaunchIntentForPackage(applicationContext.packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent?.component)
        applicationContext.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
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



//            else if (Settings.Secure.getString(contentResolver,"bluetooth_name")!=null){
//                deviceName = Settings.Secure.getString(contentResolver,"bluetooth_name")
//            }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
//            if (Settings.Global.getString(contentResolver,Settings.Global.DEVICE_NAME)!=null){
//                deviceName = Settings.Global.getString(contentResolver,Settings.Global.DEVICE_NAME)
//            }else{
//                if (Build.MODEL.lowercase().startsWith(Build.MANUFACTURER.lowercase())) {
//                    deviceName = Build.MODEL.capitalize()
//                } else {
//                    deviceName = Build.MANUFACTURER.capitalize()+" "+Build.MODEL
//                }
//            }
//        }else{
//            if (Build.MODEL.lowercase().startsWith(Build.MANUFACTURER.lowercase())) {
//                deviceName = Build.MODEL.capitalize()
//            } else {
//                deviceName = Build.MANUFACTURER.capitalize()+" "+Build.MODEL
//            }
//        }


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
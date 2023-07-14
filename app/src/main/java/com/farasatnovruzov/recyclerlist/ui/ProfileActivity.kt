package com.farasatnovruzov.recyclerlist.ui

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.hardware.fingerprint.FingerprintManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.*
import androidx.biometric.BiometricPrompt
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentTransaction
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.Executor


class ProfileActivity : BaseActivity() {
//    // The path to the root of this app's internal storage
//    private lateinit var privateRootDir: File
//    // The path to the "images" subdirectory
//    private lateinit var imagesDir: File
//    // Array of files in the images subdirectory
//    private lateinit var imageFiles: Array<File>
//    // Array of filenames corresponding to imageFiles
//    private lateinit var imageFilenames: Array<String>



    lateinit var executor: Executor
    lateinit var biometricPrompt: BiometricPrompt
    lateinit var promptInfo: BiometricPrompt.PromptInfo
//    lateinit var test:Fragment
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        supportFragmentManager.putFragment(outState,"test",TestFragment())
//    }



    private var imageUri: Uri? = null
    private fun pickImage(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "*/*"
        galleryActivityResultLauncher.launch(intent)
    }
    private var galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ActivityResultCallback<ActivityResult> {result->
            if (result.resultCode==Activity.RESULT_OK){
               //image picked
                val intent = result.data
                imageUri = intent!!.data
            }else{
                //cancelled
            }
        }
    )
    private fun shareImage(){
        val contentUri = getContentUri()
        val intent = Intent(Intent.ACTION_SEND)
//        intent.type = "image/png"
        intent.type = "*/*"
        intent.putExtra(Intent.EXTRA_STREAM,contentUri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(Intent.createChooser(intent,"Share Via"))
    }
    private fun getContentUri():Uri?{
        val bitmap: Bitmap
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
            val source = ImageDecoder.createSource(contentResolver,imageUri!!)
            bitmap = ImageDecoder.decodeBitmap(source)
        }else{
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver,imageUri)
        }
//        val filesFolder = File(cacheDir,"images")
        val filesFolder = File(cacheDir,"files")
        var contentUri : Uri? = null
        try{
            filesFolder.mkdir()
            val file = File(filesFolder, "shared_file.jpeg")
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 1/999999999999999999, stream)
            stream.flush()
            stream.close()
            contentUri = FileProvider.getUriForFile(this, "com.farasatnovruzov.recyclerlist.fileprovider", file)
        }catch(e: Exception){
            e.printStackTrace()
        }
        return contentUri
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val aboutAppCard = findViewById<CardView>(R.id.aboutAppCard)
        aboutAppCard.setOnClickListener {
            pickImage()
        }
        aboutAppCard.setOnLongClickListener {
            shareImage()
            true
        }








//        // Set up an Intent to send back to apps that request a file
//        val resultIntent = Intent("com.example.myapp.ACTION_RETURN_FILE")
//        // Get the files/ subdirectory of internal storage
//        privateRootDir = filesDir
//        // Get the files/images subdirectory;
//        imagesDir = File(privateRootDir, "images")
//        // Get the files in the images subdirectory
//        imageFiles = imagesDir.listFiles()
//        // Set the Activity's result to null to begin with
//        setResult(Activity.RESULT_CANCELED, null)
//
//        val requestFile = File(imageFilenames[0])
//        /*
//         * Most file-related method calls need to be in
//         * try-catch blocks.
//         */
//        // Use the FileProvider to get a content URI
//        val fileUri: Uri? = try {
//            FileProvider.getUriForFile(
//                this,
//                "com.example.myapp.fileprovider",
//                requestFile)
//        } catch (e: IllegalArgumentException) {
//            Log.e("File Selector",
//                "The selected file can't be shared: $requestFile")
//            null
//        }





//        MobileAds.initialize(this) {}
//        val adView = findViewById<AdView>(R.id.adView)
//
//
//        AdView(this).also {
//            adView.addView(it)
//            val adRequest = AdRequest.Builder().build()
////            it.adSize = adSize
//            it.adUnitId = "ca-app-pub-9767202211621680/9546532272"
//            it.loadAd(adRequest)
//        }





//        if (savedInstanceState != null) {
//            test = supportFragmentManager.getFragment(savedInstanceState, "test")!!
//        } else {
//            test = TestFragment()
//        }

        if (isFinishAffinityCalled==true){
            val activityView = findViewById<ConstraintLayout>(R.id.activityView)
            activityView.visibility = View.GONE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.addToBackStack(null)
            transaction.replace(R.id.mainContainer,TestFragment())
                .addToBackStack(TestFragment::class.java.name)
                .commit()
        }


        checkDeviceHasBiometricWithPermission()

        //------------------------------------------------

        val customerInfoCard = findViewById<CardView>(R.id.customerInfoCard)
        val securityCard = findViewById<CardView>(R.id.securityCard)

        customerInfoCard.setOnClickListener {
            val intent = Intent(this, CustomerInfoActivity::class.java)
            startActivity(intent)
        }
        securityCard.setOnClickListener {
//            val intent = Intent(this, SecurityActivity::class.java)
//            startActivity(intent)
            val activityView = findViewById<ConstraintLayout>(R.id.activityView)
            activityView.visibility = View.GONE
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            transaction.addToBackStack(null)
            transaction.replace(R.id.mainContainer,TestFragment())
                .addToBackStack(TestFragment::class.java.name)
                .commit()

        }
//        aboutAppCard.setOnClickListener {
//            val intent = Intent(this, AboutAppActivity::class.java)
//            startActivity(intent)
//        }


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

        println("userAgent: " + userAgent.toString())
        println("deviceID: " + deviceID)

    }

    private fun checkDeviceHasBiometricWithPermission() {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            if (!keyguardManager.isDeviceSecure) {
                println("Fingerprint authentication has not been enabled in settings")
            }
            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.USE_BIOMETRIC
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                println("Fingerprint Authentication Permission is not enabled")
            }
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
                println("Fingerprint Authentication Permission is enabled")
            }
        } else {
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                    executor = ContextCompat.getMainExecutor(this@ProfileActivity)
                    biometricPrompt = BiometricPrompt(this@ProfileActivity, executor,
                        object : BiometricPrompt.AuthenticationCallback() {
                            override fun onAuthenticationFailed() {
                                super.onAuthenticationFailed()
                                Toast.makeText(
                                    this@ProfileActivity,
                                    "Authentication failed",
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                super.onAuthenticationSucceeded(result)
                                Toast.makeText(
                                    this@ProfileActivity,
                                    "Authentication succeeded",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            override fun onAuthenticationError(
                                errorCode: Int,
                                errString: CharSequence
                            ) {
                                super.onAuthenticationError(errorCode, errString)
                                Toast.makeText(
                                    this@ProfileActivity,
                                    "Authentication error $errString",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    promptInfo = BiometricPrompt.PromptInfo.Builder()
//            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.BIOMETRIC_STRONG)
//            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
                        .setTitle("Biometric Authentication")
                        .setSubtitle("Login using your biometric credential")
                        .setConfirmationRequired(true)
                        .setDeviceCredentialAllowed(true)
//                        .setNegativeButtonText("Cancel")
                        .build()
                    val touchId =
                        getSharedPreferences("user", MODE_PRIVATE).getBoolean("touchId", false)
                    val faceId =
                        getSharedPreferences("user", MODE_PRIVATE).getBoolean("faceId", false)
                    val irisId =
                        getSharedPreferences("user", MODE_PRIVATE).getBoolean("irisId", false)

//        if(this.getSystemService(BiometricManager::class.java)?.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS){
//        if(promptInfo.allowedAuthenticators==BiometricManager.Authenticators.BIOMETRIC_WEAK){
                    if (getSharedPreferences("user", MODE_PRIVATE).contains("faceId") ||
                        getSharedPreferences("user", MODE_PRIVATE).contains("touchId") ||
                        getSharedPreferences("user", MODE_PRIVATE).contains("irisId") ||
                        getSharedPreferences("user", MODE_PRIVATE).contains("screenshotIsAllowed")
                    ) {
                        if (touchId) {
                            biometricPrompt.authenticate(promptInfo)
                        }
                        if (faceId) {
                            biometricPrompt.authenticate(promptInfo)
                        }
                        if (irisId) {
                            biometricPrompt.authenticate(promptInfo)
                        }



//                        val fingerprintManager = FingerprintManagerCompat.from(applicationContext)
//                        if (fingerprintManager.isHardwareDetected && fingerprintManager.hasEnrolledFingerprints()) {
//                            // Fingerprint authentication is available and fingerprints are enrolled
//                            println("Fingerprint authentication")
//                            getSharedPreferences("user", Context.MODE_PRIVATE).edit().apply {
//                                putBoolean("isFinger", true)
//                                apply()
//                            }
//                        }
                        val authenticationCallback = object : FingerprintManager.AuthenticationCallback() {
                            override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                                // Authentication succeeded
                                println("Fingerprint authentication")

                            }

                            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                                // Authentication error
                            }

                            override fun onAuthenticationFailed() {
                                // Authentication failed
                            }
                        }

//                        val cryptoObject = FingerprintManager.CryptoObject(yourCipher)
//                        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, authenticationCallback, null)
                    } else {
                        println("shared cleared")
                        biometricPrompt.authenticate(promptInfo)
                        getSharedPreferences("user", Context.MODE_PRIVATE).edit().apply {
                            putBoolean("firstTimeLaunched", true)
                            apply()
                        }
                    }
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.e("MY_APP_TAG", "No biometric features available on this device.")
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")
                }
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Prompts the user to create credentials that your app accepts.

                }
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> {
                }
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> {
                }
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> {
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val activityView = findViewById<ConstraintLayout>(R.id.activityView)
        activityView.visibility = View.VISIBLE
    }
}


//private fun checkDeviceHasBiometric() {
////        BIOMETRIC_ERROR_NONE_ENROLLED if the user does not have any enrolled
////        BIOMETRIC_ERROR_HW_UNAVAILABLE if none are currently supported/enabled
////        BIOMETRIC_SUCCESS if a biometric can currently be used (enrolled and available).
//    val biometricManager = BiometricManager.from(this)
//    when (biometricManager.canAuthenticate(BIOMETRIC_STRONG or BIOMETRIC_WEAK or DEVICE_CREDENTIAL)) {
//        BiometricManager.BIOMETRIC_SUCCESS -> Toast.makeText(
//            this.applicationContext,
//            "Device have biometric",
//            Toast.LENGTH_SHORT
//        ).show()
//        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Toast.makeText(
//            this.applicationContext,
//            "Device doesn't have biometric",
//            Toast.LENGTH_SHORT
//        ).show()
//        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Toast.makeText(
//            this.applicationContext,
//            "Not working",
//            Toast.LENGTH_SHORT
//        ).show()
//        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Toast.makeText(
//            this.applicationContext,
//            "No biometric assigned",
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//}

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

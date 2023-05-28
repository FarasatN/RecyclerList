package com.farasatnovruzov.recyclerlist

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up common features or initialization code here
        // that should be executed for all activities.

        val screenshotIsAllowed = getSharedPreferences("user", MODE_PRIVATE).getBoolean("screenshotIsAllowed",false)
        if (screenshotIsAllowed==false) {
            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }

//        if (ScreenshotUtil.screenshotIsAllowed==false) {
//            window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
//        }else{
//            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
//        }


    }

    // Define any common methods or functionality
    // that should be available to all activities.
    protected fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    } // You can add more common methods as per your requirements.
}
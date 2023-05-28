//package com.farasatnovruzov.recyclerlist
//
//import android.app.Activity
//import android.app.Application
//import android.os.Bundle
//import android.view.WindowManager
//
//class MyApp: Application() {
//
//    override fun onCreate() {
//        super.onCreate()
//
//        val screenshotIsAvailable = getSharedPreferences("user", MODE_PRIVATE).getBoolean("screenshotIsAllowed",false)
//        if (!screenshotIsAvailable) {
//            registerListener()
//        }
//    }
//
//    private fun registerListener() {
//        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks{
//            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
//                p0.window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
//            }
//
//            override fun onActivityStarted(p0: Activity) {
//            }
//
//            override fun onActivityResumed(p0: Activity) {
//            }
//
//            override fun onActivityPaused(p0: Activity) {
//            }
//
//            override fun onActivityStopped(p0: Activity) {
//            }
//
//            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
//            }
//
//            override fun onActivityDestroyed(p0: Activity) {
//            }
//
//        })
//    }
//}
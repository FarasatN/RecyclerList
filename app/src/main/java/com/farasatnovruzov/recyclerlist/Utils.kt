package com.farasatnovruzov.recyclerlist

import android.os.Build
import java.util.*

object Utils {

    fun deviceName() : String {
        if (Build.MODEL.lowercase().startsWith(Build.MANUFACTURER.lowercase())) {
            return Build.MODEL.capitalize()
        } else {
            return Build.MANUFACTURER.capitalize() + " " + Build.MODEL
        }
    }

    fun String.capitalize(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }
}
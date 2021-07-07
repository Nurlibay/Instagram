package uz.texnopos.instagram.data

import android.content.Context

class Settings(private val context: Context) {

    companion object{
        const val KEY_SINGED_IN = "keySingedIn"
    }

    private val prefs = context.getSharedPreferences("${context.packageName}.prefs", Context.MODE_PRIVATE)
    var signedIn: Boolean
    set(value) = prefs.edit().putBoolean(KEY_SINGED_IN, value).apply()
    get() = prefs.getBoolean(KEY_SINGED_IN, false)
}
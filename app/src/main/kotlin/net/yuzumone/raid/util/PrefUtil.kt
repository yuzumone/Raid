package net.yuzumone.raid.util

import android.content.Context
import android.content.SharedPreferences

class PrefUtil(context: Context) {
    private val FILENAME = "net.yuzumone.raid.prefs"
    private val MORIOKA = "morioka"
    private val TAKIZAWA = "takizawa"
    private val prefs: SharedPreferences = context.getSharedPreferences(FILENAME, 0)

    var isMoriokaNotification: Boolean
    get() = prefs.getBoolean(MORIOKA, false)
    set(value) = prefs.edit().putBoolean(MORIOKA, value).apply()

    var isTakizawaNotification: Boolean
    get() = prefs.getBoolean(TAKIZAWA, false)
    set(value) = prefs.edit().putBoolean(TAKIZAWA, value).apply()
}
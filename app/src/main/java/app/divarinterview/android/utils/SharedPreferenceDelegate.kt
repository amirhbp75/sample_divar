package app.divarinterview.android.utils

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferenceDelegate<T>(
    private val context: Context,
    private val key: String,
    private val defaultValue: T,
) : ReadWriteProperty<Any?, T> {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return sharedPreferences.get(key, defaultValue)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        sharedPreferences.put(key, value)
    }

    private fun SharedPreferences.get(key: String, defaultValue: T?): T {
        @Suppress("UNCHECKED_CAST")
        return when (defaultValue) {
            null -> getString(key, null) as T
            is String -> getString(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    private fun SharedPreferences.put(key: String, value: T) {
        when (value) {
            null -> edit().putString(key, null).apply()
            is String -> edit().putString(key, value).apply()
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }
}

fun <T> Context.sharedPreferences(key: String, defaultValue: T) =
    SharedPreferenceDelegate(this, key, defaultValue)
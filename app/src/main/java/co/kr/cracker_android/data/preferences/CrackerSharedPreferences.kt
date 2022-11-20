package co.kr.cracker_android.data.preferences

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val FILE_NAME = "CRACKER"

class CrackerSharedPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val preferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE)

    var uuid: String
        set(value) = preferences.edit { putString("UUID", value) }
        get() = preferences.getString("UUID", "") ?: ""
}
package co.kr.cracker_android.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.toDateString(): String {
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(this)
}
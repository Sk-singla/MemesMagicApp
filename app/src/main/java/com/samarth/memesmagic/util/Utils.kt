package com.samarth.memesmagic.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.text.format.DateUtils
import androidx.navigation.NavController
import com.samarth.memesmagic.util.Screens.MAX_PASSWORD_LENGTH
import com.samarth.memesmagic.util.Screens.MIN_PASSWORD_LENGTH
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*
import java.util.regex.Pattern

fun numberOfPosts(postCount:Int):String {
    return when {
        postCount < 1000 -> {
            "$postCount"
        }
        postCount == 1000 -> {
            val temp:Int = postCount / 1000
            "${temp}K"
        }
        postCount < 1000000 -> {
            val temp:Int = postCount / 1000
            "${temp}K+"
        }
        postCount == 1000000 -> {
            val temp:Int = postCount / 1000000
            "${temp}M"
        }
        else -> {
            val temp:Int = postCount / 1000000
            "${temp}M+"
        }
    }
}

fun numberOfFollowersOrFollowings(followers:Int):String {
    return numberOfPosts(followers)
}


fun navigateWithPop(navController: NavController, destination:String){
    navController.popBackStack()
    navController.navigate(destination)
}


inline fun <T> sdk29AndUp(onSdk29:()->T):T?{
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
        onSdk29()
    } else null
}

// =========================== DATE FORMAT ================================


fun Calendar.isThisWeek(): Boolean {
    val thisCalendar = Calendar.getInstance()
    val thisWeek = thisCalendar.get(WEEK_OF_YEAR)
    val thisYear = thisCalendar.get(YEAR)

    val calendar = Calendar.getInstance()
    calendar.time = this.time
    val week = calendar.get(WEEK_OF_YEAR)
    val year = calendar.get(YEAR)

    return year == thisYear && week == thisWeek
}

fun Calendar.isToday(): Boolean {
    return DateUtils.isToday(this.timeInMillis)
}

fun Calendar.isYesterday(): Boolean {
    val yestercal = Calendar.getInstance()
    yestercal.add(DAY_OF_YEAR, -1)

    return yestercal.get(YEAR) == this.get(YEAR)
            && yestercal.get(DAY_OF_YEAR) == this.get(DAY_OF_YEAR)
}

fun Calendar.isThisYear(calendar: Calendar): Boolean {
    return getInstance().get(YEAR) == calendar.get(YEAR)
}

fun Calendar.isSameDayAs(date: Calendar): Boolean {
    return this.get(DAY_OF_YEAR) == date.get(DAY_OF_YEAR)
}





@SuppressLint("SimpleDateFormat")
fun getRewardDate(time:Long, isMonth:Boolean = true):String{
    val date = Date(time)
    val stf = SimpleDateFormat(if(isMonth) "MMM, yyyy" else "yyyy")
    return stf.format(date)
}

fun getChatMessageTime(time: Long): String{
    val date = Date(time)
    val stf = SimpleDateFormat("hh:mm aaa", Locale.getDefault())
    return stf.format(date)
}

fun lastChatMessageTime(time:Long) : String {
    val cal = Calendar.getInstance()
    cal.timeInMillis = time
    val date = Date(time)


    val stf = if(cal.isToday()){
        SimpleDateFormat("hh:mm aaa", Locale.getDefault()).format(date)
    } else if(cal.isYesterday()){
        "Yesterday"
    } else {
        SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(date)
    }
    return stf.format(date)
}


// =========================== DATE FORMAT ================================

fun ContentResolver.getFileName(uri: Uri):String{
    var name = ""
    val cursor = query(uri,null,null,null,null)
    cursor?.use {
        it.moveToFirst()
        name = cursor.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
    }
    return name
}

fun isItEmail(email:String):Boolean{
    val regex =  "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    return Pattern.compile(regex).matcher(email).matches()
}

fun validatePassword(password:String):Boolean{
    return password.length in MIN_PASSWORD_LENGTH..MAX_PASSWORD_LENGTH
}



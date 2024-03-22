package com.chat.youknow.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.chat.youknow.ui.authenticate.LoginActivity


fun isConnected(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
    val network = connectivityManager.activeNetwork
    return network != null
}

fun moveToLogin(activity: FragmentActivity) {
    val intent = Intent(activity, LoginActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}


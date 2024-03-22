package com.chat.youknow.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.chat.youknow.R
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Fragment.hideKeyboard(){
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View){
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showSnackBar(message: String,isShort:Boolean = true) {
    var snackbar:Snackbar? = null
    snackbar = if (isShort){
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    }else{
        Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    }
    snackbar.show()
}
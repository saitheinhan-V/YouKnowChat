package com.chat.youknow.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.chat.youknow.network.Resource

fun resize(bitmap: Bitmap,resource: Resources): Drawable? {
//    val b: Bitmap = (image as BitmapDrawable).getBitmap()
    val bitmapResized: Bitmap = Bitmap.createScaledBitmap(bitmap, 70, 50, false)
    return BitmapDrawable(resource, bitmapResized)
}
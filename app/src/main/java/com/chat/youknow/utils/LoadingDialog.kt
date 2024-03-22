package com.chat.youknow.utils

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.chat.youknow.R

class LoadingDialog(var activity: FragmentActivity) {

    var dialog: Dialog? = null

    fun showDialog(msg: String) {
        dialog = Dialog(activity)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dialog_loading)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val gifImageView = dialog?.findViewById<ImageView>(R.id.iv_loading)
        val tvMsg = dialog?.findViewById<TextView>(R.id.tv_msg)
        val imageViewTarget = DrawableImageViewTarget(gifImageView)

        if(msg.isNullOrEmpty()){
            tvMsg?.text = activity.resources.getString(R.string.loading)
        }else{
            tvMsg?.text = msg
        }

        Glide.with(activity)
            .load(R.drawable.loading)
            .placeholder(R.drawable.loading)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageViewTarget)
            dialog?.show()
    }

    fun isShowing() = dialog?.isShowing

    fun hideDialog() {
        dialog?.dismiss()
    }

}
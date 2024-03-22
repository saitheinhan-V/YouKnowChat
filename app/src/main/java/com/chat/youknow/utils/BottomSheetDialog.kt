package com.chat.youknow.utils

import android.R.attr.button
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.annotation.Nullable
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.chat.youknow.R
import com.chat.youknow.data.response.Country
import com.chat.youknow.databinding.LayoutBottomSheetBinding
import com.chat.youknow.ui.authenticate.LoginActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetDialog(
    private val ctx: Context,
    private val list: MutableList<Country>,
    private val index: Int
) : BottomSheetDialogFragment() {
    private lateinit var binding: LayoutBottomSheetBinding

    var mBehavior: BottomSheetBehavior<View>? = null

    private val activityParent get() = activity as LoginActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutBottomSheetBinding.inflate(inflater, container, false)

        dialog?.setCanceledOnTouchOutside(true)

        for ((i, country) in list.withIndex()) {
            val radioButton = RadioButton(ctx)

            Glide.with(ctx)
                .asBitmap()
                .load(country.url)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
//                        val drw: Drawable = BitmapDrawable(resources, resource)
                        val resizeDrawable: Drawable = resize(resource,resources)!!
                        radioButton.setCompoundDrawablesWithIntrinsicBounds(resizeDrawable, null, null,null)
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }
                })

            radioButton.text = "${country.code} (${country.iso}) ${country.name}"
            radioButton.compoundDrawablePadding = 10
            radioButton.layoutDirection = View.LAYOUT_DIRECTION_RTL
            radioButton.gravity = Gravity.START
            radioButton.setPadding(20, 20, 20, 20)
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(10, 10, 10, 10)
            radioButton.layoutParams = params
            radioButton.id = i

            if (index == country.id) radioButton.isChecked = true

            binding.radioGroup.addView(radioButton)
        }


        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = binding.radioGroup.findViewById<RadioButton>(checkedId)
            val item = list[checkedId]
            activityParent.onCountrySelect(item)
            dismiss()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    // used to show the bottom sheet dialog
//        val dialog =
//            super.onCreateDialog(savedInstanceState) as com.google.android.material.bottomsheet.BottomSheetDialog
//        dialog?.setOnShowListener { it ->
//            val d = it as BottomSheetDialog
//            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            bottomSheet?.let {
//                val behavior = BottomSheetBehavior.from(it)
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//        }
//        return super.onCreateDialog(savedInstanceState)

//    }

    companion object {
        const val TAG = "ModalBottomSheetDialog"
    }
}
package com.chat.youknow.ui.authenticate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.findNavController
import com.chat.youknow.MainActivity
import com.chat.youknow.R
import com.chat.youknow.data.response.Country
import com.chat.youknow.data.viewmodels.LoginViewModel
import com.chat.youknow.databinding.ActivityLoginBinding
import com.chat.youknow.utils.AppConstant
import com.chat.youknow.utils.AppSharedPreference
import com.chat.youknow.utils.LoadingDialog
import de.greenrobot.event.EventBus

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var sharedPreference: AppSharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadingDialog = LoadingDialog(this)
        sharedPreference = AppSharedPreference(this)

        if(!sharedPreference.getValueString(AppConstant.TOKEN).isNullOrEmpty()){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_activity_login)
    }

    fun onCountrySelect(item: Country){
        viewModel.countryId = item.id
        viewModel.countryISO = item.iso

        EventBus.getDefault().post(item)

    }
}
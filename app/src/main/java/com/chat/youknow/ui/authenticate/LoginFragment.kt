package com.chat.youknow.ui.authenticate

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.chat.youknow.MainActivity
import com.chat.youknow.MyApplication
import com.chat.youknow.R
import com.chat.youknow.data.database.RoomDB
import com.chat.youknow.data.model.LoginData
import com.chat.youknow.data.model.User
import com.chat.youknow.data.response.Country
import com.chat.youknow.data.response.LoginResponse
import com.chat.youknow.data.viewmodels.LoginViewModel
import com.chat.youknow.databinding.FragmentLoginBinding
import com.chat.youknow.network.Resource
import com.chat.youknow.utils.*
import com.google.android.material.snackbar.Snackbar
import de.greenrobot.event.EventBus
import de.greenrobot.event.Subscribe
import de.greenrobot.event.ThreadMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var country: String
    private lateinit var password: String
    private lateinit var phoneNumber: String

    private var countryList: MutableList<Country> = mutableListOf()

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var sharedPreference: AppSharedPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EventBus.getDefault().register(this)

        loadingDialog = LoadingDialog(requireActivity())
        sharedPreference = AppSharedPreference(requireContext())

        if (viewModel.countryId != -1) {
            binding.countryTextView.text = viewModel.countryISO
        }
        loadData()

        binding.phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    binding.phoneNumberTextInputLayout.error = getString(R.string.enter_password)
                } else {
                    binding.phoneNumberTextInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
//                binding.loginButton.checkRequirement()
            }

        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s.isNullOrEmpty()) {
                    binding.passwordTextInputLayout.error = getString(R.string.enter_password)
                } else {
                    binding.passwordTextInputLayout.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {
//                binding.loginButton.checkRequirement()
            }

        })

        binding.countryTextView.setOnClickListener {
            val bottomSheetDialog =
                BottomSheetDialog(requireContext(), viewModel.countryList, viewModel.countryId)
            childFragmentManager.let {
                bottomSheetDialog.show(it, BottomSheetDialog.TAG)
            }
        }

        binding.loginButton.setOnClickListener {
            if (!isConnected(requireContext())) {
                requireContext().showToast(resources.getString(R.string.network_error))
                return@setOnClickListener
            }
            if (checkRequirement()) {
                val data = LoginData(viewModel.countryId, phoneNumber, password)
                viewModel.login(data).observe(this, Observer {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            loadingDialog.showDialog(resources.getString(R.string.logging_in))
                        }
                        Resource.Status.ERROR -> {
                            loadingDialog.hideDialog()
                            binding.root.showSnackBar(it.errorMsg.toString(),false)
                            if(it.errorMsg.toString() == AppConstant.TOKEN_EXPIRE){
                                lifecycleScope.launch(Dispatchers.IO) {
                                    viewModel.clearTable()
                                }
                                sharedPreference.clearSharedPreference()
                                moveToLogin(requireActivity())
                            }
                        }
                        Resource.Status.SUCCESS -> {
                            loadingDialog.hideDialog()

                            loginSuccess(it.data!!)
                        }
                    }
                })
            }
        }
    }

    private fun loginSuccess(data: LoginResponse) {
        sharedPreference.save(AppConstant.TOKEN, data.accessToken)
        sharedPreference.save("user_id", data.profileInfo.userId)

        val uid = UUID.randomUUID()
        val user = User(
            1,data.profileInfo.userId, data.profileInfo.username, data.profileInfo.gender,
            data.profileInfo.countryId, data.profileInfo.mobile, data.accessToken, data.refreshToken
        )

        viewModel.saveUser(user)

        activity?.startActivity(Intent(context, MainActivity::class.java))
        activity?.finish()
    }

    private fun loadData() {

        viewModel.getCountry().observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {

                }
                Resource.Status.ERROR -> {
                    Snackbar.make(binding.root, it.errorMsg.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }
                Resource.Status.SUCCESS -> {
                    countryList = it.data!!

                    viewModel.countryList = countryList

                    Log.i("country", "List::: $countryList")
                }
            }
        })
    }

    private fun checkRequirement(): Boolean {
//        country = binding.countryEditText.text.toString().trim()
        phoneNumber = binding.phoneNumberEditText.text.toString().trim()
        password = binding.passwordEditText.text.toString().trim()

        hideKeyboard()

        if (viewModel.countryId == -1) {
            Toast.makeText(requireContext(), getString(R.string.select_country), Toast.LENGTH_SHORT)
                .show()
        }

        if (phoneNumber.isNullOrEmpty()) {
            binding.phoneNumberTextInputLayout.error = getString(R.string.enter_phone)
        } else {
            binding.phoneNumberTextInputLayout.isErrorEnabled = false
        }

        if (password.isNullOrEmpty()) {
            binding.passwordTextInputLayout.error = getString(R.string.enter_password)
        } else {
            binding.passwordTextInputLayout.isErrorEnabled = false
        }

        return (viewModel.countryId != -1 && !binding.phoneNumberTextInputLayout.isErrorEnabled &&
                !binding.passwordTextInputLayout.isErrorEnabled)
    }

    override fun onResume() {
        super.onResume()
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    fun helloEventBus(message: Country) {
        binding.countryTextView.text = message.iso
    }

}
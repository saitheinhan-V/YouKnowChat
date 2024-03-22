package com.chat.youknow.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.chat.youknow.data.database.RoomDB
import com.chat.youknow.data.database.RoomDao
import com.chat.youknow.data.model.LoginData
import com.chat.youknow.data.model.User
import com.chat.youknow.data.repository.BaseRepository
import com.chat.youknow.data.response.Country
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(application: Application): AndroidViewModel(application) {


    var user = User()

    private val repository = BaseRepository(application)

    var countryList: MutableList<Country> = mutableListOf()

    var countryId = -1
    var countryISO = ""

    fun getCountry() = repository.getCountry()

    fun login(data: LoginData) = repository.userLogin(data)

    fun logout(id: String) = repository.logout(id)

    fun saveUser(data: User) = viewModelScope.launch {
        user = data
        repository.saveUser(data)
    }

    fun getUser() = repository.getUser()


    fun deleteUser(data: User) = viewModelScope.launch {
        repository.deleteUser(data)
    }

    suspend fun clearTable() = withContext(Dispatchers.IO) {
        repository.clearDataTable()
    }
}
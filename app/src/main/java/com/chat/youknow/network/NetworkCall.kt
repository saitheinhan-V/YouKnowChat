package com.chat.youknow.network

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.chat.youknow.utils.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class NetworkCall<T> {
    private lateinit var call: Call<T>

    fun makeCall(call: Call<T>): MutableLiveData<Resource<T>> {
        this.call = call
        val callBackKt = CallBackKt<T>()
        callBackKt.result.value = Resource.loading(null)
        this.call.clone().enqueue(callBackKt)
        return callBackKt.result
    }

    class CallBackKt<T> : Callback<T> {
        var result: MutableLiveData<Resource<T>> = MutableLiveData()

        override fun onFailure(call: Call<T>, t: Throwable) {
            Log.i("Network onFailure", t.localizedMessage);
            result.value = Resource.error(ErrorHandler().networkError(t.localizedMessage))
            t.printStackTrace()
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                result.value = Resource.success(response.body())
            } else {
                result.value = Resource.error(
                    ErrorHandler().apiError(response.errorBody())
                )
            }
        }
    }

    fun cancel() {
        if (::call.isInitialized) {
            call.cancel()
        }
    }
}
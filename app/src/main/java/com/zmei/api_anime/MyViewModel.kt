package com.zmei.api_anime

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyViewModel: ViewModel() {
    val imageList = mutableListOf<Image_Anime>()
    val loggingInterceptor = HttpLoggingInterceptor().apply {  level = HttpLoggingInterceptor.Level.BODY }
    val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    val retrofitClient = RetrofitClient(okHttpClient)
    private val waifuApiService = retrofitClient.retrofit.create(WaifuApiService::class.java)
    var loadedItemCount = 0
    var isLoading = false
    private val callBack = object : Callback<ImageModel?> {
        override fun onResponse(
            call: Call<ImageModel?>,
            response: Response<ImageModel?>
        ) {
            if (response.isSuccessful) {
                val image = response.body()
                if (image != null) {
                    val imageAnime = Image_Anime(image, "Image ${loadedItemCount + 1}")
                    adapter.addImage(imageAnime)
                    loadedItemCount++
                    Log.d("mylog", "${loadedItemCount}")
                    isLoading = false
                }
            }
            isLoading = false
        }

        override fun onFailure(call: retrofit2.Call<ImageModel?>, t: Throwable) {
            //Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            isLoading = false

        }
    }
    fun loadData(){
        waifuApiService.getWaifuImage().enqueue(callBack)
    }
}
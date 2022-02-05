package ru.tyupik.piclab.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONTokener
import ru.tyupik.piclab.model.Picture
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

interface PictureRepository {
    val picturesList: LiveData<List<Picture>>
    fun loadPicture(): MutableList<Picture>
}

class PictureRepositoryImpl : PictureRepository {

    private val data: MutableList<Picture> = mutableListOf()
    override val picturesList: LiveData<List<Picture>> = MutableLiveData(data)

    private val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build()

    companion object {
        const val BASE_URL =
            "https://developerslife.ru/random?json=true"
    }

    override fun loadPicture(): MutableList<Picture> {
        thread {
            val request: Request = Request.Builder()
                .url(BASE_URL)
                .build()

            client.newCall(request)
                .execute()
                .use { response ->
                    if (!response.isSuccessful) {
                        Log.e("error", "error")
                    } else
                        parseJsonData(response.body!!.string())
                }
        }
        return data
    }

    private fun parseJsonData(rawData: String) {
        val jsonObject = JSONTokener(rawData).nextValue() as JSONObject

        val gifURL = jsonObject.getString("gifURL")
        val description = jsonObject.getString("description")
        data.add(Picture(gifURL, description))
        print(data)
    }

}
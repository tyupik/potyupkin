package ru.tyupik.piclab.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.json.JSONTokener
import ru.tyupik.piclab.dto.Picture
import ru.tyupik.piclab.model.PictureState
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

interface PictureRepository {
    val picturesList: LiveData<List<Picture>>
    fun loadPicture(): MutableList<Picture>
    fun loadNextPicture(): Int
    fun loadPreviousPicture(): Int
}

class PictureRepositoryImpl : PictureRepository {

    private val data = ArrayList<Picture>()
    override val picturesList = MutableLiveData<List<Picture>>()

    val errorState = MutableLiveData(PictureState())


    private val client = OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).build()
    private var mark = 0

    companion object {
        const val BASE_URL =
            "https://developerslife.ru/random?json=true"
    }

    override fun loadPicture(): MutableList<Picture> {
        thread {
            val request: Request = Request.Builder()
                .url(BASE_URL)
                .build()
            try {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        errorState.value = PictureState()
                    } catch (e: Exception) {
                    }
                }
                client.newCall(request)
                    .execute()
                    .use { response ->
                        if (!response.isSuccessful) {
                            Log.e("error", "request error")
                        } else {
                            parseJsonData(response.body!!.string())
                            mark = data.size - 1
                            print(mark)
                        }
                    }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    try {
                        errorState.value = PictureState(error = true, firstPicture = false)
                    } catch (e: Exception) {
                    }
                }
            }
        }
        return data
    }

    override fun loadNextPicture(): Int {
        return if (mark >= data.size - 1) {
            loadPicture()
            data.size - 1
        } else {
            mark++
            mark
        }
    }

    override fun loadPreviousPicture(): Int {
        print(mark)
        return if (mark < 1) {
            0
        } else {
            mark--
            print(mark)
            mark
        }
    }

    private fun parseJsonData(rawData: String) {
        val jsonObject = JSONTokener(rawData).nextValue() as JSONObject
        val gifURL = jsonObject.getString("gifURL")
        val description = jsonObject.getString("description")
        data.add(Picture(gifURL, description))
        GlobalScope.launch(Dispatchers.Main) {
            try {
                picturesList.value = data
            } catch (e: Exception) {
            }
        }

    }

}
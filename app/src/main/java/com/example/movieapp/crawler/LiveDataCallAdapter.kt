package com.example.movieapp.crawler

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Benjamin Vouillon on 17,July,2020
 */
class LiveDataCallAdapter<ResultPage>(private val responseType: Type) :
    CallAdapter<ResultPage, LiveData<ResultPage>> {

    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<ResultPage>): LiveData<ResultPage> {
        return object : LiveData<ResultPage>() {
            internal var started = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<ResultPage> {
                        override fun onFailure(call: Call<ResultPage>, t: Throwable) {
                            TODO()
                        }

                        override fun onResponse(
                            call: Call<ResultPage>,
                            response: Response<ResultPage>
                        ) {
                            postValue(response.body())
                        }
                    })
                }
            }
        }
    }
}
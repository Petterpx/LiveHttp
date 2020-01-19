package com.cloudx.livehttp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.ktx.toast
import com.cloudx.core.LiveHttp.createApi
import com.cloudx.core.download.FileInfoKtx
import com.cloudx.core.download.over
import com.cloudx.core.download.overSchedule
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {
    private var a: TextView by Delegates.notNull()
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_download.setOnClickListener {
            val apk =
                "https://tva1.sinaimg.cn/large/006tNbRwly1gaxggh8lzag30oo0dw4ko.gif"
            lifecycleScope.launch {
//                async {
//                    createApi(ApiTest::class.java).dowload(apk).over(FileInfoKtx("test.gif"))
//                }.let {
//                    toast(it.await().toString())
//                }

                async { createApi(ApiTest::class.java).dowload(apk).over(FileInfoKtx("test.apk","test")) }.let {
                    toast(it.await().toString())
                }


            }


        }
        btn_get.setOnClickListener {
            lifecycleScope.launch {
                Log.e(
                    "petterp", createApi(ApiTest::class.java).getBaidu()
                )
            }
        }


    }
}

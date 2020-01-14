package com.cloudx.livehttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.android.ktx.toast
import com.cloudx.core.LiveHttp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import java.lang.Exception
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            lifecycleScope.launch {
                async {
                    toast(LiveHttp.createApi(ApiTest::class.java).getBaidu())
                }

            }


        }
    }

    suspend fun test() = coroutineScope {
        LiveHttp.createApi(ApiTest::class.java).getBaidu()
    }
}




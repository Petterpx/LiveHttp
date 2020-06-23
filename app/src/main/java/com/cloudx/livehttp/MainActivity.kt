package com.cloudx.livehttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.cloudx.core.LiveHttp
import com.cloudx.core.error.launchHttp
import com.cloudx.core.error.launchLfHttp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    val createApi = LiveHttp.createApi(ApiTest::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            launchLfHttp {
                val baiDu = LiveHttp.createApi(ApiTest::class.java).getBaiDu()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, baiDu, Toast.LENGTH_SHORT).show()
                }
                postM().start()

            }
        }

    }


    fun postM() = GlobalScope.async(Dispatchers.IO) {
        println("")
    }
}
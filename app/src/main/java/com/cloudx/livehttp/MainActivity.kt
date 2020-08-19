package com.cloudx.livehttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cloudx.core.LiveHttp
import com.cloudx.core.error.launchLfHttp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    val createApi = LiveHttp.createApi(ApiTest::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            launchLfHttp {
                val baiDu = createApi.getBaiDu()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, baiDu, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    fun test() {

    }
}
package com.cloudx.livehttp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.android.ktx.toast
import com.cloudx.core.LiveHttp
import com.cloudx.core.error.launchHttp
import com.cloudx.core.utils.block
import com.cloudx.core.utils.blockIO
import com.cloudx.core.utils.blockMain
import com.cloudx.core.utils.requestBody
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                launchHttp {
                    LiveHttp.createApi(ApiTest::class.java)
                        .login(requestBody {
                            mapOf("mobile" to "!23", "code" to "!231")
                        }).blockMain {  }
                }
            }
        }
    }
}



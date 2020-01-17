package com.cloudx.livehttp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.cloudx.core.LiveHttp.download
import com.cloudx.core.utils.FileDow
import com.cloudx.core.utils.obver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
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
            lifecycleScope.launch(Dispatchers.Main) {
                download(ApiTest::class.java).dowload(apk)
                    .obver(FileDow("te.apk", "test"))
                    .flowOn(Dispatchers.IO)
                    .collect {
                    Log.e("petterp","${Thread.currentThread().name}---$it")
                }
            }
        }
    }
}

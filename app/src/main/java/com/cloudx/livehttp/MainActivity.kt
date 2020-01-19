package com.cloudx.livehttp

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.liveData
import com.android.ktx.toast
import com.cloudx.core.LiveHttp.createApi
import com.cloudx.core.download.FileInfoKtx
import com.cloudx.core.download.over
import com.cloudx.core.download.overSchedule
import com.cloudx.core.error.launchHttp
import com.cloudx.core.utils.FileBean
import com.cloudx.core.utils.blockMain
import com.cloudx.core.utils.fileBody
import com.cloudx.core.utils.fileBodys
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.io.File


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    @SuppressLint("NewApi")
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val createApi = createApi(ApiTest::class.java)


        btn_get.setOnClickListener {
            lifecycleScope.launch {
                launchHttp(errorBlock = {
                    //Retrofit异常
                    Log.e("petterp",it.message)
                }) {
                    createApi.getWan().blockMain({
                        //失败处理，处理错误码，这里携带了我们错误数据，便于处理
                        //如果你定义了全局错误处理，这里相当于替换本次错误处理规则
                        //注意，这里已经成功切换为主线程
                        Log.e("petterp", it.m)

                    }) {
                        //成功处理，直接返回我们需要的数据类型
                    }
                }
            }
        }

        btn_upload.setOnClickListener {
            lifecycleScope.launch {

                createApi.uploadFile(fileBody {
                    FileBean(File("你的文件"))
                })

                //多文件
                createApi.uploadFiles(fileBodys {
                    listOf(
                        FileBean(File("你的文件")),
                        FileBean(File("你的文件"))
                    )
                })
            }
        }


        btn_download.setOnClickListener {
            val apk =
                "https://tva1.sinaimg.cn/large/006tNbRwly1gaxggh8lzag30oo0dw4ko.gif"
            lifecycleScope.launch {
                createApi.dowload(apk)
                    .over(FileInfoKtx("test.gif", "test")).let {
                        toast(it.toString())
                    }
            }
        }

        progressBar.maxWidth = 100
        btn_download_schedule.setOnClickListener {
            val apk =
                "http://s.duapps.com/apks/own/ESFileExplorer-V4.2.1.9.apk"
            lifecycleScope.launch {
                createApi.dowload(apk)
                    .overSchedule(FileInfoKtx("es.apk", "test")) {
                        toast(it.toString())
                    }.collect {
                        //此时已发到父launch所在线程，当前即为main
                        progressBar.progress = it
                    }
            }
        }


    }
}

package com.cloudx.livehttp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.cloudx.core.LiveHttp.createApi
import com.cloudx.core.error.launchHttp
import com.cloudx.core.file.*
import com.cloudx.core.utils.blockMain
import com.cloudx.core.utils.requestBody
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.lang.Exception


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MainActivity : AppCompatActivity() {
    lateinit var file: File

    @SuppressLint("NewApi")
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val serviceApi = createApi(ApiTest::class.java)

        btn_get.setOnClickListener {

            requestBody {
                mapOf(
                    "" to ""
                )
            }

            //按照LiveResponse定义自己的响应样式
            lifecycleScope.launch {
                launchHttp(errorBlock = {
                    //Retrofit异常
                    Log.e("petterp", it.message)
                }) {
                    serviceApi.getWan().blockMain({
                        //失败处理，处理错误码，这里携带了我们错误数据，便于处理
                        //如果你定义了全局错误处理，这里相当于替换本次错误处理规则
                        //注意，这里已经切换为主线程
                        Log.e("petterp", it.second)

                    }) {
                        //成功处理，直接返回我们需要的数据类型
                        Toast.makeText(this@MainActivity, "长度为${it.size}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }


                serviceApi.getWan().blockMain {

                }

            }
        }

        btn_post.setOnClickListener {
            lifecycleScope.launch {
                launchHttp {

                }
            }
        }

        btn_upload.setOnClickListener {
            lifecycleScope.launch {
                launch {
                    serviceApi.uploadFile(fileBody {
                        FileBean(File("你的文件"))
                    })

                    //多文件
                    serviceApi.uploadFiles(fileBodys {
                        listOf(
                            FileBean(File("你的文件")),
                            FileBean(File("你的文件"))
                        )
                    })
                }
            }
        }


        btn_download.setOnClickListener {
            val apk =
                "https://tva1.sinaimg.cn/large/006tNbRwly1gaxggh8lzag30oo0dw4ko.gif"
            lifecycleScope.launch {
                serviceApi.dowload(apk)
                    .over(DownloadFileKtx("test.gif", "test")).let {
                    }
            }
        }

        btn_download_schedule.setOnClickListener {
            val apk =
                "http://s.duapps.com/apks/own/ESFileExplorer-V4.2.1.9.apk"
            lifecycleScope.launch {
                serviceApi.dowload(apk)
                    .overSchedule(DownloadFileKtx("es.apk", "test")) {
                    }.collect {
                        //此时已发到父launch所在线程，当前即为main
                        progressBar.progress = it
                    }
            }
        }

        btn_upload_test_xl.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            file = File(
                Environment.getExternalStorageDirectory(), "${System.currentTimeMillis()}.png"
            )
            cameraIntent.putExtra(
                MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(
                    this,
                    this.applicationContext.packageName + ".FileProvider",
                    file
                )
            )
            startActivityForResult(cameraIntent, 110)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 110 && resultCode == Activity.RESULT_OK) {
            Log.e("petterp", "File---$file")
            lifecycleScope.launch {
                val serviceApi = createApi(ApiTest::class.java)
                val uploadXl = serviceApi.uploadXl(fileBody {
                    FileBean(file)
                }).apply {
                    Log.e("petterp", this)
                }
            }
        }
    }

}

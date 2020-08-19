# LiveHttp

[![](https://jitpack.io/v/Petterpx/LiveHttp.svg)](https://jitpack.io/#Petterpx/LiveHttp)

### 基于 Retrofit+Coroutines 的网络请求组件。

> 网络请求一直是我们开发中必不可少的组件，但通常我们往往用不到太重的组件，如果你厌烦了过重的网络框架，那么LiveHttp将是一个比较好的选择，代码简洁易看懂，通过组件简化开发难度，如果对你有所帮助，不妨点个star。

### 

### 为什么要使用它？

得益于 **Kotlin** 简洁优雅的语法与 **Coroutines** 的强大支持，LiveHttp 支持以下功能：

- 简洁的文件上传下载，已适配至Android10，使用Flow返回进度；
- 完善的异常处理机制，支持全局异常处理，单独处理，业务单独处理；
- 全局全局自定义错误配置，或者使用默认配置；
- 支持网络状态监听；
- ktx扩展...；







### 如何使用？


##### Gradle

```groovy
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```groovy
dependencies {
	        implementation 'com.github.Petterpx:LiveHttp:1.0.1'
	}
```



### 自行导入以下组件：

```groovy
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.google.code.gson:gson:2.8.6'
implementation "com.squareup.okhttp3:logging-interceptor:4.3.0"
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.8"
```

#### 如果需要LiveHttp对于ViewModel-ktx或者Lifecycle-Ktx的扩展，请加入

```groovy
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"
implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"

//ViewModel
launchVMhttp{
  
}
//FragmentActivity&Fragment
launchLfHttp{
  
}
```

#### 如果需要日志提示，请导入

```groovy
implementation 'com.orhanobut:logger:2.2.0'

//Application
LiveConfig.log()
```

#### 关于混淆：

框架内部已进行处理，无需再次处理；



#### 想定义更多参数？

```kotlin
LiveConfig
            .context(this)
            .baseUrl("https://www.baidu.com")   //baseUrl
            .writeTimeout(30L)     //设置读写超时时间，默认30l
            .connectTimeout(10L)  //设置连接超时时间,默认10l
            .isCache(true)     //开启网络缓存，默认关闭
            .filePath("xxx")    //文件下载路径
            .log()   //需要导入 logger 依赖
            .initNetObser()        //监听网络,必须在context之后
            .initNetObser(object : INetEnable {
                override fun netOpen() {
                    //网络打开
                }

                override fun netOff() {
                    //网络关闭
                }

            })
            //以下错误逻用于全局请求码处理,具体处理域皆处于挂起函数
            .errorCodeKtx(101, CodeBean {
                //默认为发起网络请求的线程，一般为io,记得调用withContext()
                //对于code=101的处理
            })
            //通用网络处理方案
            .universalErrorHttpKtx {
                //EnumException.CONNECT_EXCEPTION
                //EnumException.TIMEOUT_EXCEPTION
                //EnumException.SOCKET_EXCEPTION,
                //EnumException.NET_UNAVAILABLE, 
                Log.e("petterp", "通用网络连接-超时等失败，如网络开启，但网络其实不可用等情况")
            }
            //以下错误用于指定网络异常处理，具体处理域皆处于挂起函数
            .errorHttpKtx(EnumException.NET_DISCONNECT) {
                Log.e("petterp", "网络断开")
            }
```





***以下示例均在Activity中使用，推荐在ViewModel中使用***

### 发起一个Get请求

```kotlin
//FragmentActivity

            launchLfHttp(errorBlock = {
                    //Retrofit异常，可配置全局统一处理
                    Log.e("petterp", it.message)
                }) {
                    createApi.getWan().blockMain({
                        //失败处理，处理错误码，这里携带了我们错误数据，便于处理
                        //如果你定义了全局错误处理，这里相当于替换本次错误处理规则
                        //注意，这里已经切换为主线程
                        Log.e("petterp", it.second)

                    }) {
                        //成功处理，直接返回我们需要的数据类型
                        toast("长度为${it.size}")
                    }
            }
```

#### 可以再简单一点？

```kotlin
//异步
serviceApi.getWan().syncBlock{ 
      						//这里拿到数据
                }
//同步写法，异步处理
val res=serviceApi.getWan().block()
```





### 发起一个Post请求

##### Api

```kotlin
interface ApiTest {
    @POST("/orgframe/root")
    suspend fun login(@Body body: RequestBody): LiveResponse<Login>
}
```

```kotlin
 launchLfHttp {
      //异步
      LiveHttp.createApi(ApiTest::class.java)
          .login(requestBody {
              mapOf("mobile" to "xxxxxxxxxxx", "code" to "123")
          })
      
     //同步写法，异步处理
     val isSuccess = LiveHttp.createApi(ApiTest::class.java).login() 
}
```





### 文件上传

```kotlin
@Multipart
@POST("/upload/head")
suspend fun uploadFile(@Part file: MultipartBody.Part): String
```

#### 单文件上传

```kotlin
              launchLfHttp {
                    serviceApi.uploadFile(fileBody {
                        FileBean(File("你的文件"))
                    })
                }
```

#### 多文件上传

```kotlin
//多文件
serviceApi.uploadFiles(fileBodys {
                        listOf(
                            FileBean(File("你的文件")),
                            FileBean(File("你的文件"))
                        )
                    })
```





### 文件下载

> 注意配置文件保存路径及权限
>
> **默认存储位置**
>
> ***Android10存储位置*** 
>
> **path默认为包名**
>
> ```kotlin
> MediaStore.Downloads.RELATIVE_PATH, "Download/$path/"
> ```
>
> **Android10以下默认存储位置**
>
> ```
> sd卡/包名
> Environment.getExternalStorageDirectory().path + "/" + path + "/"
> ```
>
> ***建议手动设置默认存储路径***

#### 下载完成直接返回Uri

```kotlin
val apk ="https://tva1.sinaimg.cn/large/006tNbRwly1gaxggh8lzag30oo0dw4ko.gif"
            launchLfHttp {
                serviceApi.dowload(apk)
                    .over(DownloadFileKtx("文件名.后缀", "路径path")).let {
                        toast(it.toString())
                    }
            }
```



#### 下载加进度条

```kotlin
val apk ="http://s.duapps.com/apks/own/ESFileExplorer-V4.2.1.9.apk"
        
           launchLfHttp {
                serviceApi.dowload(apk)
                    .overSchedule(DownloadFileKtx("es.apk", "test")) {
                      	//返回Uri
                        toast(it.toString())
                    }.collect {
                        //进度条
                        progressBar.progress = it
                    }
            }
```





#### 自定义配置 **LiveResponse**

**参考 CustomResponse.kt,更改其中的变量名或者结构即可**

```kotlin
class LiveResponse<T>(val result: Result, val t: T)

class Result(var c: Int, var m: String)

inline fun <T> LiveResponse<T>.block(
    noinline blockError: ((Result) -> Unit)? = null,
    success: (T) -> Unit) {
    if (result.c == 200) {
        success(t)
    } else {
        blockError?.let { it(result) } ?: ErrorCodeKts.getCode(result.c).obj.invoke()
    }
}
//更改 LiveResponse 与 Result即可
```


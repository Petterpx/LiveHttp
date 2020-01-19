# LiveHttp

### 基于 Retrofit+Coroutines 的网络请求组件。

### 

### 为什么要使用它？

得益于 **Kotlin** 简洁优雅的语法与 **Coroutines** 的强大支持，LiveHttp 支持以下功能：

- 简洁的文件上传下载，已适配至Android10，使用Flow返回进度
- 完善的异常处理机制，支持全局异常处理，单独处理，业务单独处理
- 全局傻瓜式自定义配置，或者使用默认配置
- 得益于 kt 与 Retrofit 2.6+的特性，Get，Post ,只需要一行即可解决



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
	        implementation 'com.github.people-rmxc:LiveHttp:1.1'
	}
```

### 初始化LiveHttp

```kotlin
LiveConfig.initDefault(this,"你的baseUrl")
```

#### 想定义更多参数？

```kotlin
 LiveConfig
            .baseUrl("你的baseUrl")   //baseUrl
            .context(this)       //context
            .WriteTimeout(30L)     //设置读写超时时间，默认30l
            .connectTimeout(10L)  //设置连接超时时间,默认10l
            .isCache(true)     //开启网络缓存，默认开启
            .listInterCeptor()       //设置你的拦截器
						.filePath()    //文件下载路径
				
		

						//以下错误逻用于全局处理
            .errorKtx(101, CodeBean("这是错误码处理逻辑，全局处理") { test() })
            .errorKtx(SparseArray<CodeBean>())  //添加批量错误逻辑处理
```



***以下示例均在Activity中使用，推荐在ViewModel中使用***

### 发起一个Get请求

```kotlin
//Activity
lifecycleScope.launch {
                launchHttp(errorBlock = {
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
            }
```

#### 可以再简单一点？

> 需将异常处理放在核心base类中通一处理，搭配系统框架使用

```kotlin
serviceApi.getWan().blockMain { 
      				//完成服务器异常码处理并切换到主线程    
                }
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
lifecycleScope.launch(Dispatchers.IO) {
                launchHttp {
                    LiveHttp.createApi(ApiTest::class.java)
                        .login(requestBody {
                            mapOf("mobile" to "xxxxxxxxxxx", "code" to "123")
                        })
                        .block(blockError =
                        {
                           //错误处理,如果设置了全局处理操作，这里可以忽略
                        }
                        ) {
													//成功处理
                          //切换线程
                          withContext(Dispatchers.MAIN)
                        }
                }
}
```

##### 可以再简单一点？

```kotlin
//liveHttp-> 接口对象定义为全局
liveHttp.login(requestBody {
                mapOf("mobile" to "!23", "code" to "!231")
               }).blockMain {  }  //请求成功后自动切换Main线程并完成异常处理
```





### 文件上传

```kotlin
@Multipart
@POST("/upload/head")
suspend fun uploadFile(@Part file: MultipartBody.Part): String
```

#### 单文件上传

```kotlin
lifecycleScope.launch {
                launch {
                    serviceApi.uploadFile(fileBody {
                        FileBean(File("你的文件"))
                    })
                }
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
            lifecycleScope.launch {
                serviceApi.dowload(apk)
                    .over(DownloadFileKtx("文件名.后缀", "路径path")).let {
                        toast(it.toString())
                    }
            }
```



#### 下载加进度条

```kotlin
val apk ="http://s.duapps.com/apks/own/ESFileExplorer-V4.2.1.9.apk"
        
           lifecycleScope.launch {
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


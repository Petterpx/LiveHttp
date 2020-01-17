# LiveHttp

### 基于Retrofit+协程的网络请求组件。

### 

### 为什么要使用它？

基于Kotlin简洁优雅的语法与协程的强大支持，LiveHttp支持以下功能：

- 傻瓜式的配置信息
- 一行代码实现post,get
- 简洁的文件上传下载，并使用Flow返回进度，已适配至Android10
- 优雅的语法与Api,线程自动切换
- 完善的异常处理机制，支持全局异常处理，单独处理，业务单独处理
- 附带网络缓存(get)，日志显示拦截器
- 支持基本类型数据返回，自定义属性包装返回



todo



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
	        implementation 'com.github.people-rmxc:LiveHttp:1.0'
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

						//以下错误逻用于全局处理
            .errorKtx(101, CodeBean("这是错误码处理逻辑，全局处理") { test() })
            .errorKtx(SparseArray<CodeBean>())  //添加批量错误逻辑处理
```



### 发起一个Post请求

##### Api

```kotlin
interface ApiTest {
    @POST("/orgframe/root")
    suspend fun login(@Body body: RequestBody): LiveResponse<Login>
}
```

#### MainActiivty || ViewModel，需要运行在协程体内，推荐使用ViewModel

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



#### 自定义配置 **LiveResponse**

**参考 Response.kt,更改其中的变量名或者结构即可**

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


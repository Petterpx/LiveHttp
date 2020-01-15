# LiveHttp
### 基于Retrofit+协程的网络请求组件。

### 如何使用？

[！[]（https://jitpack.io/v/people-rmxc/LiveHttp.svg）]（https://jitpack.io/#people-rmxc/LiveHttp）

##### Gradle

```css
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

```css
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


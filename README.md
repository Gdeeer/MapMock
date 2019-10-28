# 功能
集成后会在桌面添加一个 MapMock 的图标，点击后可定位，再进入自己的应用，执行定位后拿到的就是自己点击的位置信息。

 <img src="https://img-blog.csdnimg.cn/20191028133859683.png" width = "250" height = "100" align="top"/> <img src="https://img-blog.csdnimg.cn/20191028133912989.png" width = "250" height = "500"/>
 <img src="https://img-blog.csdnimg.cn/20191028133926266.png" width = "250" height = "500"/>

# 集成

1. build.gradle 引入：

```groovy
// 根 build.gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

// app build.gradle
dependencies {
    implementation 'com.github.Gdeeer.MapMock:mapmock:0.1.0'
}
```

2. 在高德地图的定位回调里添加拦截：

```java
public void onLocationChanged(AMapLocation aMapLocation) {
    MapMock.handleRealLocation(aMapLocation);
}
```

3. 在 AndroidManifest.xml 里写入高德的 key（需要自己去申请）：

```xml
<meta-data
    android:name="com.amap.api.v2.apikey"
    android:value="xxxx">
</meta-data>
```

4. 如果需要在 release 中使用，记得混淆：

```
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
```

5. 如果只需要在 debug 包里使用 MapMock 的功能，release 不做处理：

```groovy
// debug 时，执行拦截
debugImplementation 'com.github.Gdeeer.MapMock:mapmock:0.1.0'
// release 时，不执行拦截
releaseImplementation 'com.github.Gdeeer.MapMock:mapmocknoop:0.1.0'
```

# SatelliteLayout
自定义布局，行星运动轨迹的布局

## 依赖使用
1.在build.gradle(project)中：
```
allprojects {
    repositories {
        ......
	maven { url "https://jitpack.io" }
    }
}
```
2.在build.gradle(app)中：
```
dependencies {
    ......
    implementation 'com.github.mayemonkey:Satellite:v0.01'
}
```

## 公开方法
```setAngle```                           设置当前外侧球体偏移角度
 

##使用示例

```xml
<com.maye.satelitelayout.SatelliteLayout
        android:id="@+id/sl_home"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:bCircleColor="#cc66ff"
        app:sCircleColor="#66ccff"
        android:layout_centerInParent="true">

        <RelativeLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="#ff0000">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"/>
        </RelativeLayout>

        <RelativeLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:background="#0000ff">

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"/>
        </RelativeLayout>

</com.maye.satelitelayout.SatelliteLayout>
```


```Java
sl_home = findViewById(R.id.sl_home);

ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
valueAnimator.setInterpolator(new LinearInterpolator());
valueAnimator.setDuration(3000);
valueAnimator.setRepeatCount(2);
valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float angle = (float) valueAnimator.getAnimatedValue();
        Log.i("角度值", "角度：" + angle);
        sl_home.setAngle(angle);
    }
});
valueAnimator.start();
```

##License
```
Copyright [2016] [mayemonkey]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

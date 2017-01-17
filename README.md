# AutoScrolBanner

##Getting Started

 - Add the dependency to your build.gradle.
 
```
dependencies {
    compile 'com.zhangjingjing:autoscrolbanner:1.0.1'
}
```
- Maven:

```
<dependency>
  <groupId>com.zhangjingjing</groupId>
  <artifactId>autoscrolbanner</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

## Usage
- xml

```

    <com.zhangjingjing.autoscrolbanner.CycleViewpager
        android:id="@+id/vp_auto"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
        
     <com.zhangjingjing.autoscrolbanner.PagerIndicator
        android:id="@+id/pi_indicator"
        app:indicator_font_color="@color/colorAccent"
        app:indicator_font_size="12sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:layout_marginBottom="10dp"
        app:indicator_type="number"
        />
            
        .......
     
```

- code

```
   mAutoVp = (CycleViewpager) findViewById(R.id.vp_auto);
        mIndicator = (PagerIndicator) findViewById(R.id.pi_indicator);
        MyPagerAdapter adapter = new MyPagerAdapter(this, imgUrls);
        mAutoVp.setAdapter(adapter);
        mAutoVp.startAutoScroll();
        mIndicator.setViewpager(mAutoVp);
```

## Todo

 支持viewpager切换动画配置 <br>
 支持indicator切换动画配置 <br>
 优化

## License

    Copyright 2016 13663797524@163.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

# :bus:RoateIndicatorViewDemo
高仿转转APP首页banner的指示器控件

## 简介

图片轮播指示器网络上有很多种，大部分是一些相邻间隔点，转转APP中给出了一种新的思路，本项目模仿转转APP将指示器封装成一个库，方便第三方应用使用。

下图是我在LOL视频宝中的使用截图。


![](http://7bvaky.com2.z0.glb.qiniucdn.com/2017-01-10_20_02_51_1.png)

动态效果：

![](http://7bvaky.com2.z0.glb.qiniucdn.com/2017-01-10_20_35_40_2.gif)  

[显示完整图片](http://7bvaky.com2.z0.glb.qiniucdn.com/2017-01-10_20_35_40_2.gif)
## 接入

[![](https://jitpack.io/v/hust201010701/RoateIndicatorViewDemo.svg)](https://jitpack.io/#hust201010701/RoateIndicatorViewDemo)


- **Gradle**

	To get a Git project into your build:

	### Step 1. Add the JitPack repository to your build file
	
	Add it in your root build.gradle at the end of repositories:
	
		allprojects {
			repositories {
				...
				maven { url 'https://jitpack.io' }
			}
		}
	### Step 2. Add the dependency
	
		dependencies {
			compile 'com.github.User:Repo:Tag'
		}
	

- **本地导入**

	下载本项目并解压，把项目中的rotateindicatorview 模块导入到 项目中，并添加引用。

## 使用

### 1.添加到xml布局中

使用FrameLayout将ViewPager包裹起来，并添加`RotateIndicatorView`

	<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="150dp"
    android:orientation="vertical"
    android:background="#005555"
    tools:context="com.orzangleli.rotateindicatorviewdemo.MainActivity">

    <android.support.v4.view.ViewPager
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="150dp"
        />

    <com.orzangleli.rotateindicatorview.RotateIndicatorView
        android:id="@+id/rotateIndicatorView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="bottom|right"
        android:layout_marginRight="10dp"
        android:layout_marginBottom="10dp"
        app:diameter="15dp"
        app:maxPage="3"
        />


	</FrameLayout>

### 2.在Activity中使用

	//设置指示器的最大页数
    mRotateIndicatorView.setMaxPage(viewList.size());
    // 将指示器绑定到mViewPager上
    mRotateIndicatorView.bindViewPager(mViewPager);

### 3.自定义属性

- **diameter**： 指示器左边圆圈的直径（即指示器的高度，建议15dp左右）
- **textColor** ： 左边文字的颜色（默认红色）
- **textBackgroundColor** : 左边圆圈的背景颜色
- **maxPage** ： 最大页数（将显示在右边部分）

### 4.自动轮播功能

在代码中添加定时器，类似于下面

	//定时自动播放
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                if (mViewPager.getCurrentItem() == Integer.MAX_VALUE - 1) {
                    currentIndex = -1;
                }
                currentIndex = mViewPager.getCurrentItem();
                message.arg1 = currentIndex + 1;
                mHandler.sendMessage(message);
            }
        },1000,2000);

在Handler中处理消息：

	//定时轮播图片，需要在主线程里面修改 UI
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mViewPager.setCurrentItem(msg.arg1,true);
            }
        }
    };

具体使用代码可以参考：[MainActivity.java](https://github.com/hust201010701/RoateIndicatorViewDemo/blob/master/app/src/main/java/com/orzangleli/rotateindicatorviewdemo/MainActivity.java)

相关技术剖析在我的博客上： [http://www.orzangleli.com/2016/11/10/2016-11-19_%E8%BD%AC%E8%BD%ACIndicator/](http://www.orzangleli.com/2016/11/10/2016-11-19_%E8%BD%AC%E8%BD%ACIndicator/)

## MIT License

Copyright (c) 2017 orzangleli

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

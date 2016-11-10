package com.orzangleli.rotateindicatorviewdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.orzangleli.rotateindicatorview.RotateIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    RotateIndicatorView mRotateIndicatorView;
    ViewPager mViewPager;

    List<ImageView> viewList ;

    int currentIndex = 0;

    Timer timer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRotateIndicatorView = (RotateIndicatorView)this.findViewById(R.id.rotateIndicatorView);

        mViewPager = (ViewPager)this.findViewById(R.id.viewPager);

        ImageView imageView1 = new ImageView(this);
        ImageView imageView2 = new ImageView(this);
        ImageView imageView3 = new ImageView(this);


        ViewGroup.LayoutParams viewPagerImageViewParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        imageView1.setLayoutParams(viewPagerImageViewParams);
        imageView1.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView2.setLayoutParams(viewPagerImageViewParams);
        imageView2.setScaleType(ImageView.ScaleType.FIT_XY);

        imageView3.setLayoutParams(viewPagerImageViewParams);
        imageView3.setScaleType(ImageView.ScaleType.FIT_XY);


        imageView1.setImageResource(R.drawable.img1);
        imageView2.setImageResource(R.drawable.img2);
        imageView3.setImageResource(R.drawable.img3);

        viewList = new ArrayList<ImageView>();
        viewList.add(imageView1);
        viewList.add(imageView2);
        viewList.add(imageView3);

        //设置指示器的最大页数
        mRotateIndicatorView.setMaxPage(viewList.size());

        //设置适配器
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentIndex);

        mRotateIndicatorView.bindViewPager(mViewPager);

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


    }

    //定时轮播图片，需要在主线程里面修改 UI
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mViewPager.setCurrentItem(msg.arg1,true);
            }
        }
    };



    public PagerAdapter mPagerAdapter = new PagerAdapter() {
        int instantiatePosition =0;  //保存上一个初始化的位置
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i("lxc","destroyItem ="+position);
            //container.removeView(viewList.get(position%viewList.size()));

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.i("lxc","instantiateItem ="+position);

            if (viewList.get(position % viewList.size()).getParent() != null) {
                ((ViewPager) viewList.get(position % viewList.size())
                        .getParent()).removeView(viewList.get(position
                        % viewList.size()));
            }

            if(position > instantiatePosition)
            {
                //代表右滑
                ((ViewPager) container).addView(
                        viewList.get(position % viewList.size()), container.getChildCount()-1);

            }
            else
            {
                //代表左滑
                ((ViewPager) container).addView(
                        viewList.get(position % viewList.size()), 0);
            }

            instantiatePosition = position;

            return viewList.get(position % viewList.size());
        }
    };
}

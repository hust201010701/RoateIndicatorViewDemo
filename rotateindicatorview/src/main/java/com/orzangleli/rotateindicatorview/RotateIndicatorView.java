package com.orzangleli.rotateindicatorview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by 81118 on 2016/11/7.
 * Summary:完成模仿转转APP首页上的轮播Indicator自定义空间的效果
 *
 */
public class RotateIndicatorView extends View implements ViewPager.OnPageChangeListener {

    ViewPager mViewPager;
    //圆点直径
    private int diameter;
    private int textColor;
    private int textBackgroundColor;
    private int maxPage;

    //当前页数
    private int currentPage = 1;
    //当前旋转的角度
    private float rotateAngle = 0.0f;

    private Bitmap rightBitmap;
    private Paint mPaint;
    private Camera camera;

    public RotateIndicatorView(Context context) {
        this(context, null, 0);
    }

    public RotateIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RotateIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RotateIndicatorView, defStyleAttr, 0);
        diameter = array.getDimensionPixelSize(R.styleable.RotateIndicatorView_diameter, 50);
        textColor = array.getColor(R.styleable.RotateIndicatorView_textColor, Color.RED);
        textBackgroundColor = array.getColor(R.styleable.RotateIndicatorView_textBackgroundColor, Color.WHITE);
        maxPage = array.getInt(R.styleable.RotateIndicatorView_maxPage, 1);

        rightBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.indicator2);
        Matrix matrix = new Matrix();
        float scale = 1.0f * diameter / rightBitmap.getHeight();
        matrix.postScale(scale, scale);
        rightBitmap = Bitmap.createBitmap(rightBitmap, 0, 0, rightBitmap.getWidth(), rightBitmap.getHeight(), matrix, true);

        mPaint = new Paint();
        camera = new Camera();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RotateIndicatorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = (int) (diameter * 3.4f);
        int height = diameter;
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
        //画右边固定不变的部分
        drawRightArea(canvas);

    }
    //判断每次滑动图片时是不是第一次进入
    private boolean firstTimeTouch = false;
    //判断旋转到90度时，是否应该加1，true为加1，false为减1
    private boolean add = true;
    private int scrollState = 0;

    private void drawCircle(Canvas canvas) {

        canvas.save();
        Matrix matrix = new Matrix();
        camera.save();
        camera.rotateY(rotateAngle);
        camera.getMatrix(matrix);
        camera.restore();

        mPaint.setColor(textBackgroundColor);
        int centerX = diameter / 2;
        int centerY = diameter / 2;
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
        canvas.concat(matrix);

        canvas.drawCircle(diameter / 2, diameter / 2, diameter / 2, mPaint);

        canvas.restore();

        mPaint.setColor(textColor);

        //画文字
        Rect rect = new Rect(0, 0, diameter, diameter);//右边的背景图片的Rect
        mPaint.setTextSize((int) (diameter * 0.6f));
        mPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        mPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式

        if (firstTimeTouch) {
            if (rotateAngle > 0 && rotateAngle <= 90)
                add = true;
            else
                add = false;
            firstTimeTouch = false;
        }


        if ((add && rotateAngle >= 0 && rotateAngle <= 90)
                ||(!add && rotateAngle > 90 && rotateAngle <= 180)) {
            canvas.save();
            matrix = canvas.getMatrix();
            camera.save();
            if(add)
                camera.rotateY(rotateAngle);
            else
                camera.rotateY(180-rotateAngle);
            camera.getMatrix(matrix);
            camera.restore();
            matrix.preTranslate(-centerX, -centerY);
            matrix.postTranslate(centerX, centerY);
            canvas.concat(matrix);

            canvas.drawText(currentPage + "", rect.centerX(), baseLineY, mPaint);
            canvas.restore();
        }
        //圆圈在90度时候，开始重新写数字，写下一个数字
        else {


            if (scrollState == 1) {
                //这里需要将canvas在旋转90度，因为文字对观众来说，始终时正对的
                canvas.save();
                matrix = canvas.getMatrix();
                camera.save();
                if(add)
                    camera.rotateY(180 - rotateAngle);
                else
                    camera.rotateY(rotateAngle);
                camera.getMatrix(matrix);
                camera.restore();
                matrix.preTranslate(-centerX, -centerY);
                matrix.postTranslate(centerX, centerY);
                canvas.concat(matrix);

                if (add) {
                    if (currentPage + 1 <= maxPage)
                        canvas.drawText(currentPage + 1 + "", rect.centerX(), baseLineY, mPaint);
                    else
                        canvas.drawText(1 + "", rect.centerX(), baseLineY, mPaint);
                } else {
                    if (currentPage - 1 >= 1)
                        canvas.drawText(currentPage - 1 + "", rect.centerX(), baseLineY, mPaint);
                    else
                    {
                        if(mViewPager.getCurrentItem() != 0)
                            canvas.drawText(maxPage + "", rect.centerX(), baseLineY, mPaint);
                        else{
                            //如果当前已经是第一页了，则不能进行变换
                            canvas.restore();
                            canvas.drawText(currentPage+ "", rect.centerX(), baseLineY, mPaint);
                            return;
                        }
                    }
                }

                canvas.restore();

            } else
                canvas.drawText(currentPage + "", rect.centerX(), baseLineY, mPaint);


        }

    }

    private void drawRightArea(Canvas canvas) {
        canvas.drawBitmap(rightBitmap, diameter, 0, mPaint);
        mPaint.setColor(Color.WHITE);

        //画文字
        Rect rect = new Rect(diameter, 0, (int) (diameter * 3.4f), diameter);//右边的背景图片的Rect
        Paint textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize((int) (diameter * 0.6f));
        textPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        textPaint.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (rect.centerY() - top / 2 - bottom / 2);//基线中间点的y轴计算公式
        canvas.drawText("of " + maxPage, rect.centerX(), baseLineY, textPaint);
    }

    public void bindViewPager(ViewPager viewPager) {
        viewPager.setOnPageChangeListener(this);
        mViewPager = viewPager;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        rotateAngle = 180 * positionOffset;
        Log.i("lxc", "position=" + position + " positionOffset=" + positionOffset + " positionOffsetPixels=" + positionOffsetPixels);
        postInvalidate();
    }

    @Override
    public void onPageSelected(int position) {
        currentPage = position % maxPage + 1;
        postInvalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.i("lxc222", "state=" + state);
        scrollState = state;
        //正在滑动
        if (state == 1)
            firstTimeTouch = true;
        else
            firstTimeTouch = false;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getTextBackgroundColor() {
        return textBackgroundColor;
    }

    public void setTextBackgroundColor(int textBackgroundColor) {
        this.textBackgroundColor = textBackgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
}

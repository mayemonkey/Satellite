package com.maye.satelitelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class SatelliteLayout extends ViewGroup {

    private static final int CENTER = 1;
    private static final int SIDE = 2;

    private int sBallColor = Color.RED;
    private int bBallColor = Color.BLACK;

    //大圆半径
    private Paint bPaint;
    private float bRadius = 0f;
    private PointF bCenterPoint;

    //小圆半径
    private Paint lPaint;
    private float sRadius = 0f;

    //角度
    private double angle = 360;

    //大圆相对小圆倍数
    private float ballWeight = 8.5f;
    private float firstX;
    private float firstY;

    //内侧圆点击
    public interface OnCenterCircleClickedListener {
        void onClicked();
    }

    OnCenterCircleClickedListener listener_center;

    public void setOnCenterCircleClickedListener(OnCenterCircleClickedListener listener_center) {
        this.listener_center = listener_center;
    }

    public interface OnSideCircleClickedListener {
        void onClicked();
    }

    OnSideCircleClickedListener listener_side;

    public void setOnSideCircleClickedListener(OnSideCircleClickedListener listener_side) {
        this.listener_side = listener_side;
    }

    public SatelliteLayout(Context context) {
        this(context, null);
    }

    public SatelliteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray circleBall = context.obtainStyledAttributes(attrs, R.styleable.SatelliteLayout);

        bBallColor = circleBall.getColor(R.styleable.SatelliteLayout_bCircleColor, Color.BLACK);
        sBallColor = circleBall.getColor(R.styleable.SatelliteLayout_sCircleColor, Color.RED);
        ballWeight = circleBall.getFloat(R.styleable.SatelliteLayout_ballWeight, 5f);
        angle = circleBall.getInt(R.styleable.SatelliteLayout_angle, 0);

        circleBall.recycle();

        init();

        setWillNotDraw(false);
    }


    private void init() {
        bPaint = new Paint();
        bPaint.setAntiAlias(true);

        lPaint = new Paint();
        lPaint.setAntiAlias(true);

        bCenterPoint = new PointF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        View bCircle = getChildAt(0);
        View sCircle = getChildAt(1);
        int bSize = measureChildSize(bCircle, widthMeasureSpec, heightMeasureSpec);
        int sSize = measureChildSize(sCircle, widthMeasureSpec, heightMeasureSpec);
        Log.i("圆内布局", "内侧：" + bSize);
        Log.i("圆内布局", "外侧：" + sSize);

        if (widthMode == MeasureSpec.EXACTLY || heightMode == MeasureSpec.EXACTLY) {
            //获取SatelliteLayout宽高度
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(widthMeasureSpec);

            int size = widthSize >= heightSize ? heightSize : widthSize;

            float baseWeight = 1 / (ballWeight + 1);
            Log.e("基础权重", "" + baseWeight);

            //计算大小圆半径
            if (ballWeight == 0) {
                sRadius = 0;
                bRadius = size / 2;
            } else {
                sRadius = size / 2 * baseWeight;
                bRadius = size / 2 * baseWeight * ballWeight;
            }

            Log.e("父布局测绘->大圆半径", "" + bRadius);
            Log.e("父布局测绘->小圆半径", "" + sRadius);

            bCenterPoint.set(bRadius, bRadius);
            bCenterPoint.offset(sRadius, sRadius);
            setMeasuredDimension(size, size);
        } else {
            bRadius = (float) (bSize / 2 / Math.sin(45 * Math.PI / 180));
            sRadius = (float) (sSize / 2 / Math.sin(45 * Math.PI / 180));

            ballWeight = sRadius != 0 ? bRadius / sRadius : 0;

            Log.e("子布局测绘->大圆半径", "" + bRadius);
            Log.e("子布局测绘->小圆半径", "" + sRadius);

            bCenterPoint.set(bRadius, bRadius);
            bCenterPoint.offset(sRadius, sRadius);

            setMeasuredDimension((int) (2 * (bRadius + sRadius)), (int) (2 * (bRadius + sRadius)));
        }
    }

    /**
     * 测量子布局宽高
     */
    private int measureChildSize(View child, int widthMeasureSpec, int heightMeasureSpec) {
        if (child != null) {
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int width = resolveSize(child.getMeasuredWidth(), widthMeasureSpec);
            Log.i("测量子布局", "宽：" + width);
            int height = resolveSize(child.getMeasuredHeight(), heightMeasureSpec);
            Log.i("测量子布局", "高：" + height);
            return width >= height ? width : height;
        }
        return 0;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() != 2) {
            try {
                throw new Exception("SatelliteLayout中需要包含两个子控件");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        View centerLayout = getChildAt(0);
        View sideLayout = getChildAt(1);


        //圆内最大布局，布局与圆边界接触点角度为45°,获取一个正方形的布局
        if (centerLayout != null) {
            int left = (int) (sRadius + bRadius - (bRadius * Math.sin(45 * Math.PI / 180)));
            int right = (int) (sRadius + bRadius + Math.sin(45 * Math.PI / 180) * bRadius);

            centerLayout.layout(left, left, right, right);
        }

        if (sideLayout != null) {
            int left = (int) (sRadius + bRadius + bRadius * Math.sin(angle * Math.PI / 180) - (sRadius * Math.sin(45 * Math.PI / 180)));
            int top = (int) (sRadius + bRadius - (bRadius * Math.cos(angle * Math.PI / 180) + (sRadius * Math.sin(45 * Math.PI / 180))));
            int right = (int) (left + 2 * (sRadius * Math.sin(45 * Math.PI / 180)));
            int bottom = (int) (top + 2 * (sRadius * Math.sin(45 * Math.PI / 180)));

            sideLayout.layout(left, top, right, bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bPaint.setStyle(Paint.Style.FILL);
        bPaint.setColor(bBallColor);

        lPaint.setStyle(Paint.Style.FILL);
        lPaint.setColor(sBallColor);

        canvas.drawCircle(bCenterPoint.x, bCenterPoint.y, bRadius, bPaint);

        float xOffset = (float) (Math.sin(angle * Math.PI / 180) * bRadius);
        Log.e("X轴偏移", "xOffset:" + xOffset + "--X:" + (bCenterPoint.x + xOffset));
        float yOffset = (float) (Math.cos(angle * Math.PI / 180) * bRadius);
        Log.e("Y轴偏移", "yOffset:" + yOffset + "--Y:" + (bCenterPoint.y - yOffset));

        canvas.drawCircle(bCenterPoint.x + xOffset, bCenterPoint.y - yOffset, sRadius, lPaint);
    }


    private int checkArea(float x, float y) {

        //外侧圆圆心坐标
        float sideX = (float) (sRadius + bRadius + bRadius * Math.sin(angle * Math.PI / 180));
        float sideY = (float) (sRadius + bRadius - (bRadius * Math.cos(angle * Math.PI / 180)));

        //内侧圆圆心坐标
        float centerX = sRadius + bRadius;

        //外侧圆范围内
        if (x >= sideX - sRadius && x <= sideX + sRadius) {
            double touchAngel = Math.acos(Math.abs(x - sideX) / sRadius);
            double offsetY = Math.sin(touchAngel) * sRadius;
            if (y >= sideY - offsetY && y <= sideY + offsetY) {
                return SIDE;
            }
        }

        //内侧圆范围内
        if (x >= centerX - bRadius && x <= centerX + bRadius) {
            double touchAngel = Math.acos(Math.abs(x - centerX) / bRadius);
            double offsetY = Math.sin(touchAngel) * bRadius;
            if (y >= centerX - offsetY && y <= centerX + offsetY) {
                return CENTER;
            }
        }

        //非预定范围
        return -1;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                firstX = event.getX();
                firstY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float x = event.getX();
                float y = event.getY();

                //点击事件
                if (x == firstX && y == firstY) {
                    int i = checkArea(x, y);
                    switch (i) {
                        case SIDE:
                            if (listener_side != null){
                                listener_side.onClicked();
                            }
                            break;

                        case CENTER:
                            if (listener_center != null){
                                listener_center.onClicked();
                            }
                            break;
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 设置大圆颜色
     */
    public void setBigBallColor(int mBallColor) {
        this.bBallColor = mBallColor;
    }

    /**
     * 设置小圆颜色
     */
    public void setSmallBallColor(int mBallColor) {
        this.sBallColor = mBallColor;
    }


    /**
     * 设置大布局半径
     */
    public void setRadius(float radius) {
        this.bRadius = radius;
        bCenterPoint.set(radius, radius);
    }

    public float getRadius() {
        return bRadius;
    }

    /**
     * 设置小圆角度
     */
    public void setAngle(double angle) {
        this.angle = angle;
        requestLayout();
    }

}

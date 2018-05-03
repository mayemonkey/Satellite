package com.maye.satelitelayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class SatelliteLayout extends ViewGroup {

    private int sBallColor = Color.RED;
    private int bBallColor = Color.BLACK;

    //大圆半径
    private Paint bPaint;
    private float bRadius = 8.5f;
    private PointF bCenterPoint;

    //小圆半径
    private Paint lPaint;
    private float sRadius = 1.5f;

    //角度
    private double angle = 360;

    //大圆相对小圆倍数
    private float ballWeight = 5;

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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() != 2){
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

        //获取SatelliteLayout宽高度
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);

        int size = widthSize >= heightSize ? heightSize : widthSize;

        float baseWeight = 1 / (ballWeight + 1);
        Log.e("基础权重", "" + baseWeight);

        //计算大小圆半径
        sRadius = size / 2 * baseWeight;
        bRadius = size / 2 * baseWeight * ballWeight;

        Log.e("大圆半径", "" + bRadius);
        Log.e("小圆半径", "" + sRadius);

        bCenterPoint.set(bRadius, bRadius);
        bCenterPoint.offset(sRadius, sRadius);
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

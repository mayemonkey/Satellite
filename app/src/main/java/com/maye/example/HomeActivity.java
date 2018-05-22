package com.maye.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.maye.satelitelayout.SatelliteLayout;

public class HomeActivity extends AppCompatActivity {

    private SatelliteLayout sl_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponent();
    }

    private void initComponent() {
        sl_home = findViewById(R.id.sl_home);

        sl_home.setOnCenterCircleClickedListener(new SatelliteLayout.OnCenterCircleClickedListener() {
            @Override
            public void onClicked() {
                Log.i("点击事件", "内侧圆点击");
            }
        });

        sl_home.setOnSideCircleClickedListener(new SatelliteLayout.OnSideCircleClickedListener() {
            @Override
            public void onClicked() {
                Log.i("点击事件", "外侧圆点击");
            }
        });

        TextView tv_b = findViewById(R.id.tv_b);
        tv_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("点击事件,", "TextView");
            }
        });

//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.setDuration(10000);
//        valueAnimator.setRepeatCount(2);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator valueAnimator) {
//                float angle = (float) valueAnimator.getAnimatedValue();
////                Log.i("角度值", "角度：" + angle);
//                sl_home.setAngle(angle);
//            }
//        });
//        valueAnimator.start();
    }
}

package com.litecdows.androidbluetoothserial.demoapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class RockerViewModel extends View {
    private Paint outerCirclePaint;
    private Paint innerCirclePaint;
    /** 内圆中心x坐标 */
    private double innerCenterX;
    /** 内圆中心y坐标 */
    private double innerCenterY;
    /** view中心点x坐标 */
    private float viewCenterX;
    /** view中心点y左边 */
    private float viewCenterY;
    /** view宽高大小，设定宽高相等 */
    private int size;
    /** 外圆半径 */
    private int outerCircleRadius;
    /** 内圆半径 */
    private int innerCircleRadius;

    private OnDownActionListener mDown = null;
    private OnMoveActionListener mMove = null;
    private OnUpActionListener mUp = null;



    public RockerViewModel(Context context) {
        super(context);
        init();
    }

    public RockerViewModel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        outerCirclePaint = new Paint();
        outerCirclePaint.setColor(getResources().getColor(R.color.green));
        outerCirclePaint.setAntiAlias(true);
        outerCirclePaint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.INNER));

        innerCirclePaint = new Paint();
        innerCirclePaint.setAlpha(130);
        innerCirclePaint.setColor(getResources().getColor(R.color.deep_green));
        innerCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        size = getMeasuredWidth();
        setMeasuredDimension(size, size);

        innerCenterX = size/2;
        innerCenterY = size/2;
        viewCenterX = size/2;
        viewCenterY = size/2;
        outerCircleRadius = size/2;
        innerCircleRadius = size/5;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(viewCenterX, viewCenterY, outerCircleRadius, outerCirclePaint);

        canvas.drawCircle((float) innerCenterX, (float) innerCenterY, innerCircleRadius, innerCirclePaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        double m_x = (innerCenterX-viewCenterX)/(outerCircleRadius-innerCircleRadius);
        double m_y = (innerCenterY-viewCenterY)/(outerCircleRadius-innerCircleRadius);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleEvent(event);
                if (mDown != null) {
                    mDown.OnDown(m_x, m_y);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                handleEvent(event);
                if (mMove != null) {
                    mMove.OnMove(m_x, m_y);
                }
                break;
            case MotionEvent.ACTION_UP:
                restorePosition();
                if (mUp != null) {
                    mUp.OnUp(m_x, m_y);
                }
                break;
        }

        return true;
    }

    /**
     * 处理手势事件
     */
    private void handleEvent(MotionEvent event) {
        double distance = Math.sqrt(Math.pow(event.getX()-viewCenterX, 2) + Math.pow(event.getY()-viewCenterY, 2)); //触摸点与view中心距离
        if (distance < outerCircleRadius-innerCircleRadius) {
            //在自由域之内，触摸点实时作为内圆圆心
            innerCenterX = event.getX();
            innerCenterY = event.getY();
            invalidate();
        } else {
            //在自由域之外，内圆圆心在触摸点与外圆圆心的线段上
            updateInnerCircelCenter(event);
        }
    }

    /**
     * 在自由域外更新内圆中心坐标
     */
    private void updateInnerCircelCenter(MotionEvent event) {
        double distance = Math.sqrt(Math.pow(event.getX()-viewCenterX, 2) + Math.pow(event.getY()-viewCenterY, 2));  //当前触摸点到圆心的距离
        int innerDistance = outerCircleRadius-innerCircleRadius;  //内圆圆心到中心点距离
        //相似三角形的性质，两个相似三角形各边比例相等得到等式
        innerCenterX = (event.getX()-viewCenterX)*innerDistance/distance + viewCenterX;
        innerCenterY = (event.getY()-viewCenterY)*innerDistance/distance + viewCenterY;

        invalidate();
    }

    /**
     * 恢复内圆到view中心位置
     */
    private void restorePosition() {
        innerCenterX = viewCenterX;
        innerCenterY = viewCenterY;
        invalidate();
    }

    // 为每个接口设置监听器
    public void setOnDownActionListener(OnDownActionListener down) {
        mDown = down;
    }
    public void setOnMoveActionListener(OnMoveActionListener move) {
        mMove = move;
    }
    public void setOnUpActionListener(OnUpActionListener up) {
        mUp = up;
    }
    // 定义三个接口
    public interface OnDownActionListener {
        public void OnDown(double x, double y);
    }
    public interface OnMoveActionListener {
        public void OnMove(double x, double y);
    }
    public interface OnUpActionListener {
        public void OnUp(double x, double y);
    }

}

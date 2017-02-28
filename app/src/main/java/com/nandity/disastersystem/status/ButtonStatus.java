package com.nandity.disastersystem.status;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * Created by 11540 on 2016/12/13.
 */
public interface ButtonStatus {
    /**
     * @return 对应的Status值
     */
    Status getStatus();

    /**
     * 这个状态的事件处理代理
     * @param mEvent
     * @return
     */
    boolean onTouchEvent(MotionEvent mEvent);

    /**
     * 这个状态的绘制代理
     * @param mCanvas
     * @param mPaint
     */
    void onDraw(Canvas mCanvas, Paint mPaint);
}

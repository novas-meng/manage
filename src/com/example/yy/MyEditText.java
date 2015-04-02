package com.example.yy;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;


public class MyEditText extends EditText {
    // ���� �������»���
    private Paint paint;

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GRAY);
        // ��������� �Ϻ��ڴ�
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // �õ�������
        int lineCount = getLineCount();
        // �õ�ÿ�еĸ߶�
        int lineHeight = getLineHeight();
        // ��������ѭ������
        for (int i = 0; i < lineCount; i++) {
            int lineY = (i + 1) * lineHeight;
            canvas.drawLine(0, lineY, this.getWidth(), lineY, paint);
        }

    }

}
package huahui.com.animation360.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by fengding on 2016/11/13.
 */
public class MyProgressView extends View {
    private int width = 200;
    private int height = 200;
    private Paint circlePaint;
    private Paint progressPaint;
    private Paint textPaint;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Path path = new Path();
    private int progress = 50;
    private int max = 100;
    private GestureDetector detector;
    public MyProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyProgressView(Context context) {
        super(context);
        init();
    }

    public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.argb(0xff, 0x3a, 0x8c, 0x6c));

        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setColor(Color.argb(0xff, 0x4e, 0xc9, 0x63));
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(25);

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        detector = new GestureDetector(new MyGestureDetectorListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return detector.onTouchEvent(event);
            }
        });
        setClickable(true);
    }

    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Toast.makeText(getContext(), "双击啦", Toast.LENGTH_SHORT).show();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Toast.makeText(getContext(), "单击啦", Toast.LENGTH_SHORT).show();
            return super.onSingleTapConfirmed(e);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        bitmapCanvas.drawCircle(width / 2, height / 2, width / 2, circlePaint);
        path.reset();
        float y = (1 - (float) progress / max) * height;
        path.moveTo(width, y);
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.lineTo(0, y);
        for (int i = 0; i < 5; i++) {
            path.rQuadTo(10, -10, 20, 0);
            path.rQuadTo(10, 10, 20, 0);
        }
        path.close();
        bitmapCanvas.drawPath(path, progressPaint);

        String text = (int) (((float) progress / max) * 100) + "%";
        float textwidth = textPaint.measureText(text);
        Paint.FontMetrics metrics = textPaint.getFontMetrics();
        float baseLine = height / 2 - (metrics.ascent + metrics.descent) / 2;
        bitmapCanvas.drawText(text, width / 2 - textwidth / 2, baseLine, textPaint);
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}

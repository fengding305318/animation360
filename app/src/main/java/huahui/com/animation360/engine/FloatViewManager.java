package huahui.com.animation360.engine;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;

import huahui.com.animation360.view.FloatCircleView;
import huahui.com.animation360.view.FloatMenuView;

/**
 * Created by fengding on 2016/11/6.
 */
public class FloatViewManager {
    private Context context;
    private WindowManager wm;//通过这个windowmanager来操控浮窗体以及位置的改变
    private static FloatViewManager instance;
    private FloatCircleView circleView;
    private float startx;
    private float starty;
    private float x0;
    private float y0;
    private WindowManager.LayoutParams params;
    FloatMenuView floatMenuView;
    private View.OnTouchListener circleviewTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startx = event.getRawX();//获取手机屏幕上的坐标
                    starty = event.getRawY();
                    x0 = event.getRawX();
                    y0 = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float y = event.getRawY();
                    float dx = x - startx;
                    float dy = y - starty;
                    params.x += dx;
                    params.y += dy;
                    circleView.setDragState(true);
                    wm.updateViewLayout(circleView, params);
                    startx = x;
                    starty = y;
                    break;
                case MotionEvent.ACTION_UP:
                    float x1 = event.getRawX();
                    if (x1 > getScreenWidth() / 2) {
                        params.x = getScreenWidth() - circleView.width;
                    } else {
                        params.x = 0;
                    }
                    circleView.setDragState(false);
                    wm.updateViewLayout(circleView, params);
                    if (Math.abs(x1 - x0) > 6) {
                        return true;//拖
                    } else {
                        return false;//点击
                    }
                default:
                    break;
            }
            return false;
        }
    };
    public int getScreenWidth() {
        return wm.getDefaultDisplay().getWidth();
    }
    public int getScreenHeight() {
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 反射机制
     *
     * @return
     */
    public int getStatusHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.RSdimen");
            Object o = c.newInstance();
            Field field = c.getField("status bar height");
            int x = (Integer)field.get(0);
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            return 0;
        }
    }
    private FloatViewManager(final Context context) {
        this.context = context;
        wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        circleView = new FloatCircleView(context);
        circleView.setOnTouchListener(circleviewTouchListener);
        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "onclick", 0).show();
                //隐藏circleview  显示菜单栏，开启动画
                wm.removeView(circleView);
                showFloatMenuView();
                floatMenuView.startAnimation();
            }
        });
        floatMenuView = new FloatMenuView(context);
    }
    private void showFloatMenuView() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.width = getScreenWidth();
        params.height = getScreenHeight()-getStatusHeight();
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        wm.addView(floatMenuView, params);
    }

    public static FloatViewManager getInstance(Context context) {
        if (instance == null) {
            synchronized (FloatViewManager.class) {
                if (instance == null) {
                    instance = new FloatViewManager(context);
                }
            }
        }
        return instance;
    }

    /**
     *展示富川小球到窗口上
     */
    public void showFloatCircleView() {
        if (params==null) {
            params = new WindowManager.LayoutParams();
            params.width = circleView.width;
            params.height = circleView.height;
            params.gravity = Gravity.TOP | Gravity.LEFT;
            params.x = 0;
            params.y = 0;
            params.type = WindowManager.LayoutParams.TYPE_PHONE;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            params.format = PixelFormat.RGBA_8888;
        }
        wm.addView(circleView, params);
    }


    public void hideFloatMenuView() {
        wm.removeView(floatMenuView);
    }
}

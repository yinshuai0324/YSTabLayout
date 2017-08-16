package com.example.admin.ystablayout.view;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.ystablayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 尹帅 on 2017/8/12.
 *
 * 1317972280@qq.com
 *
 * 作者:尹帅
 */
public class YSTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    public static final int SCENE_WIDTH = 1;
    public static final int SCENE_HEIGHT = 2;

    private TabViewLinearLayout tab;
    private List<View> views = new ArrayList<>();
    private ViewPager viewPager;
    private View lastView;
    private Context mContext;

    /***
     * 参数设置
     ***/
    private int margin = 0;//只针对左右的外边距
    private int defaultoption = 0;//设置默认选中第几个
    private int lineColor;//线的颜色
//    private int lineWidth = 0;//线的宽度
    private int lineHeight = 0;//线的高度
    private int selectColor;//选中的颜色
    private int noSelectColor;
    private int textSize = 0;
    private int slideTiem = 200;
    private int selectTextSize = 0;
    private int backgroundColor=0;
    private boolean overstriking=false;



    public YSTabLayout(Context context) {
        super(context);
        setTabViewLinear(context);
    }

    public YSTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.YSTabLayout);
        margin = (int) typedArray.getDimension(R.styleable.YSTabLayout_ys_margin, 5);
        defaultoption = typedArray.getInteger(R.styleable.YSTabLayout_ys_defaultoption, 0);
        lineColor = typedArray.getColor(R.styleable.YSTabLayout_ys_lineColor, Color.RED);
//        lineWidth = (int) typedArray.getDimension(R.styleable.YSTabLayout_ys_lineWidth, 0);
        lineHeight = (int) typedArray.getDimension(R.styleable.YSTabLayout_ys_lineHeight, 10);
        selectColor = typedArray.getColor(R.styleable.YSTabLayout_ys_selectColor, Color.RED);
        noSelectColor = typedArray.getColor(R.styleable.YSTabLayout_ys_noSelectColor, Color.BLACK);
        textSize = (int) typedArray.getDimension(R.styleable.YSTabLayout_ys_textSize, 16);
        selectTextSize = (int) typedArray.getDimension(R.styleable.YSTabLayout_ys_selectTextSize, 16);
        backgroundColor=typedArray.getColor(R.styleable.YSTabLayout_ys_backgroundColor,Color.WHITE);
        overstriking=typedArray.getBoolean(R.styleable.YSTabLayout_ys_overstriking,false);
        setTabViewLinear(context);
    }


    protected void setTabViewLinear(Context context) {
        this.setHorizontalScrollBarEnabled(false);
        tab = new TabViewLinearLayout(context);
        mContext = context;
        addView(tab);
    }

    public YSTabLayout setTabView(List<String> list) {
        LinearLayout.LayoutParams layoutParams = null;
        if (margin != 0) {
            layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(margin, 0, margin, 0);
        }
        for (int i = 0; i < list.size(); i++) {//添加Item
            TextView view = new TextView(mContext);
            view.setText(list.get(i));
            view.setTextColor(noSelectColor);
            view.setTextSize(px2dip(textSize));

            if (overstriking){
                TextPaint textPaint=view.getPaint();
                textPaint.setFakeBoldText(true);
            }

            if (margin != 0 && layoutParams != null) {
                view.setLayoutParams(layoutParams);
            }
            if (tab != null) {
                tab.addView(view);
            }
        }
        return this;
    }

    public YSTabLayout setViewPage(ViewPager viewPage) {
        this.viewPager = viewPage;
        viewPager.addOnPageChangeListener(this);
        return this;
    }

    private int lastValue = -1;
    private int nextPosition;

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int lastPosition = tab.indexOfChild(lastView);
        if (lastPosition < 0) {
            tab.updateRect(tab.getChildAt(position).getLeft(), getHeight() - lineHeight, tab.getChildAt(position).getLeft() + tab.getChildAt(position).getMeasuredWidth(), getHeight() + lineHeight);
            tab.setSelectItem(position);
            return;
        }
        float leftoffset = 0;
        float rightoffset = 0;
        nextPosition = -1;
        if (positionOffsetPixels > 0) {
            if (lastValue < positionOffsetPixels) {//左滑
                nextPosition = position + 1;
                //获取到当前View和下一个View
                View nextview = tab.getChildAt(nextPosition);
                View currentview = tab.getChildAt(position);

                if (nextview != null) {
                    //计算出从当前view到下一个view的宽度 算出百分百
                    leftoffset = (nextview.getLeft() - currentview.getLeft()) * positionOffset;
                    rightoffset = (nextview.getRight() - currentview.getRight()) * positionOffset;
                }
            } else if (lastValue > positionOffsetPixels) {//右滑
                nextPosition = position;
                //获取到当前View和下一个View
                View nextview = tab.getChildAt(nextPosition);
                View currentview = tab.getChildAt(position + 1);

                if (nextview != null) {
                    //计算出从当前view到下一个view的宽度 算出百分百
                    leftoffset = (currentview.getLeft() - nextview.getLeft()) * positionOffset;
                    rightoffset = (currentview.getRight() - nextview.getRight()) * positionOffset;
                }
            }
            View view = tab.getChildAt(nextPosition);
            if (view != null) {
                float left = tab.getChildAt(position).getLeft() + leftoffset;//计算出要位移的距离+百分比
                float right = tab.getChildAt(position).getRight() + rightoffset;
                float top = getHeight() - lineHeight;//高度
                float bottom = getHeight();
                tab.updateRect((int) left, (int) top, (int) right, (int) bottom);//得到最新的数据 通知Canvas更新界面
            }
        }
        lastValue = positionOffsetPixels;
    }

    /**
     * 检测是否被遮住显示不全
     *
     * @return trur-显示不全或不显示  false-完全显示
     */
    protected boolean isCover(View view) {
        boolean cover = false;
        Rect rect = new Rect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        cover = view.getGlobalVisibleRect(rect);
        if (cover) {
            if (rect.width() >= view.getMeasuredWidth() && rect.height() >= view.getMeasuredHeight()) {
                return !cover;
            }
        }
        return true;
    }

    /**
     * 获取当item处于一半可见一半不可见时的隐藏部分的宽度
     * @param view
     * @return
     */
    protected int getItemHideWidth(View view){
        int width=view.getMeasuredWidth();
        Rect rect = new Rect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        if (view.getLocalVisibleRect(rect)){
            //可见状态
            width-=rect.width();
        }else {
            //不可见状态
            width=view.getMeasuredWidth();
        }
        return width;
    }


    protected int getItemVisibleWidth(View view){
        int width=0;
        Rect rect = new Rect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        if (view.getLocalVisibleRect(rect)){
            //可见状态
            width= rect.width();
        }

        return width;
    }

    private int tempPosition = 0;


    /**
     * 在这里进行计算 当选中之后 如果下一个的Item没有显示或者显示不全 那么要处理 让滚动的距离刚好下一个Item给显示出来
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        tab.setSelectItem(position);

        TextView nextView = null;
        if (position > tempPosition) {
            nextView = (TextView) tab.getChildAt(position + 1);
            if (nextView != null) { //当手指像左滑的时候
                if (isCover(nextView)) {
                  float scrollPosition = getScrollX() + getItemHideWidth(nextView)+margin;//当item的数量超出屏幕的时候 使之固定
                    //这里是使用ValueAnimator来实现 平缓滚动的效果
                  ValueAnimator valueAnimator = ValueAnimator.ofFloat(getScrollX()-margin, scrollPosition);
                  valueAnimator.setDuration(slideTiem);
                  valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                      @Override
                      public void onAnimationUpdate(ValueAnimator animation) {
                          float anim = (float) animation.getAnimatedValue();
                          scrollTo((int) anim+margin, 0);
                      }
                  });
                  valueAnimator.start();
                }

                Log.e("-----",""+getItemHideWidth(nextView)+"---"+nextView.getMeasuredWidth());

            }else {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(getScrollX()-margin, tab.getMeasuredWidth());
                valueAnimator.setDuration(slideTiem);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float anim = (float) animation.getAnimatedValue();
                        scrollTo((int) anim+margin, 0);
                    }
                });
                valueAnimator.start();
            }
        } else if (position < tempPosition) {//当手指向右滑的时候
            nextView = (TextView) tab.getChildAt(position - 1);
            TextView view = (TextView) tab.getChildAt(position);
            if (nextView != null) { //当手指像左滑的时候
                if (isCover(nextView)) {
                    float scrollPosition = getScrollX() - getItemHideWidth(nextView) - margin;//当item的数量超出屏幕的时候 使之固定
                    //这里是使用ValueAnimator来实现 平缓滚动的效果
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(getScrollX() + margin, scrollPosition);
                    valueAnimator.setDuration(slideTiem);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float f = (float) animation.getAnimatedValue();
                            scrollTo((int) f - margin, 0);
                        }
                    });
                    valueAnimator.start();
                }
            }else {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(getScrollX() + margin, 0);
                valueAnimator.setDuration(slideTiem);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float f = (float) animation.getAnimatedValue();
                        scrollTo((int) f - margin, 0);
                    }
                });
                valueAnimator.start();
            }

            }

        tempPosition = position;

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * 标签Item
     */
    class TabViewLinearLayout extends LinearLayout implements OnClickListener {
        private Rect rect;
        private Paint paint;
        private List<View> views = new ArrayList<>();

        public TabViewLinearLayout(Context context) {
            super(context);
            setOrientation(LinearLayout.HORIZONTAL);
            setGravity(Gravity.CENTER);
            setBackgroundColor(backgroundColor);
            setWillNotDraw(false);
            initPaint();
            initRect();
        }

        /**
         * 测量控件宽高
         *
         * @param widthMeasureSpec
         * @param heightMeasureSpec
         */
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            int width = 0;
            int height = 0;

            if (heightMode == MeasureSpec.EXACTLY) {
                height = heightSize;
            } else {
                height = 50;//默认高度
            }

            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                getChildAt(i).setOnClickListener(this);
                width += getChildAt(i).getMeasuredWidth() + margin * 2;
                views.add(getChildAt(i));
            }
            if (width < getScene(SCENE_WIDTH)) {
                width = LayoutParams.MATCH_PARENT;
            }
            setMeasuredDimension(width, height);
            views.clear();
        }


        /**
         * 初始化画笔
         */
        private void initPaint() {
            paint = new Paint();
            paint.setColor(lineColor);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
        }

        /**
         * 初始化Rect
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void initRect() {
            rect = new Rect(0, 0, 0, 0);
        }

        /**
         * 实时更新线的宽度以及位置
         *
         * @param left
         * @param top
         * @param right
         * @param bottom
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        private void updateRect(int left, int top, int right, int bottom) {
            rect.left = left;
            rect.top = top;
            rect.right = right;
            rect.bottom = bottom;
            postInvalidateOnAnimation();
        }

        /**
         * 画底下的线
         *
         * @param canvas
         */
        @Override
        public void onDrawForeground(Canvas canvas) {
            canvas.drawRect(rect, paint);
        }

        /**
         * 设置选中的样式
         *
         * @param position
         */
        private void setSelectItem(int position) {
            View view = getChildAt(position);
            view.performClick();
        }

        /**
         * 设置选中时和不选中时的样式
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (lastView == v ) {
                return;
            }

            int position = indexOfChild(v);
            if (viewPager != null) {
                viewPager.setCurrentItem(position);
            }

            for (int i = 0; i < getChildCount(); i++) {
                TextView view = (TextView) getChildAt(i);
                if (position == i) {
                    view.setTextColor(selectColor);
                    view.setTextSize(px2dip(selectTextSize));
                } else {
                    view.setTextColor(noSelectColor);
                    view.setTextSize(px2dip(textSize));
                }
            }
            lastView = v;
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param i
     * @return
     */
    public int getScene(int i) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        if (i == SCENE_WIDTH) {
            return width;
        } else if (i == SCENE_HEIGHT) {
            return height;
        }
        return 0;
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}

package myandroid.pigrobot;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;


public class ChangeColorIconWithText extends View {

    //四个自定义属性
    private int mColor = 0xFF45C01A;
    private Bitmap mIconBitmap;
    private String mText = "聊天";
    private int mTextSize = (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

    private Bitmap mBitmap;
    private Rect mIconRect = new Rect();
    private Rect mTextBound;
    private Paint mTextPaint = new Paint();

    public ChangeColorIconWithText(Context context) {
        super(context);
        mTextBound = new Rect();
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextBound = new Rect();
    }

    public ChangeColorIconWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取自定义属性并绑定
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ChangeColorIconWithText);

        int n = a.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorIconWithText_icon:
                    BitmapDrawable drawable = (BitmapDrawable) a.getDrawable(attr);
                    if (drawable != null) {
                       mIconBitmap = BitmapFactory.decodeResource(getResources(),attr);
                        mIconBitmap = drawable.getBitmap();
                    }
                    break;
                case R.styleable.ChangeColorIconWithText_color:
                    mColor = a.getColor(attr, 0xFF45C01A);
                    break;
                case R.styleable.ChangeColorIconWithText_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.ChangeColorIconWithText_text_size:
                    mTextSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        //设置字体的画 mPaint.setAntiAlias(true);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0Xff555555);
        mTextBound = new Rect();
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //icon的范围的设置
        int iconWidth = Math.min((getMeasuredWidth() - getPaddingLeft() - getPaddingRight()),
                getMeasuredHeight()-getPaddingBottom()-getPaddingTop());
        int left = getMeasuredWidth()/2 - iconWidth/2;
        int top = getMeasuredHeight()/2-(mTextBound.height()+iconWidth)/2 ;
        mIconRect = new Rect(left,top,left+iconWidth,top+iconWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //第一次绘制图标
        canvas.drawBitmap(mIconBitmap,null,mIconRect,null);
        float mAlpha = 1.0f;
        int alpha = (int) Math.ceil( 255* mAlpha);
       setupTargetBitmap(alpha);

        drawSourceText(canvas, alpha);
        drawTargetText(canvas, alpha);

        canvas.drawBitmap(mBitmap,0,0,null);
    }

    private void drawTargetText(Canvas canvas, int alpha)
    {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawSourceText(Canvas canvas, int alpha)
    {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        int x = getMeasuredWidth() / 2 - mTextBound.width() / 2;
        int y = mIconRect.bottom + mTextBound.height();
        canvas.drawText(mText, x, y, mTextPaint);

    }

    //在内存中绘制可以变色的Icon,传入透明度参数，得到不同效果的可变色图表
    private void setupTargetBitmap(int alpha) {
        //绘制纯色
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas mCanvas = new Canvas(mBitmap);
        Paint mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect, mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        //再次绘制图标
        mPaint.setAlpha(255);
        mCanvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
    }
}

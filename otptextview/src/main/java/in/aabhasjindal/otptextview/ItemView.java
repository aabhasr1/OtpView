package in.aabhasjindal.otptextview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ItemView extends FrameLayout {
    public static final int ACTIVE = 1;
    public static final int INACTIVE = 0;
    public static final int ERROR = -1;
    public static final int SUCCESS = 2;

    private static final float DEFAULT_BAR_HEIGHT = 2f;
    private static final float DEFAULT_OTP_TEXT_SIZE = 24f;
    private static final int DEFAULT_BAR_PADDING = 2;

    private Context context;
    private TextView textView;
    private View view;
    private int barActiveColor, barInactiveColor, barErrorColor, barSuccessColor;

    public ItemView(@NonNull Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public ItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs) {
        TypedArray styles = getContext().obtainStyledAttributes(attrs, R.styleable.OtpTextView);
        generateViews(styles);
        styles.recycle();
    }

    private void generateViews(TypedArray styles) {
        float defaultHeight = Utils.getPixels(context, DEFAULT_BAR_HEIGHT);
        float defaultOtpTextSize = Utils.getPixels(context, DEFAULT_OTP_TEXT_SIZE);
        int textColor = styles.getColor(R.styleable.OtpTextView_android_textColor
                , ResourcesCompat.getColor(context.getResources(), R.color.black, null));
        float barHeight = styles.getDimension(R.styleable.OtpTextView_bar_height, defaultHeight);
        int barPadding = styles.getInt(R.styleable.OtpTextView_bar_padding, DEFAULT_BAR_PADDING);
        Float otpTextSize = styles.getDimension(R.styleable.OtpTextView_otp_text_size, defaultOtpTextSize);

        String otpTextTypeFace = styles.getString(R.styleable.OtpTextView_text_typeface);
        barActiveColor = styles.getColor(R.styleable.OtpTextView_bar_active_color
                , ResourcesCompat.getColor(context.getResources(), R.color.black, null));
        barInactiveColor = styles.getColor(R.styleable.OtpTextView_bar_inactive_color
                , ResourcesCompat.getColor(context.getResources(), R.color.grey, null));
        barErrorColor = styles.getColor(R.styleable.OtpTextView_bar_error_color
                , ResourcesCompat.getColor(context.getResources(), R.color.red, null));
        barSuccessColor = styles.getColor(R.styleable.OtpTextView_bar_success_color
                , ResourcesCompat.getColor(context.getResources(), R.color.black, null));

        FrameLayout.LayoutParams textViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        textViewParams.gravity = Gravity.CENTER;
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        if (otpTextTypeFace!=null) {
            try {
                Typeface tf = Typeface.createFromAsset(context.getAssets(), otpTextTypeFace);
                textView.setTypeface(tf);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, otpTextSize);
        this.addView(textView, textViewParams);

        FrameLayout.LayoutParams barViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int)barHeight);
        barViewParams.gravity = Gravity.BOTTOM;
        int margin = Utils.getPixels(context, barPadding);
        barViewParams.leftMargin = margin;
        barViewParams.rightMargin = margin;
        view = new View(context);
        this.addView(view, barViewParams);
    }

    public void setText(String value) {
        if (textView != null) {
            textView.setText(value);
        }
    }

    public void setViewState(int state) {
        if (view != null) {
            switch (state) {
                case ACTIVE: {
                    view.setBackgroundColor(barActiveColor);
                    break;
                }
                case INACTIVE: {
                    view.setBackgroundColor(barInactiveColor);
                    break;
                }
                case ERROR: {
                    view.setBackgroundColor(barErrorColor);
                    break;
                }
                case SUCCESS: {
                    view.setBackgroundColor(barSuccessColor);
                    break;
                }
            }
        }
    }

}

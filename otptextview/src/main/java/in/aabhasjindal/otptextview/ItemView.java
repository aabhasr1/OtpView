package in.aabhasjindal.otptextview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
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
    private static final int DEFAULT_BAR_MARGIN = 2;

    private Context context;
    private TextView textView;
    private View view;
    private int barActiveColor, barInactiveColor, barErrorColor, barSuccessColor,
            boxBackgroundColorActive, boxBackgroundColorInactive, boxBackgroundColorSuccess,
            boxBackgroundColorError, hideOTPDrawable, defaultOTPDrawable;
    private boolean hideOTP = false;

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
        float barMargin = styles.getDimension(R.styleable.OtpTextView_bar_margin, Utils.getPixels(context, 0));
        float barMarginBottom = styles.getDimension(R.styleable.OtpTextView_bar_margin_bottom, DEFAULT_BAR_MARGIN);
        float barMarginRight = styles.getDimension(R.styleable.OtpTextView_bar_margin_right, DEFAULT_BAR_MARGIN);
        float barMarginLeft = styles.getDimension(R.styleable.OtpTextView_bar_margin_left, DEFAULT_BAR_MARGIN);
        float barMarginTop = styles.getDimension(R.styleable.OtpTextView_bar_margin_top, DEFAULT_BAR_MARGIN);
        hideOTP = styles.getBoolean(R.styleable.OtpTextView_hide_otp, false);
        hideOTPDrawable = styles.getResourceId(R.styleable.OtpTextView_hide_otp_drawable
                , R.drawable.bg_pin);

        defaultOTPDrawable = ResourcesCompat.getColor(context.getResources(), R.color.transparent, null);

        boolean barEnabled = styles.getBoolean(R.styleable.OtpTextView_bar_enabled, false);

        Float otpTextSize = styles.getDimension(R.styleable.OtpTextView_otp_text_size, defaultOtpTextSize);

        String otpTextTypeFace = styles.getString(R.styleable.OtpTextView_text_typeface);
        int boxBackgroundColor = styles.getResourceId(R.styleable.OtpTextView_otp_box_background
                , ResourcesCompat.getColor(context.getResources(), R.color.transparent, null));
        boxBackgroundColorActive = styles.getResourceId(R.styleable.OtpTextView_otp_box_background_active
                , boxBackgroundColor);
        boxBackgroundColorInactive = styles.getResourceId(R.styleable.OtpTextView_otp_box_background_inactive
                , boxBackgroundColor);
        boxBackgroundColorSuccess = styles.getResourceId(R.styleable.OtpTextView_otp_box_background_success
                , boxBackgroundColor);
        boxBackgroundColorError = styles.getResourceId(R.styleable.OtpTextView_otp_box_background_error
                , boxBackgroundColor);
        barActiveColor = styles.getColor(R.styleable.OtpTextView_bar_active_color
                , ResourcesCompat.getColor(context.getResources(), R.color.black, null));
        barInactiveColor = styles.getColor(R.styleable.OtpTextView_bar_inactive_color
                , ResourcesCompat.getColor(context.getResources(), R.color.grey, null));
        barErrorColor = styles.getColor(R.styleable.OtpTextView_bar_error_color
                , ResourcesCompat.getColor(context.getResources(), R.color.red, null));
        barSuccessColor = styles.getColor(R.styleable.OtpTextView_bar_success_color
                , ResourcesCompat.getColor(context.getResources(), R.color.black, null));

        this.setBackgroundResource(boxBackgroundColor);

        FrameLayout.LayoutParams textViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        textViewParams.gravity = Gravity.CENTER;
        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        if (otpTextTypeFace != null) {
            try {
                Typeface tf = Typeface.createFromAsset(context.getAssets(), otpTextTypeFace);
                textView.setTypeface(tf);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        textView.setTextColor(textColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, otpTextSize);
        this.addView(textView, textViewParams);

        if (barEnabled) {
            FrameLayout.LayoutParams barViewParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) barHeight);
            barViewParams.gravity = Gravity.BOTTOM;
            if (barMargin != 0f) {
                barMarginLeft = barMargin;
                barMarginRight = barMargin;
                barMarginBottom = barMargin;
                barMarginTop = barMargin;
            }
            barViewParams.leftMargin = (int) barMarginLeft;
            barViewParams.rightMargin = (int) barMarginRight;
            barViewParams.bottomMargin = (int) barMarginBottom;
            barViewParams.topMargin = (int) barMarginTop;
            view = new View(context);
            this.addView(view, barViewParams);
        }
    }

    public void setText(String value) {
        if (!hideOTP) {
            if (textView != null) {
                textView.setText(value);
            }
        } else {
            textView.setText("");
            if (value.equals("")) {
                textView.setBackgroundResource(defaultOTPDrawable);
            } else {
                textView.setBackgroundResource(hideOTPDrawable);
            }
        }
    }

    public void setViewState(int state) {
        switch (state) {
            case ACTIVE: {
                if (view != null) {
                    view.setBackgroundColor(barActiveColor);
                }
                this.setBackgroundResource(boxBackgroundColorActive);
                break;
            }
            case INACTIVE: {
                if (view != null) {
                    view.setBackgroundColor(barInactiveColor);
                }
                this.setBackgroundResource(boxBackgroundColorInactive);
                break;
            }
            case ERROR: {
                if (view != null) {
                    view.setBackgroundColor(barErrorColor);
                }
                this.setBackgroundResource(boxBackgroundColorError);
                break;
            }
            case SUCCESS: {
                if (view != null) {
                    view.setBackgroundColor(barSuccessColor);
                }
                this.setBackgroundResource(boxBackgroundColorSuccess);
                break;
            }
            default:
                break;
        }
    }

}

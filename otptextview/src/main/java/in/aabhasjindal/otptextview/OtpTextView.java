package in.aabhasjindal.otptextview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OtpTextView extends FrameLayout {

    private static final int DEFAULT_LENGTH = 4;
    private static final int DEFAULT_HEIGHT = 48;
    private static final int DEFAULT_WIDTH = 48;
    private static final int DEFAULT_SPACE = -1;
    private static final int DEFAULT_SPACE_LEFT = 4;
    private static final int DEFAULT_SPACE_RIGHT = 4;
    private static final int DEFAULT_SPACE_TOP = 4;
    private static final int DEFAULT_SPACE_BOTTOM = 4;

    private static final String PATTERN = "[1234567890]*";

    private Context context;
    private List<ItemView> itemViews;
    private OTPChildEditText otpChildEditText;
    private OTPListener otpListener;

    private int length;

    public OtpTextView(@NonNull Context context) {
        super(context);
        this.context = context;
        init(null);
    }

    public OtpTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public OtpTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray styles = getContext().obtainStyledAttributes(attrs, R.styleable.OtpTextView);
        styleEditTexts(styles, attrs);
        styles.recycle();
    }

    private void styleEditTexts(TypedArray styles, AttributeSet attrs) {
        length = styles.getInt(R.styleable.OtpTextView_length, DEFAULT_LENGTH);
        generateViews(styles, attrs);
    }

    private void generateViews(TypedArray styles, AttributeSet attrs) {
        itemViews = new ArrayList<>();
        if (length > 0) {
            String otp = styles.getString(R.styleable.OtpTextView_otp);
            int width = (int) styles.getDimension(R.styleable.OtpTextView_width, Utils.getPixels(context, DEFAULT_WIDTH));
            int height = (int) styles.getDimension(R.styleable.OtpTextView_height, Utils.getPixels(context, DEFAULT_HEIGHT));
            int space = (int) styles.getDimension(R.styleable.OtpTextView_box_margin, Utils.getPixels(context, DEFAULT_SPACE));
            int spaceLeft = (int) styles.getDimension(R.styleable.OtpTextView_box_margin_left, Utils.getPixels(context, DEFAULT_SPACE_LEFT));
            int spaceRight = (int) styles.getDimension(R.styleable.OtpTextView_box_margin_right, Utils.getPixels(context, DEFAULT_SPACE_RIGHT));
            int spaceTop = (int) styles.getDimension(R.styleable.OtpTextView_box_margin_top, Utils.getPixels(context, DEFAULT_SPACE_TOP));
            int spaceBottom = (int) styles.getDimension(R.styleable.OtpTextView_box_margin_bottom, Utils.getPixels(context, DEFAULT_SPACE_BOTTOM));
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(width, height);
            if (space > 0) {
                params.setMargins(space, space, space, space);
            } else {
                params.setMargins(spaceLeft, spaceTop, spaceRight, spaceBottom);
            }

            LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            editTextLayoutParams.gravity = Gravity.CENTER;
            otpChildEditText = new OTPChildEditText(context);
            otpChildEditText.setFilters(new InputFilter[]{getFilter()
                    , new InputFilter.LengthFilter(length)});
            setTextWatcher(otpChildEditText);
            addView(otpChildEditText, editTextLayoutParams);


            LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout linearLayout = new LinearLayout(context);

            addView(linearLayout, linearLayoutParams);

            for (int i = 0; i < length; i++) {
                ItemView itemView = new ItemView(context, attrs);
                itemView.setViewState(ItemView.INACTIVE);
                linearLayout.addView(itemView, i, params);
                itemViews.add(itemView);
            }
            if (otp != null) {
                setOTP(otp);
            } else {
                setOTP("");
            }
        } else {
            throw new IllegalStateException("Please specify the length of the otp view");
        }
    }

    private void setTextWatcher(OTPChildEditText otpChildEditText) {
        otpChildEditText.addTextChangedListener(new TextWatcher() {
            /**
             * @param s
             * @param start
             * @param count
             * @param after
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             * @param s
             * @param start
             * @param before
             * @param count
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otpListener != null) {
                    otpListener.onInteractionListener();
                    if (s.length() == length) {
                        otpListener.onOTPComplete(s.toString());
                    }
                }
                setOTP(s);
                setFocus(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setFocus(int length) {
        for (int i = 0; i < itemViews.size(); i++) {
            if (i == length) {
                itemViews.get(i).setViewState(ItemView.ACTIVE);
            } else {
                itemViews.get(i).setViewState(ItemView.INACTIVE);
            }
        }
        if (length == itemViews.size()) {
            itemViews.get(itemViews.size() - 1).setViewState(ItemView.ACTIVE);
        }
    }

    public void setOTP(CharSequence s) {
        for (int i = 0; i < itemViews.size(); i++) {
            if (i < s.length()) {
                itemViews.get(i).setText(String.valueOf(s.charAt(i)));
            } else {
                itemViews.get(i).setText("");
            }
        }
    }

    public OTPListener getOtpListener() {
        return otpListener;
    }

    public void setOtpListener(OTPListener otpListener) {
        this.otpListener = otpListener;
    }

    private InputFilter getFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                       int dstart, int dend) {
                for (int i = start; i < end; ++i) {
                    if (!Pattern.compile(
                            PATTERN)
                            .matcher(String.valueOf(source.charAt(i)))
                            .matches()) {
                        return "";
                    }
                }
                return null;
            }
        };
    }

    public void requestFocusOTP() {
        if (otpChildEditText != null) {
            otpChildEditText.requestFocus();
        }
    }

    public void showError() {
        if (itemViews != null) {
            for (ItemView itemView : itemViews) {
                itemView.setViewState(ItemView.ERROR);
            }
        }
    }

    public void resetState() {
        setFocus(getOTP().length());
    }

    public void showSuccess() {
        if (itemViews != null) {
            for (ItemView itemView : itemViews) {
                itemView.setViewState(ItemView.SUCCESS);
            }
        }
    }

    public void setOTP(String otp) {
        otpChildEditText.setText(otp);
    }

    public String getOTP() {
        if (otpChildEditText != null && otpChildEditText.getText() != null)
            return otpChildEditText.getText().toString();
        return null;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        otpChildEditText.setOnTouchListener(l);
    }
}

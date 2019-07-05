package in.aabhasjindal.otpview;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class MainActivity extends AppCompatActivity {
    private OtpTextView otpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        asdasdawasdasdasdasdaasd
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.black));
        }
        Button errorButton = findViewById(R.id.button);
        Button successButton = findViewById(R.id.button2);
        otpTextView = findViewById(R.id.otp_view);
        otpTextView.requestFocusOTP();
        otpTextView.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                Toasty.success(MainActivity.this, "The OTP is " + otp, Toast.LENGTH_SHORT).show();
            }
        });
        errorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpTextView.showError();
            }
        });
        successButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpTextView.showSuccess();
            }
        });
    }
}

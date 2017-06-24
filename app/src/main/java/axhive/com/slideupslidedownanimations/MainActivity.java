package axhive.com.slideupslidedownanimations;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    public MainActivity mainActivityPointer() {
        return this;
    }

    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.mainRelLayout);

        final float dp = this.getResources().getDisplayMetrics().density;

        final int ButtonHeight = 50 * (int)dp;
        final int SpaceBetween = 10 * (int)dp;
        final int LeftButtonWidth = 100 * (int)dp;
        final int RightButtonWidth = 200 * (int)dp;

        final Button[] leftButtons = new Button[4];
        final Button[] rightButtons = new Button[4];

        layout.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 4; i++) {
                    Button btn = new Button(mainActivityPointer());
                    btn.setText(String.valueOf(i));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

                    int top = ((layout.getHeight() - (ButtonHeight * 4 + SpaceBetween * 3)) / 2) + (i * (ButtonHeight + SpaceBetween));
                    params.topMargin = top;
                    params.height = ButtonHeight;
                    params.leftMargin = 10 * (int)dp;
                    params.width = LeftButtonWidth;
                    btn.setLayoutParams(params);
                    layout.addView(btn);
                    leftButtons[i] = btn;

                    Button rightBtn = new Button(mainActivityPointer());
                    rightBtn.setText(String.valueOf(i*2));
                    RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    paramsRight.topMargin = top;
                    paramsRight.height = ButtonHeight;
                    paramsRight.leftMargin = params.leftMargin + LeftButtonWidth + 30 * (int)dp;
                    paramsRight.width = RightButtonWidth;
                    rightBtn.setLayoutParams(paramsRight);
                    layout.addView(rightBtn);

                }

            }
        });




    }

}

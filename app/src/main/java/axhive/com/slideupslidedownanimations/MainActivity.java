package axhive.com.slideupslidedownanimations;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public MainActivity mainActivityPointer() {
        return this;
    }

    private int currentMoveToIndex = 0;
    private int selectedLeftButton = -1;
    private int selectedRightButton = -1;

    final int ButtonsCount = 4;
    final Button[] leftButtons = new Button[ButtonsCount];
    final Button[] rightButtons = new Button[ButtonsCount];

    float dp;

    int ButtonHeight;
    int SpaceBetween;
    int LeftButtonWidth;
    int RightButtonWidth;

    private int buttonTopMarginByItsPlace(int placeNumber) {
        return ((layout.getHeight() - (ButtonHeight * ButtonsCount + SpaceBetween * (ButtonsCount-1))) / 2) + (placeNumber * (ButtonHeight + SpaceBetween));
    }

    private RelativeLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        layout = (RelativeLayout) findViewById(R.id.mainRelLayout);

        dp = this.getResources().getDisplayMetrics().density;
        ButtonHeight = 50 * (int)dp;
        SpaceBetween = 10 * (int)dp;
        LeftButtonWidth = 100 * (int)dp;
        RightButtonWidth = 200 * (int)dp;

        layout.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < ButtonsCount; i++) {
                    Button btn = new Button(mainActivityPointer());
                    btn.setText(String.valueOf(i));
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);

                    int top = buttonTopMarginByItsPlace(i);

                    params.topMargin = top;
                    params.height = ButtonHeight;
                    params.leftMargin = 10 * (int)dp;
                    params.width = LeftButtonWidth;
                    btn.setLayoutParams(params);
                    layout.addView(btn);
                    leftButtons[i] = btn;
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickedLeftButton((Button)view);
                        }
                    });
                    btn.setBackgroundResource(R.drawable.button);

                    Button rightBtn = new Button(mainActivityPointer());
                    rightBtn.setText(String.valueOf(i*2));
                    RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    paramsRight.topMargin = top;
                    paramsRight.height = ButtonHeight;
                    paramsRight.leftMargin = params.leftMargin + LeftButtonWidth + 30 * (int)dp;
                    paramsRight.width = RightButtonWidth;
                    rightBtn.setLayoutParams(paramsRight);
                    layout.addView(rightBtn);
                    rightButtons[i] = rightBtn;
                    rightBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickedRightButton((Button)view);
                        }
                    });
                    rightBtn.setBackgroundResource(R.drawable.button);
                }

            }
        });
    }

    private void clickedLeftButton(Button button) {
        selectedLeftButton = -1;
        button.setSelected(!button.isSelected());
        int i = 0;
        for (Button btn: leftButtons) {
            if (btn != button) {
                btn.setSelected(false);
            }
            else {
                if (button.isSelected()) {
                    if (i < currentMoveToIndex) return;
                    selectedLeftButton = i;
                }
            }
            i++;
        }
        checkConnection();
    }

    private void clickedRightButton(Button button) {
        selectedRightButton = -1;
        button.setSelected(!button.isSelected());
        int i = 0;
        for (Button btn: rightButtons) {
            if (btn != button) {
                btn.setSelected(false);
            }
            else {
                if (button.isSelected()) {
                    if (i < currentMoveToIndex) return;
                    selectedRightButton = i;
                }
            }
            i++;
        }
        checkConnection();
    }

    private ObjectAnimator makeAnimatorToSwapButtons(Button[] arraySrc, int indexToAnimate, int targetIndex) {
        final Button buttonToMove = arraySrc[indexToAnimate];
        final RelativeLayout.LayoutParams btnToMoveLP = (RelativeLayout.LayoutParams)buttonToMove.getLayoutParams();
        final int srcValue = buttonTopMarginByItsPlace(indexToAnimate);
        final int dstValue = buttonTopMarginByItsPlace(targetIndex);
        ObjectAnimator animator = ObjectAnimator.ofInt(buttonToMove, "topMargin", srcValue, dstValue);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                btnToMoveLP.topMargin = (Integer) valueAnimator.getAnimatedValue();
                buttonToMove.setLayoutParams(btnToMoveLP);
            }
        });
        return animator;
    }

    private ObjectAnimator makeAnimatorToTurnButtonToItsPlace(final Button b, int place) {
        final RelativeLayout.LayoutParams flp = (RelativeLayout.LayoutParams) b.getLayoutParams();
        final int srcValue = flp.topMargin;
        final int dstValue = buttonTopMarginByItsPlace(place);
        ObjectAnimator animator = ObjectAnimator.ofInt(b, "topMargin", srcValue, dstValue);
        animator.setDuration(1000);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                flp.topMargin = (Integer) valueAnimator.getAnimatedValue();
                b.setLayoutParams(flp);
            }
        });
        return animator;
    }

    private void checkConnection() {
        if (selectedLeftButton != -1 && selectedRightButton != -1) {
            leftButtons[selectedLeftButton].setBackgroundResource(R.drawable.button_locked);
            rightButtons[selectedRightButton].setBackgroundResource(R.drawable.button_locked);


            Collection<Animator> animators = new ArrayList<>();
            AnimatorSet animSet = new AnimatorSet();

            //Animate left button to new position if needed
            if (selectedLeftButton != currentMoveToIndex) {
                //animators.add(makeAnimatorToSwapButtons(leftButtons, selectedLeftButton, currentMoveToIndex));

                Button currentButtonAtLockedPlace = leftButtons[currentMoveToIndex];
                leftButtons[currentMoveToIndex] = leftButtons[selectedLeftButton];
                for (int i = selectedLeftButton; i>currentMoveToIndex; i--) {
                    leftButtons[i] = leftButtons[i-1];
                }
                leftButtons[currentMoveToIndex+1]=currentButtonAtLockedPlace;

                for (int i = 0; i < ButtonsCount; i++) {
                    animators.add(makeAnimatorToTurnButtonToItsPlace(leftButtons[i], i));
                }
            }
            else {
                //only alpha animation / animation switch would be here.
            }

            //Animate right button to new position if needed
            if (selectedRightButton != currentMoveToIndex) {
                //animators.add(makeAnimatorToSwapButtons(rightButtons, selectedRightButton, currentMoveToIndex));
                Button currentButtonAtLockedPlace = rightButtons[currentMoveToIndex];
                rightButtons[currentMoveToIndex] = rightButtons[selectedRightButton];
                for (int i = selectedRightButton; i>currentMoveToIndex; i--) {
                    rightButtons[i] = rightButtons[i-1];
                }
                rightButtons[currentMoveToIndex+1]=currentButtonAtLockedPlace;

                for (int i = 0; i < ButtonsCount; i++) {
                    Animator anim = makeAnimatorToTurnButtonToItsPlace(rightButtons[i], i);
                    anim.setStartDelay(300);
                    animators.add(anim);
                }
            }
            else {
                //only alpha animation / animation switch would be here.
            }

            currentMoveToIndex++; //increase locked/linked buttons count.

            animSet.playTogether(animators);
            animSet.start();
            selectedLeftButton = selectedRightButton = -1;
        }
    }

}

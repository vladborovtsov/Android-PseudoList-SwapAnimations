package axhive.com.slideupslidedownanimations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    Animation slideUpAnimation, slideDownAnimation;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_animation);

        slideDownAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down_animation);
        slideUpAnimation.setFillEnabled(true);
        slideDownAnimation.setFillEnabled(true);

    }

    public void startSlideUpAnimation(View view) {
        imageView.clearAnimation();
        imageView.startAnimation(slideUpAnimation);
    }

    public void startSlideDownAnimation(View view) {
        imageView.clearAnimation();
        imageView.startAnimation(slideDownAnimation);
    }
}

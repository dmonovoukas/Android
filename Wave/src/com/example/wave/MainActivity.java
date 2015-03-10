package com.example.wave;

import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements AnimationListener {
	
	View menu;
    View app;
    boolean menuOut = false;
    AnimParams animParams = new AnimParams();
    
	private Button startButton;
	private TextView waveText, profileText, swimText, storeText, settingsText;
	
	class ClickListener implements OnClickListener {
        @Override
        public void onClick(View v) {
            System.out.println("onClick " + new Date());
            MainActivity me = MainActivity.this;
            Animation anim;

            int w = app.getMeasuredWidth();
            int h = app.getMeasuredHeight();
            int left = (int) (app.getMeasuredWidth() * 0.8);

            if (!menuOut) {
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_right_out_80);
                anim = new TranslateAnimation(0, left, 0, 0);
                menu.setVisibility(View.VISIBLE);
                animParams.init(left, 0, left + w, h);
            } else {
                // anim = AnimationUtils.loadAnimation(context, R.anim.push_left_in_80);
                anim = new TranslateAnimation(0, -left, 0, 0);
                animParams.init(0, 0, w, h);
            }

            anim.setDuration(500);
            anim.setAnimationListener(me);
            //Tell the animation to stay as it ended (we are going to set the app.layout first than remove this property)
            anim.setFillAfter(true);


            // Only use fillEnabled and fillAfter if we don't call layout ourselves.
            // We need to do the layout ourselves and not use fillEnabled and fillAfter because when the anim is finished
            // although the View appears to have moved, it is actually just a drawing effect and the View hasn't moved.
            // Therefore clicking on the screen where the button appears does not work, but clicking where the View *was* does
            // work.
            // anim.setFillEnabled(true);
            // anim.setFillAfter(true);

            app.startAnimation(anim);
        }
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		/*Typeface type=Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/open-sans/OpenSans-Light.ttf");
		startButton.setTypeface(type);
		waveText.setTypeface(type);
		profileText.setTypeface(type);
		swimText.setTypeface(type);
		storeText.setTypeface(type);
		settingsText.setTypeface(type);*/
		
		
        menu = findViewById(R.id.menu);
        app = findViewById(R.id.app);

        ViewUtils.printView("menu", menu);
        ViewUtils.printView("app", app);

        app.findViewById(R.id.BtnSlide).setOnClickListener(new ClickListener());
	}
	
    void layoutApp(boolean menuOut) {
        System.out.println("layout [" + animParams.left + "," + animParams.top + "," + animParams.right + ","
                + animParams.bottom + "]");
        app.layout(animParams.left, animParams.top, animParams.right, animParams.bottom);
        //Now that we've set the app.layout property we can clear the animation, flicker avoided :)
        app.clearAnimation();

    }

    public void onAnimationEnd(Animation animation) {
        System.out.println("onAnimationEnd");
        ViewUtils.printView("menu", menu);
        ViewUtils.printView("app", app);
        menuOut = !menuOut;
        if (!menuOut) {
            menu.setVisibility(View.INVISIBLE);
        }
        layoutApp(menuOut);
    }

    public void onAnimationRepeat(Animation animation) {
        System.out.println("onAnimationRepeat");
    }

    public void onAnimationStart(Animation animation) {
        System.out.println("onAnimationStart");
    }

    static class AnimParams {
        int left, right, top, bottom;

        void init(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }
}
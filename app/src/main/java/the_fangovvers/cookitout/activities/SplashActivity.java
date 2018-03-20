package the_fangovvers.cookitout.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;

/**
 * Created by Karolina on 13/11/2017.
 */

public class SplashActivity extends AppCompatActivity {

    Button test;
    ImageView imgtest;
    Animation frombottom, fromtop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //FirebaseCONN.getInstance().getmAuth().signOut();
        test = (Button) findViewById(R.id.getStarted_button);
        imgtest = (ImageView) findViewById(R.id.firstScreen);

        frombottom = AnimationUtils.loadAnimation(this, R.anim.frombottom);
        fromtop = AnimationUtils.loadAnimation(this, R.anim.fromtop);

        /*test.setAnimation(frombottom);*/
        imgtest.setAnimation(frombottom);


        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseCONN.getInstance().getCurrentUser() == null) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), RecipeListActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}

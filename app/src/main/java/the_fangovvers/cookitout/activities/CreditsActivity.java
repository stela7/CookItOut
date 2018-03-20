package the_fangovvers.cookitout.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import the_fangovvers.cookitout.R;

/**
 * Created by Karolina on 14/11/2017.
 */

public class CreditsActivity extends BaseActivity{

    Button flaticonBut;
    Button iconFinderBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        super.inflateToolbar(R.id.toolbar);

        flaticonBut = (Button) findViewById(R.id.flaticon_button);

        flaticonBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.flaticon.com/"));
                startActivity(intent);
            }
        });

        iconFinderBut = (Button) findViewById(R.id.iconfinder_button);

        iconFinderBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://www.iconfinder.com/"));
                startActivity(intent);
            }
        });
    }

}

package the_fangovvers.cookitout.activities;

import android.os.Bundle;

import the_fangovvers.cookitout.R;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        super.inflateToolbar(R.id.toolbar);
    }
}

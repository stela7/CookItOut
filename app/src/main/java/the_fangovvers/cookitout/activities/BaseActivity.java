package the_fangovvers.cookitout.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void inflateToolbar(int id){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if(FirebaseCONN.getInstance().getCurrentUser() != null)
        // TODO: 11/14/2017 make it check if user is logged in and inflate according to it
        getMenuInflater().inflate(R.menu.logged_in_menu_main, menu);
        else {
            getMenuInflater().inflate(R.menu.logged_out_menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle action bar item clicks here
        int id = item.getItemId();
        if (id == R.id.home_item) {
            Intent intent = new Intent(getApplicationContext(), RecipeListActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.categories_item) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.basket_item) {
            Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.logOut_item) {
            if (FirebaseCONN.getInstance().getCurrentUser() != null) {
                FirebaseCONN.getInstance().logOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.logIn_item) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.register_item) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.credits_item) {
            Intent intent = new Intent(getApplicationContext(), CreditsActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.about_item){
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showAlert(Context context, String title, String text){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

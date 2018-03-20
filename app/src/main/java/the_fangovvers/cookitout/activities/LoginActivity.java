package the_fangovvers.cookitout.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import the_fangovvers.cookitout.Helper;
import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;
import the_fangovvers.cookitout.adapters.IconAdapter;

public class LoginActivity extends BaseActivity {

   // private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        super.inflateToolbar(R.id.toolbar);
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        EditText emailtext = findViewById(R.id.addEmail);
        String email = emailtext.getText().toString();
        EditText passwordtext = findViewById(R.id.addPassword);
        String password = passwordtext.getText().toString();

        if(!Helper.validateEmail(email)){

            showAlert(LoginActivity.this,"Error","Wrong Email format");

            return;
        }
        if (!Helper.validateLength(password,8) ){
            showAlert(LoginActivity.this,"Error","Wrong Password");
            return;
        }
        if (email.equals("") || password.equals("")){
            showAlert(LoginActivity.this,"Error","Empty field");
            return;
        }

        FirebaseCONN.getInstance().getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = FirebaseCONN.getInstance().getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), RecipeListActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

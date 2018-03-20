package the_fangovvers.cookitout.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import the_fangovvers.cookitout.Helper;
import the_fangovvers.cookitout.R;
import the_fangovvers.cookitout.Services.FirebaseCONN;
import the_fangovvers.cookitout.model.Ingredient;
import the_fangovvers.cookitout.model.Recipe;
import the_fangovvers.cookitout.model.Step;

public class RegisterActivity extends BaseActivity {

    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        super.inflateToolbar(R.id.toolbar);
    }

    public void signup(View view) {
        EditText emailtext = findViewById(R.id.addEmail);
        String email = emailtext.getText().toString();
        EditText passwordtext = findViewById(R.id.addPassword);
        String password = passwordtext.getText().toString();
        EditText confirmtext = findViewById(R.id.confirmPassword);
        String confirm = confirmtext.getText().toString();

        if(!Helper.validateEmail(email)){
            showAlert(RegisterActivity.this,"Error","Wrong Email format");
            return;
        }
        if (email.equals("") || password.equals("") || confirm.equals("")){
            showAlert(RegisterActivity.this,"Error","Empty field");
            return;
        }
        if (!password.equals(confirm)){
            showAlert(RegisterActivity.this,"Error","The passwords are not matching");
            return;
        }

        if (!Helper.validateLength(password,8) ){
            showAlert(RegisterActivity.this,"Error","The password must be at least 8 characters long");
            return;
        }
        FirebaseCONN.getInstance().getmAuth().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Register", "createUserWithEmail:success");
                            FirebaseUser user = FirebaseCONN.getInstance().getCurrentUser();
                            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //addEmptyList();
                    }
                });
    }

    private void addEmptyList() {
        FirebaseUser currentUser = FirebaseCONN.getInstance().getCurrentUser();
        ArrayList<Recipe> recipes = new ArrayList<>();

        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient("Cream", "ml", 250));
        ingredients.add(new Ingredient("Sugar", "g", 300));
        ingredients.add(new Ingredient("Egg", "psc.", 3));
        ArrayList<Step> steps = new ArrayList<>();
        steps.add(new Step("Add some water and mix"));
        recipes.add(new Recipe("First", ingredients, steps));
        DatabaseReference myRef = FirebaseCONN.getInstance().getmDB().getReference("USERS_TABLE");
        myRef.child(FirebaseCONN.getInstance().getmAuth().getCurrentUser().getUid()).child("mainRecipes").setValue(recipes);
    }
}
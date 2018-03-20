package the_fangovvers.cookitout.Services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by marek on 11/14/2017.
 */

public class FirebaseCONN {

    private static FirebaseCONN instance = null;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDB;

    private FirebaseCONN(){
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
    }

    public static FirebaseCONN getInstance(){
        if (instance == null){
            instance = new FirebaseCONN();
        }
        return instance;
    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public FirebaseDatabase getmDB() {
        return mDB;
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void logOut() {
        mAuth.signOut();
    }
}

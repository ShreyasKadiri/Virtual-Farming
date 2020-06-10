package application.example.myfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN=123;
    public Button signInButton;
    public Button signOutButton;
    FirebaseUser user;
    View background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView imageView = findViewById(R.id.imageView);
        background=findViewById(R.id.background);
        Drawable bkg = background.getBackground();
        bkg.setAlpha(80);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);
        CreateSignIn();

    }
    public void CreateSignIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers).setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("On activity res is call", "Shreyas");
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.i("On res code is call", "Abhi");
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                user = FirebaseAuth.getInstance().getCurrentUser();
                Log.i("Sign-in Successful ", user.getUid());

                DatabaseReference
                UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
                UsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.hasChild(user.getUid())) {
                            // it exists!
                            Toast.makeText(MainActivity.this, "Has Child", Toast.LENGTH_SHORT).show();
                            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
                            ValueEventListener postListener = new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String username = dataSnapshot.child("Users").child(user.getUid()).child("name").getValue(String.class);
                                    String type = dataSnapshot.child("Users").child(user.getUid()).child("type").getValue(String.class);
                                    String status = dataSnapshot.child("Users").child(user.getUid()).child("status").getValue(String.class);
                                    Toast.makeText(MainActivity.this, ""+ type, Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPref = getSharedPreferences(getPackageName(),MODE_PRIVATE);
                                    sharedPref.edit().putString("username",username).commit();

                                    if(status.equals("CONTRACT")){
                                        String agreementId = dataSnapshot.child("Users").child(user.getUid()).child("agreementId").getValue(String.class);
                                        sharedPref.edit().putString("agreementId",agreementId).commit();
                                    }
                                    //sharedPref.edit().putString("username",username);

                                    Intent i = new Intent(MainActivity.this,WelcomeActivity.class);
                                    i.putExtra("type", type);
                                    i.putExtra("status",status);
                                    startActivity(i);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            };

                            dbRef.addValueEventListener(postListener);

                        }
                        else{
                            //doesn't exist!
                            Intent i = new Intent(MainActivity.this, ClientSignUpActivity.class);
                            startActivity(i);
                             }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent i = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(i);
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i("Sign-in Unsuccessful ", "This is Bad");
                // ...
            }
        }
    }
}

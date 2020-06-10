package application.example.myfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    View background;
    LinearLayout linearLayout;
    ImageView imageView;

    public Button signUpFarmerButton;
    public Button signUpClientButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        linearLayout = findViewById(R.id.linear_layout);
        signUpFarmerButton = findViewById(R.id.signUpFarmerButton);
        signUpFarmerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "Farmer", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignUpActivity.this, FarmerSignUpActivity.class);
                startActivity(i);
            }
        });
        signUpClientButton = findViewById(R.id.signUpClientButton);
        signUpClientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SignUpActivity.this, "Client", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(SignUpActivity.this, ClientSignUpActivity.class);
                startActivity(i);
            }
        });

        imageView = findViewById(R.id.imageView);
        background=findViewById(R.id.background);
        Drawable bkg = background.getBackground();
        bkg.setAlpha(80);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
             linearLayout.setVisibility(View.VISIBLE);
             imageView.setVisibility(View.GONE);
            }
        },6000);

    }
}

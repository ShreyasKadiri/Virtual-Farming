package application.example.myfarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {

    private String status;
    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        status = getIntent().getStringExtra("status");
        type = getIntent().getStringExtra("type");

        if( type.equals("CLIENT") && status.equals("ACTIVE")){
            Intent i = new Intent(WelcomeActivity.this,ViewFarmsActivity.class);
            startActivity(i);
        }
        else if (type.equals("CLIENT") && status.equals("SELECTED")){
            Intent i = new Intent(WelcomeActivity.this,ClientBidActivity.class);
            startActivity(i);
        }
        else if (type.equals("CLIENT") && status.equals("CONTRACT")){
            Intent i = new Intent(WelcomeActivity.this,ClientContractActivity.class);
            startActivity(i);
        }
        else if(type.equals("FARMER") && status.equals("ACTIVE")){
            Intent i = new Intent(WelcomeActivity.this,ViewClientsActivity.class);
            startActivity(i);
        }
        else if(type.equals("FARMER") && status.equals("CONTRACT")){
            Intent i = new Intent(WelcomeActivity.this,FarmerContractActivity.class);
            startActivity(i);
        }



    }
}

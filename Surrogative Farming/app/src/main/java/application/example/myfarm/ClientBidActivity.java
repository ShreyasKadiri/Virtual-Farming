package application.example.myfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClientBidActivity extends AppCompatActivity {

    public String uid;
    public String bidKey = "";
    public ClientClass currentClient;
    public FarmerClass currentFarmer;
    private DatabaseReference databaseReference;

    public String bidAmount;
    public ImageView farmImage;
    public TextView city;
    public TextView farmerName;
    public  TextView basePrice;
    public TextView cropsGrown;
    public TextView irrigation;
    public TextView phoneNumber;
    public ImageView phoneCallImg;
    public Button selectButton;

    public TextView bidAmountTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_bid);

        farmImage = findViewById(R.id.farm_image);
        city = findViewById(R.id.city_name_fc);
        farmerName = findViewById(R.id.farmer_name_fc);
        basePrice = findViewById(R.id.base_price_fc);
        cropsGrown = findViewById(R.id.crops_grown_fc);
        irrigation = findViewById(R.id.irrigation_type_fc);
        phoneNumber = findViewById(R.id.phone_number_fc);
        phoneCallImg = findViewById(R.id.phone_button_fc);
        selectButton = findViewById(R.id.select_button_fc);
        bidAmountTextView = findViewById(R.id.bid_amount);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeBid();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {

                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("Abhishek", ds.toString());
                        HashMap<Object, Object> hm = (HashMap<Object, Object>) ds.getValue();
                        Log.d("HASSAN", hm.get("type").toString());
                        if (hm.get("uid").toString().equals(uid)) {
                            ClientClass clientClass = new ClientClass();
                            clientClass = (ClientClass) ds.getValue(ClientClass.class);
                            currentClient = clientClass;
                            Log.d("ANGEL",clientClass.getName());
                        }
                    }
                   getFarm();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFarm(){
        Map<String,Object> map = new HashMap<>();
        map = currentClient.getBidslist();
        Set<String> mapKeys = map.keySet();

        for(String s : mapKeys){
            bidKey = s;
        }

        HashMap<String,String> hm = (HashMap<String, String>) map.get(bidKey);
        Log.d("MYSORE",hm.toString());
        Log.d("MYSORE",map.get(bidKey).toString());
        final String farmerUid = hm.get("farmerUid");

        bidAmount = hm.get("bidAmount");

       DatabaseReference farmerDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        farmerDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {

                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("Abhishek", ds.toString());
                        HashMap<Object, Object> hm = (HashMap<Object, Object>) ds.getValue();
                        Log.d("HASSAN", hm.get("type").toString());
                        if (hm.get("uid").toString().equals(farmerUid)) {
                            FarmerClass farmerClass = new FarmerClass();
                            farmerClass = (FarmerClass) ds.getValue(FarmerClass.class);
                            currentFarmer = farmerClass;
                            Log.d("ANGEL",farmerClass.getName());
                        }
                    }
                }

                getFarmerDetails();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getFarmerDetails() {
        Picasso.get().load(currentFarmer.getFarmImgUrl()).into(farmImage);
        farmerName.setText(currentFarmer.getName());
        city.setText(currentFarmer.getCity());
        basePrice.setText(currentFarmer.getBasePrice());
        cropsGrown.setText(currentFarmer.getCropsGrown());
        irrigation.setText(currentFarmer.getIrrigation());
        phoneNumber.setText(currentFarmer.getPhoneNumber());
        bidAmountTextView.setText(bidAmount);
    }

    public void removeBid(){
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentClient.getUid())
                .child("bidslist").child(bidKey).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Users").child(currentFarmer.getUid())
                .child("bidsList").child(bidKey).removeValue();

        FirebaseDatabase.getInstance().getReference().child("Users").child(currentClient.getUid())
                .child("status").setValue("ACTIVE");

    }


}

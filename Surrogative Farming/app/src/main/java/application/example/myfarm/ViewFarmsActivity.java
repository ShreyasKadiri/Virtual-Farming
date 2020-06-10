package application.example.myfarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewFarmsActivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {

    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private FarmsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    public ArrayList<FarmerClass> farms;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_farms);
        linearLayout = findViewById(R.id.linear_layout);

        farms = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(ViewFarmsActivity.this, "No Children", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d("Abhishek", ds.toString());
                        HashMap<Object, Object> hm = (HashMap<Object, Object>) ds.getValue();
                        Log.d("HASSAN", hm.get("type").toString());
                        if (hm.get("type").toString().equals("FARMER") && hm.get("status").toString().equals("ACTIVE")) {
                            FarmerClass farmerClass = new FarmerClass();
                            farmerClass = (FarmerClass) ds.getValue(FarmerClass.class);
                            farms.add(farmerClass);
                        }
                    }
                    enableUserInteraction();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void enableUserInteraction() {

        Log.d("Harsha", farms.size() + "");
        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new FarmsAdapter(farms);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        linearLayout.setVisibility(View.VISIBLE);
        Log.d("WISKY1",farms.get(0).getName());
        Log.d("WISKY2",farms.get(0).getCity());
        adapter.setOnItemClickListener(new FarmsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ViewFarmsActivity.this, ""+farms.get(position).getName(), Toast.LENGTH_SHORT).show();
                p  = position;
                openDialog(position);
            }
        });

    }

    public void openDialog(int position){
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"Example Dialog");
    }

    @Override
    public void setBidAmount(String bidAmount) {

        Toast.makeText(this, ""+bidAmount, Toast.LENGTH_SHORT).show();
        addToDataBase(bidAmount);
    }

    public void addToDataBase(String bidAmount){

        String farmerUid = farms.get(p).getUid();
        String clientUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        BidClass bid = new BidClass();
        bid.setBidAmount(bidAmount);
        bid.setFarmerUid(farmerUid);
        bid.setClientUid(clientUid);
        DatabaseReference clientDB = FirebaseDatabase.getInstance().getReference().child("Users").child(clientUid);
        DatabaseReference clientBidRef = clientDB.child("bidslist").push();
        String key = clientBidRef.getKey();
        bid.setBidUid(key);
        Map<String,Object> map = new HashMap<>();
        map.put(key,bid);
        clientBidRef = clientDB.child("bidslist");
        clientBidRef.updateChildren(map);

        DatabaseReference farmerDB = FirebaseDatabase.getInstance().getReference().child("Users").child(farmerUid);
        DatabaseReference farmerBidRef = farmerDB.child("bidsList");
        farmerBidRef.updateChildren(map);
        clientDB.child("status").setValue("SELECTED");
        Toast.makeText(this, "Successfully Selected", Toast.LENGTH_SHORT).show();


    }

}

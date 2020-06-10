package application.example.myfarm;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class ViewClientsActivity extends AppCompatActivity {

    public String farmerUid;
    public DatabaseReference databaseReference;
    public ArrayList<BidClass> bidsList;
    public ClientClass clientClass;

    private LinearLayout linearLayout;
    private RecyclerView recyclerView;
    private BidAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clients);
        linearLayout = findViewById(R.id.linear_layout);


        farmerUid  = FirebaseAuth.getInstance().getCurrentUser().getUid();

        bidsList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(farmerUid).child("bidsList");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    Toast.makeText(ViewClientsActivity.this, "No Children", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        HashMap<String,Object> hm = (HashMap<String, Object>) ds.getValue();
                        Log.d("FANCY", hm.toString());



                        BidClass b = new BidClass();
                        b.setBidUid(hm.get("bidUid").toString());
                        b.setClientUid(hm.get("clientUid").toString());
                        b.setFarmerUid(hm.get("farmerUid").toString());
                        b.setBidAmount(hm.get("bidAmount").toString());

                        bidsList.add(b);
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

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        adapter = new BidAdapter(bidsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        linearLayout.setVisibility(View.VISIBLE);
        adapter.setOnItemClickListener(new BidAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ViewClientsActivity.this, ""+bidsList.get(position).getBidAmount(), Toast.LENGTH_SHORT).show();
                //openDialog(position);
                getCLient(position);
            }

            @Override
            public void onDeleteItem(int position) {
                    deleteBid(position);
            }
        });

    }



    public void getCLient(final int position){
        final String clitentUid =  bidsList.get(position).getClientUid();
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if (dataSnapshot.getChildrenCount() == 0){
               //TODO
             }
             else{
                 for (DataSnapshot ds:dataSnapshot.getChildren()){

                     HashMap<String,Object> hm = (HashMap<String, Object>) ds.getValue();
                     if(hm.get("uid").toString().equals(clitentUid)){
                         clientClass = ds.getValue(ClientClass.class);
                     }
                 }
             }
             displayClient(position);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void displayClient(final int position) {
        String clientDetails = "Client Name : "+ clientClass.getName()
                + "\n Client City : " + clientClass.getCity()
                + "\n Client Phone Number : " +clientClass.getPhoneNumber();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewClientsActivity.this);
        alertDialogBuilder.setTitle("Client Details");
        alertDialogBuilder.setMessage(clientDetails);
        alertDialogBuilder.setPositiveButton("Agree to Contract ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                createAgreement(position);
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }


    public  void deleteBid(int position){

        BidClass bid = bidsList.get(position);
        FirebaseDatabase.getInstance().getReference().child("Users").child(bid.getClientUid())
                .child("bidslist").child(bid.getBidUid()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Users").child(bid.getFarmerUid())
                .child("bidsList").child(bid.getBidUid()).removeValue();
        FirebaseDatabase.getInstance().getReference().child("Users").child(bid.getClientUid())
                .child("status").setValue("ACTIVE");

        bidsList.remove(position);
        adapter.notifyDataSetChanged();

    }

    public void createAgreement(int position) {
        if ((position < bidsList.size())) {
            AgreementClass agreement = new AgreementClass();
            agreement.setBidAmount(bidsList.get(position).getBidAmount());
            agreement.setFarmerUid(bidsList.get(position).getFarmerUid());
            agreement.setClientUid(bidsList.get(position).getClientUid());

            DatabaseReference agreementDBRef;
            agreementDBRef = FirebaseDatabase.getInstance().getReference().child("Agreements");
            String key = agreementDBRef.push().getKey();
            agreement.setAgreementUid(key);
            agreementDBRef.child(key).setValue(agreement);
            String farmerUid = agreement.getFarmerUid();
            String clientUid = agreement.getClientUid();

            for (int i = 0; i < bidsList.size(); i++) {
                deleteBid(i);
            }

            FirebaseDatabase.getInstance().getReference().child("Users").child(clientUid)
                    .child("status").setValue("CONTRACT");
            FirebaseDatabase.getInstance().getReference().child("Users").child(clientUid)
                    .child("agreementId").setValue(key);
            FirebaseDatabase.getInstance().getReference().child("Users").child(farmerUid)
                    .child("status").setValue("CONTRACT");
            FirebaseDatabase.getInstance().getReference().child("Users").child(farmerUid)
                    .child("agreementId").setValue(key);

        }
    }
}

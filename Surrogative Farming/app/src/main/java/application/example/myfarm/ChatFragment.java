package application.example.myfarm;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends Fragment{

    View view;
    public TextView loadingTextView;
    public LinearLayout linearLayout;
    public RecyclerView recyclerView;
    public ArrayList<ChatClass> chatClassArrayList;
    public String userName;
    public String agreementId;

    private ChatAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chat,container,false);


        loadingTextView = view.findViewById(R.id.loading_text_view);
        linearLayout = view.findViewById(R.id.linear_layout);
        recyclerView =view.findViewById(R.id.recycler_view_report);
        chatClassArrayList = new ArrayList<>();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getActivity().getPackageName(),MODE_PRIVATE);
        userName = sharedPref.getString("username","user");
        agreementId = sharedPref.getString("agreementId","agreementId");
        loadData();

        return view;
    }


    public void loadData(){
        FirebaseDatabase.getInstance().getReference().child("Agreements").child(agreementId)
                .child("Chat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0){
                    MakeToast("NO CHATS HERE");
                }
                else{
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        HashMap<String,Object> hm = (HashMap<String,Object>) ds.getValue();
                        Log.d("MAHI",hm.toString());
                        ChatClass chatClass = new ChatClass();
                        chatClass.setSender(hm.get("sender").toString());
                        chatClass.setTimeStamp(Long.parseLong(hm.get("timeStamp").toString()));
                        chatClass.setMessage(hm.get("message").toString());
                        chatClassArrayList.add(chatClass);
                    }
                    sortList();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void enableUserInteraction(){
        if (chatClassArrayList.size() == 0){
            loadingTextView.setText("NO CHATS");
            return;
        }
        else{
            layoutManager = new LinearLayoutManager(getContext());
            adapter = new ChatAdapter(chatClassArrayList);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            linearLayout.setVisibility(View.VISIBLE);
            loadingTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void MakeToast(String infoString){
        Toast.makeText(getActivity(), ""+infoString, Toast.LENGTH_SHORT).show();
    }

    public void sortList()
    {
        Collections.sort(chatClassArrayList, new Comparator<ChatClass>() {
            @Override
            public int compare(ChatClass chatClass, ChatClass t1) {

                Long l = new Long(chatClass.getTimeStamp());
                return l.compareTo(t1.getTimeStamp());
            }
        });
        enableUserInteraction();
    }
}

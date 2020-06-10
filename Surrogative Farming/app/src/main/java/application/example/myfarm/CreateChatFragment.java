package application.example.myfarm;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

public class CreateChatFragment extends Fragment {

    View view;
    public String agreementId;
    public String userName;
    private Uri imageUri;
    public String uid;
    public ImageView imageView;
    public EditText editText;
    public Button button;
    private String imgUrl;
    private static final int PICK_IMAGE=100;

    private StorageReference photoRef;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStroageReference;
    private FirebaseStorage mFirebaseStorage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create_chat,container,false);
        editText = view.findViewById(R.id.text_chat);
        button = view.findViewById(R.id.submit_chat);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStroageReference = mFirebaseStorage.getReference();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getActivity().getPackageName(),MODE_PRIVATE);
        userName = sharedPref.getString("username","user");
        Log.d("carrot", userName);
        agreementId = sharedPref.getString("agreementId","agreementId");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        return view;
    }

    public void getData(){
        if(isEmpty()){
            return;
        }
        uploadData();
    }

    public boolean isEmpty(){
        if(editText.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Please enter text", Toast.LENGTH_SHORT).show();
            return  true;
        }
        return false;
    }


    public void MakeToast(String infoString){
        Toast.makeText(getActivity(), ""+infoString, Toast.LENGTH_SHORT).show();
    }

    public void uploadData(){
        String chatText = editText.getText().toString();
        ChatClass chatClass = new ChatClass();
        chatClass.setMessage(chatText);
        Long timeStamp = System.currentTimeMillis()/1000;
        chatClass.setTimeStamp(timeStamp);
        chatClass.setSender(userName);
        mDatabaseReference =  FirebaseDatabase.getInstance().getReference().child("Agreements").child(agreementId).child("Chat");
        String key = FirebaseDatabase.getInstance().getReference().child("test").push().getKey();
        HashMap<String,Object> hm = new HashMap<>();
        hm.put(key,chatClass);
        mDatabaseReference.updateChildren(hm);
    }
}

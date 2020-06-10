package application.example.myfarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class CreatePaymentFragment extends Fragment {

    View view;
    public String agreementId;
    public String clientName;
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
        view = inflater.inflate(R.layout.fragment_create_payment,container,false);
        imageView = view.findViewById(R.id.image_create_payment);
        editText = view.findViewById(R.id.text_create_payment);
        button = view.findViewById(R.id.submit_payment);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mFirebaseStorage = FirebaseStorage.getInstance();
        mStroageReference = mFirebaseStorage.getReference();

        SharedPreferences sharedPref = getActivity().getSharedPreferences(getActivity().getPackageName(),MODE_PRIVATE);
        clientName = sharedPref.getString("username","user");
        Log.d("carrot", clientName);
        agreementId = sharedPref.getString("agreementId","agreementId");

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(PICK_IMAGE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        return view;
    }
    public void selectImage(int p){

        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/jpeg");
        startActivityForResult(gallery,p);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);

            photoRef =
                    mStroageReference.child(imageUri.getLastPathSegment());
        }
    }

    public void getData(){
        if(isEmpty()){
            return;
        }
        uploadImage();
    }

    public boolean isEmpty(){
        if(imageUri.toString().trim().isEmpty() &&  editText.getText().toString().trim().isEmpty()){
            Toast.makeText(getContext(), "Please select image and enter text", Toast.LENGTH_SHORT).show();
            return  true;
        }
        return false;
    }

    public void  uploadImage(){

        UploadTask uploadTask = photoRef.putFile(imageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    MakeToast("Unable to upload Image");
                    throw task.getException();
                }
                return photoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imgUrl = downloadUri.toString();
                    MakeToast("Client Image Uploaded");
                    uploadData();
                } else {
                    MakeToast("Unable to upload the Image A");
                }
            }
        });
    }

    public void MakeToast(String infoString){
        Toast.makeText(getActivity(), ""+infoString, Toast.LENGTH_SHORT).show();
    }

    public void uploadData(){
        String paymentText = editText.getText().toString();
        PaymentClass paymentClass = new PaymentClass();
        paymentClass.setMessage(paymentText);
        paymentClass.setImgUrl(imgUrl);
        Long timeStamp = System.currentTimeMillis()/1000;
        paymentClass.setTimeStamp(timeStamp);
        paymentClass.setSenderName(clientName);
        paymentClass.setSenderUid(uid);
        mDatabaseReference =  FirebaseDatabase.getInstance().getReference().child("Agreements").child(agreementId).child("Payments");
        String key = FirebaseDatabase.getInstance().getReference().child("test").push().getKey();
        HashMap<String,Object> hm = new HashMap<>();
        hm.put(key,paymentClass);
        mDatabaseReference.updateChildren(hm);
    }
}

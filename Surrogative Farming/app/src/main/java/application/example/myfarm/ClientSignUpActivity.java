package application.example.myfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

public class ClientSignUpActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText cityEditText;
    private EditText emailIdEditText;
    private EditText upiIdEditText;
    private EditText ageEditText;
    private EditText phoneNumberEditText;
    private ImageButton imgBtn;
    private Button submitBtn;
    private static final int PICK_IMAGE=100;
    private Uri imageUri;

    private StorageReference photoRef;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStroageReference;
    private FirebaseStorage mFirebaseStorage;

    private String name;
    private String city;
    private String imgUrl;
    private String emailId;
    private String status;
    private String agreementId;
    private String uid;
    private String type;
    private String age;
    private String phoneNumber;
    public ClientClass clientClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_sign_up);
        nameEditText = findViewById(R.id.client_name);
        cityEditText = findViewById(R.id.city_name);
        emailIdEditText = findViewById(R.id.email_id);
        upiIdEditText = findViewById(R.id.upi_id);
        ageEditText = findViewById(R.id.client_age);
        phoneNumberEditText = findViewById(R.id.client_phone_number);
        imgBtn = findViewById(R.id.client_image);
        submitBtn = findViewById(R.id.client_signup_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        mFirebaseDataBase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDataBase.getReference().child("Users");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStroageReference = mFirebaseStorage.getReference();

        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage(PICK_IMAGE);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Submit();
            }
        });
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
            imgBtn.setImageURI(imageUri);

            photoRef =
                    mStroageReference.child(imageUri.getLastPathSegment());
        }
    }

    public boolean isEmpty(){
        if(nameEditText.getText().toString().trim().isEmpty() ||
                cityEditText.getText().toString().trim().isEmpty() ||
                emailIdEditText.getText().toString().trim().isEmpty() ||
                upiIdEditText.getText().toString().trim().isEmpty() ||
                ageEditText.getText().toString().trim().isEmpty()||
                phoneNumberEditText.getText().toString().trim().isEmpty() ||
                imageUri == null){
            return true;
        }else{
            return false;
        }
    }

    public void MakeToast(String infoString){
        Toast.makeText(ClientSignUpActivity.this, ""+infoString, Toast.LENGTH_SHORT).show();
    }
    public void Submit(){

        if(isEmpty()){
            MakeToast("Fields and Images can't be empty");
            return;
        }
        uploadImg();
    }

    public void uploadToDatabase(){

        clientClass = new ClientClass();
        clientClass.setName(nameEditText.getText().toString());
        clientClass.setCity(cityEditText.getText().toString());
        clientClass.setImgUrl(imgUrl);
        clientClass.setEmailId(emailIdEditText.getText().toString());
        clientClass.setStatus("ACTIVE");
        clientClass.setAgreementId(null);
        clientClass.setUid(uid);
        clientClass.setType("CLIENT");
        clientClass.setUpiId(upiIdEditText.getText().toString());
        clientClass.setAge(ageEditText.getText().toString());
        clientClass.setPhoneNumber(phoneNumberEditText.getText().toString());
        Log.d("CLIENT",clientClass.toString());

        mDatabaseReference.child(uid).setValue(clientClass);

    }

    public void uploadImg(){

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
                    uploadToDatabase();
                } else {
                    MakeToast("Unable to upload the Image A");
                }
            }
        });
    }
}

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

public class FarmerSignUpActivity extends AppCompatActivity {


    private EditText nameEditText;
    private EditText cityEditText;
    private EditText emailIdEditText;
    private EditText cropsGrownEditText;
    private EditText upiIdEditText;
    private EditText irrigationEditText;
    private EditText ageEditText;
    private EditText phoneNumberEditText;
    private EditText basePriceEditText;
    private ImageButton imgBtn;
    private ImageButton farmImgBtn;
    private Button submitBtn;
    private static final int PICK_IMAGE=100;
    private static final int PICK_FARM_IMAGE=101;
    private Uri imageUri;
    private Uri farmImageUri;

    private StorageReference farmerPhotoRef;
    private StorageReference farmPhotoRef;
    private FirebaseDatabase mFirebaseDataBase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStroageReference;
    private FirebaseStorage mFirebaseStorage;

    private String name;
    private String city;
    private String imgUrl;
    private String farmImgUrl;
    private String emailId;
    private String status;
    private String agreementId;
    private String uid;
    private String type;
    private String cropsGrown;
    private String upiId;
    private String irrigation;
    private int age;
    private int phoneNumber;
    private int basePrice;
    public FarmerClass farmerClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_sign_up);
        nameEditText = findViewById(R.id.farmer_name);
        cityEditText = findViewById(R.id.city_name);
        emailIdEditText = findViewById(R.id.email_id);
        cropsGrownEditText = findViewById(R.id.crops_grown);
        upiIdEditText = findViewById(R.id.upi_id);
        irrigationEditText = findViewById(R.id.irrigation_type);
        ageEditText = findViewById(R.id.farmer_age);
        phoneNumberEditText = findViewById(R.id.farmer_phone_number);
        basePriceEditText = findViewById(R.id.base_price);
        imgBtn = findViewById(R.id.farmer_image);
        farmImgBtn = findViewById(R.id.farm_image);
        submitBtn = findViewById(R.id.farmer_signup_btn);

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

        farmImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    selectImage(PICK_FARM_IMAGE);
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

            farmerPhotoRef =
                    mStroageReference.child(imageUri.getLastPathSegment());
        }
        if(resultCode == RESULT_OK && requestCode == PICK_FARM_IMAGE){
            farmImageUri = data.getData();
            farmImgBtn.setImageURI(farmImageUri);

            farmPhotoRef =
                    mStroageReference.child(farmImageUri.getLastPathSegment());
        }
    }

    public boolean isEmpty(){
        if(nameEditText.getText().toString().trim().isEmpty() ||
                cityEditText.getText().toString().trim().isEmpty() ||
                emailIdEditText.getText().toString().trim().isEmpty() ||
                cropsGrownEditText.getText().toString().trim().isEmpty() ||
                upiIdEditText.getText().toString().trim().isEmpty() ||
                irrigationEditText.getText().toString().trim().isEmpty() ||
                ageEditText.getText().toString().trim().isEmpty()||
                phoneNumberEditText.getText().toString().trim().isEmpty() ||
                basePriceEditText.getText().toString().trim().isEmpty()||
                imageUri == null || farmImageUri == null){
            return true;
        }else{
            return false;
        }
    }

    public void MakeToast(String infoString){
        Toast.makeText(FarmerSignUpActivity.this, ""+infoString, Toast.LENGTH_SHORT).show();
    }
    public void Submit(){

        if(isEmpty()){
            MakeToast("Fields and Images can't be empty");
        return;
        }
        uploadFarmerImg();
    }

    public void uploadToDatabase(){

        farmerClass = new FarmerClass();
        farmerClass.setName(nameEditText.getText().toString());
        farmerClass.setCity(cityEditText.getText().toString());
        farmerClass.setImgUrl(imgUrl);
        farmerClass.setFarmImgUrl(farmImgUrl);
        farmerClass.setEmailId(emailIdEditText.getText().toString());
        farmerClass.setStatus("ACTIVE");
        farmerClass.setAgreementId(null);
        farmerClass.setUid(uid);
        farmerClass.setType("FARMER");
        farmerClass.setCropsGrown(cropsGrownEditText.getText().toString());
        farmerClass.setUpiId(upiIdEditText.getText().toString());
        farmerClass.setIrrigation(irrigationEditText.getText().toString());
        farmerClass.setAge(ageEditText.getText().toString());
        farmerClass.setPhoneNumber(phoneNumberEditText.getText().toString());
        farmerClass.setBasePrice(basePriceEditText.getText().toString());
        Log.d("FARMER",farmerClass.toString());

        mDatabaseReference.child(uid).setValue(farmerClass);

    }

    public void uploadFarmerImg(){

        UploadTask uploadTask = farmerPhotoRef.putFile(imageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    MakeToast("Unable to upload Image");
                    throw task.getException();
                }
                return farmerPhotoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imgUrl = downloadUri.toString();
                    MakeToast("Farmer Image Uploaded");
                    uploadFarmImg();
                } else {
                    MakeToast("Unable to upload the Image A");
                }
            }
        });
    }

    public void uploadFarmImg(){

        UploadTask uploadTask = farmPhotoRef.putFile(farmImageUri);
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    MakeToast("Unable to upload Farm Image");
                    throw task.getException();
                }
                return farmPhotoRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    farmImgUrl = downloadUri.toString();
                    MakeToast("Farm Image Uploaded");
                    uploadToDatabase();
                } else {
                    MakeToast("Unable to upload the Image B");
                }
            }
        });

    }

}

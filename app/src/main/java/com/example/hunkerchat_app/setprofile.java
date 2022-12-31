package com.example.hunkerchat_app;

import static android.provider.MediaStore.Images.Media.getBitmap;
import static android.provider.MediaStore.MediaColumns.DATA;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.location.LocationRequestCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.math.Quantiles;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class setprofile extends AppCompatActivity {
    private CardView mgetuserimage;
    private ImageView mgetuserimagebackground;
    private static int PICK_IMAGE = 123;
    private Uri imagepath;
    private EditText mgetusername;
    private android.widget.Button mgetsaveprofile;
    private FirebaseAuth firebaseAuth;
    private String name;
    private FirebaseStorage firebaseStorage;

    private StorageReference storageReference;
    private String ImageUriAccessToken;
    private FirebaseFirestore firebaseFirestore;
    ProgressBar mprogressofsetprofile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setprofile);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        mgetuserimagebackground = findViewById(R.id.userimagebackgrounds);
        mgetuserimage = findViewById(R.id.userimage);
        mgetusername = findViewById(R.id.username);
        mgetsaveprofile = findViewById(R.id.saveprofile);
        mprogressofsetprofile = findViewById(R.id.progress_circular);

        mgetuserimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);

            }
        });

        mgetsaveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = mgetusername.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Your name is empty", Toast.LENGTH_SHORT).show();


                } else if (imagepath == null) {
                    Toast.makeText(getApplicationContext(), "Image is null fucker!", Toast.LENGTH_SHORT).show();

                } else {
                    mprogressofsetprofile.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    mprogressofsetprofile.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(setprofile.this, chatActivity.class);
                    startActivity(intent);
                    finish();
                }
            }


        });

    }

    private void sendDataForNewUser()
    {
        sendDataToRealTimeDatabase();
    }
    private void sendDataToRealTimeDatabase()
    {
        name=mgetusername.getText().toString().trim();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        userprofile muserprofile=new userprofile(name,firebaseAuth.getUid());
        databaseReference.setValue(muserprofile);
        Toast.makeText(getApplicationContext(),"userprofile added successfully",Toast.LENGTH_SHORT).show();
        sendImagetoStorage();

    }

    private void sendImagetoStorage()
    {
        StorageReference imageref=storageReference.child("image").child(firebaseAuth.getUid()).child("profile pic");

        Bitmap bitmap=null;
        try {
            bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25,byteArrayOutputStream);




       UploadTask uploadTask=imageref.putBytes(DATA.getBytes(StandardCharsets.UTF_8));

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        ImageUriAccessToken=uri.toString();
                        Toast.makeText(getApplicationContext(),"successfullfully uploaded",Toast.LENGTH_SHORT).show();
                        sendDataToCloudFirestore();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to upload",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"image is not uploaded",Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(),"image uploaded successfully",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendDataToCloudFirestore() {
        DocumentReference documentReference=firebaseFirestore.collection("user").document(firebaseAuth.getUid());
        Map<String,Object> Userdata=new HashMap<>();

        Userdata.put("name",name);
        Userdata.put("image",ImageUriAccessToken);
        Userdata.put("Uid",firebaseAuth.getUid());
        Userdata.put("status","online");


        documentReference.set(Userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "sent fucker", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==PICK_IMAGE && requestCode==RESULT_OK)
        {
            imagepath=data.getData();
            mgetuserimagebackground.setImageURI(imagepath);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
}
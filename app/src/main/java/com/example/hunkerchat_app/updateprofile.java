package com.example.hunkerchat_app;

import static android.provider.MediaStore.MediaColumns.DATA;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class updateprofile extends AppCompatActivity {
    EditText mviewusername;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseFirestore firebaseFirestore;
    private ImageView mgetnewuserimageonimageview;
    StorageReference storageReference;
    private String ImageURLaccessToken;
    androidx.appcompat.widget.Toolbar mtoolbarofupdateprofile;
    private ImageButton mbackbuttonofupdateprofile;
    private FirebaseStorage firebaseStorage;
    ProgressBar mprogressbarupdateprofile;
    private Uri imagepath;
    Intent intent;
    private static int PICK_IMAGE = 123;
    String newname;
    android.widget.Button mupdateprofilebutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updateprofile);
        mviewusername = findViewById(R.id.getnewuserimage);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mbackbuttonofupdateprofile = findViewById(R.id.backbuttonupdateprofile);
        mprogressbarupdateprofile = findViewById(R.id.progressbarupdateprofile);
        mgetnewuserimageonimageview = findViewById(R.id.getnewuserimageonimageview);
        mtoolbarofupdateprofile = findViewById(R.id.toolbarofupdateprofile);
        mupdateprofilebutton = findViewById(R.id.updateprofilebutton);
        intent = getIntent();
        setSupportActionBar(mtoolbarofupdateprofile);
        mbackbuttonofupdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mviewusername.setText(intent.getStringExtra("nameofuser"));


        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        mupdateprofilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newname = mviewusername.getText().toString();
                if (newname.isEmpty()) {

                    Toast.makeText(updateprofile.this, "Hello username Empty!!", Toast.LENGTH_SHORT).show();
                } else if (imagepath != null){
                    mprogressbarupdateprofile.setVisibility(View.INVISIBLE);
                    userprofile muserprofile=new userprofile(newname,firebaseAuth.getUid());
                     databaseReference.setValue(muserprofile);
                    updateimagetostorage();
                    Toast.makeText(updateprofile.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                    mprogressbarupdateprofile.setVisibility(View.INVISIBLE);
                    Intent intent=new Intent(updateprofile.this,chatActivity.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    mprogressbarupdateprofile.setVisibility(View.INVISIBLE);
                    userprofile muserprofile=new userprofile(newname,firebaseAuth.getUid());
                    databaseReference.setValue(muserprofile);
                    updatenameoncloudfirestore();
                    Toast.makeText(updateprofile.this,"uploaded successfully",Toast.LENGTH_SHORT).show();
                    mprogressbarupdateprofile.setVisibility(View.VISIBLE);
                    Intent intent=new Intent(updateprofile.this,chatActivity.class);
                    startActivity(intent);
                    finish();

                }


            }


        });
        mgetnewuserimageonimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent,PICK_IMAGE);


            }
        });
        storageReference=firebaseStorage.getReference();
        storageReference.child("image").child(firebaseAuth.getUid()).child("profic pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURLaccessToken=uri.toString();
                Picasso.get().load(uri).into(mgetnewuserimageonimageview);

            }
        });


    }

    private void updatenameoncloudfirestore() {
        DocumentReference documentReference=firebaseFirestore.collection("user").document(firebaseAuth.getUid());
        Map<String,Object> Userdata=new HashMap<>();

        Userdata.put("name",newname);
        Userdata.put("image",ImageURLaccessToken);
        Userdata.put("Uid",firebaseAuth.getUid());
        Userdata.put("status","online");


        documentReference.set(Userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "sent fucker", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateimagetostorage() {
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
                        ImageURLaccessToken=uri.toString();
                        Toast.makeText(getApplicationContext(),"successfullfully updated",Toast.LENGTH_SHORT).show();
                        updatenameoncloudfirestore();



                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Failed to updated",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"image is not updated",Toast.LENGTH_SHORT).show();
                    }
                });
                Toast.makeText(getApplicationContext(),"image updated successfully",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"successfully",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PICK_IMAGE && requestCode == RESULT_OK) {
            imagepath = data.getData();
            mgetnewuserimageonimageview.setImageURI(imagepath);
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    protected void onStop() {
        super.onStop();
        DocumentReference documentReference=firebaseFirestore.collection("username").document(firebaseAuth.getUid());
        documentReference.update("status","offline").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"User have Gone Offline/@hunker",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        DocumentReference documentReference=firebaseFirestore.collection("username").document(firebaseAuth.getUid());
        documentReference.update("status","online").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(),"User have Gone online/@hunker",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
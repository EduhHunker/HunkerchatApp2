package com.example.hunkerchat_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    EditText mviewusername;
    TextView mmovetoupdatetoprofile;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    private String ImageURLAcessToken;
    ImageView mviewuserimageinimageview;
    ImageButton mbackbuttonofviewprofile;
    androidx.appcompat.widget.Toolbar mtoolbarofvieewprofile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mviewusername = findViewById(R.id.viewusername);
        mmovetoupdatetoprofile = findViewById(R.id.movetoupdateprofile);
        firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mviewuserimageinimageview=findViewById(R.id.viewuserimageinimageview);
        mbackbuttonofviewprofile=findViewById(R.id.backbuttonupdateprofile);
        setSupportActionBar(mtoolbarofvieewprofile);

        mbackbuttonofviewprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        storageReference=firebaseStorage.getReference();
        storageReference.child("image").child(firebaseAuth.getUid()).child("profile pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                ImageURLAcessToken=uri.toString();
                Picasso.get().load(uri).into(mviewuserimageinimageview);

            }
        });
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userprofile muserprofile=snapshot.getValue(userprofile.class);
                mviewusername.setText(muserprofile.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(profile.this, "Failed to fetch", Toast.LENGTH_SHORT).show();
            }
        });
        mmovetoupdatetoprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(profile.this,updateprofile.class);
                startActivity(intent);

            }
        });
    }
}


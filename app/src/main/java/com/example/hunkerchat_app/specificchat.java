package com.example.hunkerchat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class specificchat extends AppCompatActivity {
    EditText mgetmessage;
    ImageButton msendmessagebutton;
    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbarofspeficchat;
    ImageView mspecificuserimageinimageview;
    TextView mNamespecificuser;
    private String enteredmessage;
    Intent intent;
    String mrecievername,msendername,mrecieveruid,msenderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderroom,receiverroom;
    RecyclerView mmessagerecyclerview;
    ImageButton mbackbuttonofspecificchat;
    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    messageAdapter messageAdapter;
    ArrayList<Message>messageArrayList;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specificchat);
        mgetmessage=findViewById(R.id.getmessage);
        msendmessagecardview=findViewById(R.id.cardviewofspecifuser);
        msendmessagebutton=findViewById(R.id.imageviewsendmessage);
        mtoolbarofspeficchat=findViewById(R.id.toolbarofspeficchat);
        mNamespecificuser=findViewById(R.id.Namespecificuser);
        mspecificuserimageinimageview=findViewById(R.id.specificuserimageinimageview);
        mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);
        messageArrayList=new ArrayList<>();
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        mmessagerecyclerview.setAdapter(messageAdapter);



        intent=getIntent();
        setSupportActionBar(mtoolbarofspeficchat);
        mtoolbarofspeficchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Hello son you clicked toolbar!!",Toast.LENGTH_SHORT).show();
            }
        });
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar=Calendar.getInstance();
        }
        simpleDateFormat=new SimpleDateFormat("hh:mm a");
        msenderuid=firebaseAuth.getUid();
        mrecieveruid=getIntent().getStringExtra("receiveruid");
        mrecievername=getIntent().getStringExtra("name");
        senderroom=mrecieveruid+msenderuid;
        receiverroom=mrecieveruid+msenderuid;
        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();;
            }
        });

        mNamespecificuser.setText(mrecievername);
        String uri=intent.getStringExtra("imageuri");
        if (uri.isEmpty()){
            Toast.makeText(getApplicationContext(),"No image is received",Toast.LENGTH_SHORT).show();
        }else{
            Picasso.get().load(uri).into(mspecificuserimageinimageview);
        }
        msendmessagebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredmessage=mgetmessage.getText().toString();
                if(enteredmessage.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Null Fan!!!",Toast.LENGTH_SHORT).show();
                }else{
                    Date date=new Date();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        currenttime=simpleDateFormat.format(calendar.getTime());
                    }
                    Message message=new message(enteredmessage,firebaseAuth.getUid(),date.getTime(),currenttime);
                        firebaseDatabase=FirebaseDatabase.getInstance();
                        firebaseDatabase.getReference().child("chats")
                                .child(senderroom)
                                .child("message")
                                .push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        firebaseDatabase.getReference()
                                                .child("chat")
                                                .child(receiverroom)
                                                .child("message")
                                                .push().setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                    }
                                                });

                                    }
                                });
                        mgetmessage.setText(null);

                }
            }
        });
    }
}
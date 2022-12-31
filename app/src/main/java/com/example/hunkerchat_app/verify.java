package com.example.hunkerchat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class verify extends AppCompatActivity {
    TextView mchangenumber;
    EditText mgetotp;
    android.widget.Button mverify_otp;
    String enterotp;
    FirebaseAuth firebaseAuth;
    ProgressBar mprogress_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mchangenumber=findViewById(R.id.changenumber);
        mverify_otp=findViewById(R.id.verify_otp);
        mgetotp=findViewById(R.id.verifyOTP);
        mprogress_circular=findViewById(R.id.progress_circular);

        firebaseAuth=FirebaseAuth.getInstance();
        mchangenumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(verify.this,MainActivity.class);
                startActivity(intent);
            }
        });
        mverify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterotp=mgetotp.getText().toString();
                if(enterotp.isEmpty()){
                    Toast.makeText(verify.this, "Enter The sent Otp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogress_circular.setVisibility(View.VISIBLE);
                    String codereceived=getIntent().getStringExtra("OTP");
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(codereceived,enterotp);

                    signWithPhoneAuthCredential(credential);


                }
            }
        });

    }
    private void signWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mprogress_circular.setVisibility(View.INVISIBLE);
                    Toast.makeText(verify.this, "successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(verify.this,setprofile.class);
                    startActivity(intent);
                    finish();
                }else
                {
                    if(task.getException()instanceof FirebaseAuthInvalidCredentialsException)
                        mprogress_circular.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
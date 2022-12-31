package com.example.hunkerchat_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import io.grpc.Context;

public class chatActivity extends AppCompatActivity {
    TabLayout tabLayout;
    TabLayout mchat, mstatus, mcall;
    ViewPager viewPager;
    pageAdapter pageAdapter;
    android.widget.Toolbar mtoolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_baseline_more_vert_24);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        viewPager=findViewById(androidx.fragment.R.id.fragment_container_view_tag);

        tabLayout = findViewById(R.id.include);
        mchat = findViewById(R.id.chat);
        mstatus = findViewById(R.id.status);
        mcall = findViewById(R.id.calls);
        mtoolbar = findViewById(R.id.toolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();



        pageAdapter=new pageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition()==0||tab.getPosition()==1||tab.getPosition()==2)
                {
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.setting:
                Intent intent=new Intent(chatActivity.this, profile.class);
                startActivity(intent);
                break;
                case R.id.aboutme:
                    Intent intent1=new Intent(chatActivity.this,chatActivity.class);
                    startActivity(intent1);
                    finish();

            case R.id.profile:
                Intent intent2=new Intent(chatActivity.this,chatActivity.class);
                startActivity(intent2);
                finish();
            case R.id.Massmessage:
                Intent intent3=new Intent(chatActivity.this,chatActivity.class);
                Toast.makeText(getApplicationContext(),"Hello Fan Coming Soon",Toast.LENGTH_SHORT).show();
            case R.id.Hunker:
                Intent intent4=new Intent(chatActivity.this,chatActivity.class);
                Toast.makeText(getApplicationContext(),"Chill!!!!",Toast.LENGTH_SHORT).show();
            case R.id.messagenumber:
                Intent intent5=new Intent(chatActivity.this,chatActivity.class);
                Toast.makeText(getApplicationContext(),"OOOOhho!!!",Toast.LENGTH_SHORT).show();


        }




        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
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
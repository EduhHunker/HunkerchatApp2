package com.example.hunkerchat_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class chatfragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    private FirebaseAuth firebaseAuth;
    ImageView mimageviewofuser;
    FirestoreRecyclerAdapter<firebasemodel,NoteviewHolder>chatAdapter;
    RecyclerView mrecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chatfragments, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mrecyclerView = v.findViewById(R.id.recyclerview);

        /// Query query = firebaseFirestore.collection("username");
        Query query=firebaseFirestore.collection("username").whereEqualTo("uid",firebaseAuth.getUid());
        FirestoreRecyclerOptions<firebasemodel> allusername = new FirestoreRecyclerOptions.Builder<firebasemodel>()
                .setQuery(query, firebasemodel.class)
                .build();

        chatAdapter = new FirestoreRecyclerAdapter<firebasemodel, NoteviewHolder>(allusername) {
            @Override
            protected void onBindViewHolder(@NonNull NoteviewHolder noteviewHolder , int i, @NonNull firebasemodel  firebase) {
                String uri=firebasemodel.getImage();
                noteviewHolder.particularusername.setText(firebasemodel.getStatus());


                Picasso.get().load(uri).into(mimageviewofuser);
                if (firebasemodel.getStatus().equals("Online"))
                {
                    noteviewHolder.statusofuser.setText(firebasemodel.getStatus());
                    noteviewHolder.statusofuser.setTextColor(Color.BLUE);

                }else
                {
                    noteviewHolder.statusofuser.setText(firebasemodel.getStatus());
                }
                noteviewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getActivity(),specificchat.class);
                        intent.putExtra("name",firebasemodel.getName());
                        intent.putExtra("receiveruid",firebasemodel.image);
                        intent.putExtra("imageuri",firebasemodel.getName());
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public NoteviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
                return new NoteviewHolder(view);
            }
        };
        mrecyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerView.setLayoutManager(linearLayoutManager);
        mrecyclerView.setAdapter(chatAdapter);
        return v;
    }
    public class NoteviewHolder extends RecyclerView.ViewHolder
    {
        private TextView particularusername;
        private TextView statusofuser;


        public NoteviewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.username);
            statusofuser=itemView.findViewById(R.id.statusofuser);
            mimageviewofuser=itemView.findViewById(R.id.imageviewofuser);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        chatAdapter.stopListening();
    }
}

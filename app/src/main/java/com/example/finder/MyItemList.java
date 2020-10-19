package com.example.finder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MyItemList extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userID;
    RecyclerView recyclerView;
    MyItemListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_item_list);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userID = currentUser.getUid();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);

        loadData();

    }
    private void loadData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<recyclerViewModelClass> options =
                new FirebaseRecyclerOptions.Builder<recyclerViewModelClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("userId").equalTo(userID)
                                , recyclerViewModelClass.class)
                        .build();

        adapter = new MyItemListAdapter(options);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //   progressBar.setVisibility(View.INVISIBLE);
        adapter.stopListening();
    }
}//
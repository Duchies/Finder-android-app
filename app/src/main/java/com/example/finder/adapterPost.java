package com.example.finder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class adapterPost extends FirebaseRecyclerAdapter<recyclerViewModelClass, adapterPost.myViewHoldler> {


    public adapterPost(@NonNull FirebaseRecyclerOptions<recyclerViewModelClass> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final myViewHoldler holder, final int position, @NonNull final recyclerViewModelClass model) {


    //    final recyclerViewModelClass example = data[position];

        holder.tittle.setText(model.getTitle());
        Glide.with(holder.imageView.getContext()).load(model.getPicture()).into(holder.imageView);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent postDetailIndend = new Intent(view.getContext(), PostDetail.class);

                postDetailIndend.putExtra("tag",holder.tittle.getText().toString());
                postDetailIndend.putExtra("imageUrl",model.getPicture().toString());
                postDetailIndend.putExtra("foundLocation",model.getDescription().toString());

                view.getContext().startActivity(postDetailIndend);

            }
        });


    }

    @NonNull
    @Override
    public myViewHoldler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_post, parent, false);
        return new myViewHoldler(view);

    }

    class myViewHoldler extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tittle;
        ConstraintLayout layout;
      //  TextView discription;

        public myViewHoldler(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.row_post_img);
            tittle = itemView.findViewById(R.id.row_post_title);
            layout = itemView.findViewById(R.id.layout);

        }
    }
}

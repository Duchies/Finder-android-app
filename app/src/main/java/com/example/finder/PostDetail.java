package com.example.finder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PostDetail extends AppCompatActivity {

    ImageView imageView;
    TextView tag,foundLocation;
    FirebaseUser currentUser ;
    FirebaseAuth mAuth;
    Button claimBtn;
    String userName,phone,email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        imageView = findViewById(R.id.mainImageView);
        tag = findViewById(R.id.tag);
        foundLocation = findViewById(R.id.foundLocation);
        claimBtn = findViewById(R.id.claimBtn);

        Bundle bundle = getIntent().getExtras();

        String _tag = bundle.getString("tag");
        String _discription = bundle.getString("foundLocation");
        String _imageUrl = bundle.getString("imageUrl");

        Glide.with(imageView.getContext()).load(_imageUrl).into(imageView);
        tag.setText(_tag);
        foundLocation.setText(_discription);

        claimBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(_tag);
            }
        });

        userName  = currentUser.getDisplayName().toString();
        phone = currentUser.getPhoneNumber().toString();
        email = currentUser.getEmail().toString();

    }

 private void showDialog(String tittle) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.alert_dialog, null);

        Button contactBtn = view.findViewById(R.id.dialog_contactButton);

        TextView textViewUserName = view.findViewById(R.id.dialog_username);
        TextView textViewUserEmail = view.findViewById(R.id.dialog_usermail);
        TextView textViewUserPhoneNumber = view.findViewById(R.id.dialog_userphone);

       textViewUserName.setText(userName);
       textViewUserEmail.setText(email);
       if (phone.equals("")){
           textViewUserPhoneNumber.setText("7011252597");
       }else {
           textViewUserPhoneNumber.setText(phone);
       }
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Log.e(TAG, "onClick: contact button");
//                Intent i=new Intent(android.content.Intent.ACTION_SEND);
//                i.setType("text/plain");
//                i.putExtra(android.content.Intent.EXTRA_SUBJECT,"Reason For Contact");
//                i.putExtra(android.content.Intent.EXTRA_TEXT, "text that you want to put");
//                startActivity(Intent.createChooser(i,"Contact via"));

//                Intent intent = new Intent(PostDetail.this,GroupChat.class);
//                intent.putExtra("title",tittle);
//                intent.putExtra("name",currentUser.getDisplayName());
//                intent.putExtra("email",currentUser.getEmail());
//                startActivity(intent);

                Intent intent = new Intent(PostDetail.this,Chat.class);
                startActivity(intent);

//

            }
        });

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        alertDialog.show();

    }
}
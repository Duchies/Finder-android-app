package com.example.finder;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class MainActivity extends AppCompatActivity {

    private static final int PReqCode = 2;
    private static final int REQUESCODE = 2;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    adapterPost adapter;

    ProgressBar progressBar;
    RecyclerView recyclerView;



    Dialog popAddPost;
    ImageView popupPostImage, popupAddBtn;
    TextView popupTitle, popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }



        initPopUp();
        setupPopupImageClick();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        progressBar = findViewById(R.id.progress_bar);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        loadData();


    }

    private void loadData() {

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<recyclerViewModelClass> options =
                new FirebaseRecyclerOptions.Builder<recyclerViewModelClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts")
                                , recyclerViewModelClass.class)
                        .build();

        adapter = new adapterPost(options);
        recyclerView.setAdapter(adapter);


    }

    private void initPopUp() {

        popAddPost = new Dialog(this);

        popAddPost.setContentView(R.layout.pop_up_add);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;

        // init popup widgets


        popupPostImage = popAddPost.findViewById(R.id.popup_img);
        popupTitle = popAddPost.findViewById(R.id.popup_title);
        popupDescription = popAddPost.findViewById(R.id.popup_description);
        popupAddBtn = popAddPost.findViewById(R.id.popup_add);
        popupClickProgress = popAddPost.findViewById(R.id.popup_progressBar);

        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);


                if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && pickedImgUri != null) {

                    //everything is okey no empty or null value
                    // TODO Create Post Object and add it to firebase database
                    // first we need to upload post Image
                    // access firebase storage
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("blog_images");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    // create post Object
                                    Post post = new Post(popupTitle.getText().toString(),
                                            popupDescription.getText().toString().toLowerCase(),
                                            imageDownlaodLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString());

                                    // Add post to firebase database

                                    addPost(post);


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong uploading picture

                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);


                                }
                            });


                        }
                    });
                } else {
                    showMessage("Please verify all input fields and choose Post Image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);

                }


            }
        });


    }


    private void setupPopupImageClick() {


        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // here when image clicked we need to open the gallery
                // before we open the gallery we need to check if our app have the access to user files
                // we did this before in register activity I'm just going to copy the code to save time ...

                checkAndRequestForPermission();


            }
        });


    }


    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(MainActivity.this, "Please accept for required permission", Toast.LENGTH_SHORT).show();

            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        } else
            // everything goes well : we have permission to access user gallery
            openGallery();

    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            popupPostImage.setImageURI(pickedImgUri);

        }


    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Posts").push();

        // get post unique ID and upadte post key
        String key = myRef.getKey();
        post.setPostKey(key);


        // add post data to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Post Added successfully");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddPost.dismiss();
            }
        });


    }


    private void showMessage(String message) {

        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_items, menu);

        MenuItem item = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) item.getActionView();

        searchView.setQueryHint("Search here");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                firebaseUserSearch(s.toLowerCase());

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                firebaseUserSearch(s.toLowerCase());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void firebaseUserSearch(String query) {

        //recyclerView.removeAllViewsInLayout();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<recyclerViewModelClass> options1 =
                new FirebaseRecyclerOptions.Builder<recyclerViewModelClass>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Posts").orderByChild("description")
                                        .startAt(query)
                                        .endAt(query + "\uf8ff")
                                , recyclerViewModelClass.class)
                        .build();

        adapter = new adapterPost(options1);
        adapter.startListening();
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.INVISIBLE);

    }


    public void logoutClick(MenuItem item) {

        FirebaseAuth.getInstance().signOut();
        Intent out_to_login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(out_to_login);
        finish();

    }


    public void myUploadClick(MenuItem item) {

        Intent intent = new Intent(getApplicationContext(), MyItemList.class);
        startActivity(intent);

    }

    public void ChatCheckClick(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(),Chat.class);
        startActivity(intent);


    }
}





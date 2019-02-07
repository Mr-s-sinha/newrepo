package android.com.chatbox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import id.zelory.compressor.Compressor;

public class NewpostActivity extends AppCompatActivity {


    EditText description;
    Button uploadbtn;

    private String current_user_id ;

    private ProgressBar progressBar;

    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth auth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpost);

        description = findViewById(R.id.description);
        uploadbtn = findViewById(R.id.upload);
        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        current_user_id = auth.getCurrentUser().getUid();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add New Post");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        uploadbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = description.getText().toString();

                if(!TextUtils.isEmpty(desc) ) {

                    progressBar.setVisibility(View.VISIBLE);



                            String post = description.getText().toString().trim();
                            Map <String,Object> usermap = new HashMap<>();
                            usermap.put("desc" , post);
                            usermap.put("user_id", current_user_id);
                            usermap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("Posts").add(usermap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()) {

                                        Toast.makeText(NewpostActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                        Intent mainIntent = new Intent(NewpostActivity.this, MainActivity.class);
                                        startActivity(mainIntent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(NewpostActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            progressBar.setVisibility(View.VISIBLE);

                }

            }
        });
    }

}

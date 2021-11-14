package com.example.govote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.govote.Adapter.AdminAllCandidateAdapter;
import com.example.govote.Adapter.AdminAllElectionAdapter;
import com.example.govote.Adapter.CandidateAdapter;
import com.example.govote.Model.Candidate;
import com.example.govote.Model.Election;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class AddCandidateActivity extends AppCompatActivity {
    private EditText candidateName,candidateDescription;
    private AppCompatButton choosePictureBtn,submitBtn;
    private EditText pictureName;
    private RecyclerView candidateListRv;
    private DatabaseReference candidateRef;
    public static final int PICK_IMAGE_REQUEST=1;
    private Uri mImageUri;
    private Spinner spinner;
    String[] items;
    private String electionCat;
    private StorageReference storageReference;
    private DatabaseReference databaseReference,dbRef;
    private StorageTask storageTask;
    private List<Candidate> candiateList=new ArrayList<>();
    private ArrayList<String> dropdownItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);
        candidateName=(EditText) findViewById(R.id.electionName);
        choosePictureBtn=(AppCompatButton) findViewById(R.id.chooseBannerBtn);
        pictureName=(EditText) findViewById(R.id.pictureName);
        candidateDescription=(EditText) findViewById(R.id.candidateDescription);
        submitBtn=(AppCompatButton) findViewById(R.id.submitBtn);
        spinner=(Spinner) findViewById(R.id.spinner);
        candidateListRv=findViewById(R.id.candidateListRv);
        dropdownItems=new ArrayList<>();
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("Candidate");
        dbRef=FirebaseDatabase.getInstance().getReference("Election");
        candidateRef=FirebaseDatabase.getInstance().getReference("Candidate");
        candidateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Candidate candidate = snapshot1.getValue(Candidate.class);
                        candiateList.add(candidate);
                    }
                    AdminAllCandidateAdapter adminAllCandidateAdapter = new AdminAllCandidateAdapter( AddCandidateActivity.this,candiateList);
                    candidateListRv.setLayoutManager(new LinearLayoutManager(AddCandidateActivity.this));
                    candidateListRv.setAdapter(adminAllCandidateAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Election election=dataSnapshot.getValue(Election.class);
                    dropdownItems.add(election.getName());
                    items = new String[]{election.getName()};
                    Log.d("dropdown_items", election.getName());
                }
               // ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(AddCandidateActivity.this, android.R.layout.simple_spinner_dropdown_item,dropdownItems);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddCandidateActivity.this, android.R.layout.simple_spinner_dropdown_item, dropdownItems);
//set the spinners adapter to the previously created one.
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.v("item", (String) adapterView.getItemAtPosition(position));
                electionCat=(String) adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        choosePictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=candidateName.getText().toString();
                if(storageTask!=null&&storageTask.isInProgress()){
                    Toast.makeText(AddCandidateActivity.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
                }else {
                    uploadFile();
                }
            }
        });
    }

    private void openFileChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==AddElectionFragment.PICK_IMAGE_REQUEST && resultCode==RESULT_OK
                &&data!=null && data.getData()!=null){
            mImageUri=data.getData();
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri!=null){
            StorageReference fileReference=storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(mImageUri));
            storageTask=fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(AddCandidateActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Candidate candidate=new Candidate(candidateName.getText().toString().trim()
                                    ,candidateDescription.getText().toString(),uri.toString(),pictureName.getText().toString(),electionCat);
                            String uploadId=databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(candidate);
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddCandidateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(AddCandidateActivity.this, "Please select file first!", Toast.LENGTH_SHORT).show();
        }
    }

}
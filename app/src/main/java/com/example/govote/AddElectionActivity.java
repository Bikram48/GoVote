package com.example.govote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.govote.Model.Election;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddElectionActivity extends AppCompatActivity {
    private EditText electionName;
    private AppCompatButton chooseBannerBtn,submitBtn,dateTimePickerBtn;
    private EditText pictureName;
    public static final int PICK_IMAGE_REQUEST=1;
    private Uri mImageUri;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private String startDate,endDate;
    Date date_minimal,date_maximal;
    Calendar  date = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_election);
        electionName=(EditText) findViewById(R.id.electionName);
        chooseBannerBtn=(AppCompatButton) findViewById(R.id.chooseBannerBtn);
        pictureName=(EditText) findViewById(R.id.pictureName);
        submitBtn=(AppCompatButton) findViewById(R.id.submitBtn);
        dateTimePickerBtn=(AppCompatButton) findViewById(R.id.startDateTimePickerBtn);
        storageReference= FirebaseStorage.getInstance().getReference("uploads");
        databaseReference= FirebaseDatabase.getInstance().getReference("Election");
        chooseBannerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=electionName.getText().toString();
                if(storageTask!=null&&storageTask.isInProgress()){
                    Toast.makeText(AddElectionActivity.this, "Upload is in progress", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddElectionActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Election election=new Election(electionName.getText().toString().trim()
                                    ,uri.toString(),pictureName.getText().toString(),startDate,endDate);
                            String uploadId=databaseReference.push().getKey();
                            databaseReference.child(uploadId).setValue(election).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    /*
                                    AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                                    Intent alarmShowIntent = new Intent(AddElectionActivity.this, BroadcastManager.class);

                                    alarmShowIntent.putExtra("TodoTitle",todoTaskInput);
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddElectionActivity.this,0,alarmShowIntent,0);
                                    Calendar c1 = Calendar.getInstance();
                                    long currentTime = c1.getTimeInMillis();
                                    alarmManager.set(AlarmManager.RTC_WAKEUP,c1.getTimeInMillis()-currentTime,pendingIntent);
                                    Toast.makeText(getApplicationContext(),"ToDo Item created successfully",Toast.LENGTH_LONG).show();

                                     */
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddElectionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(AddElectionActivity.this, "Please select file first!", Toast.LENGTH_SHORT).show();
        }
    }


    public void showMaxDateTimePicker(View view) {
        final Calendar currentDate = Calendar.getInstance();

        new DatePickerDialog(AddElectionActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);

                new TimePickerDialog(AddElectionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:MM:SS");
                        //FirebaseDatabase.getInstance().getReference().child("date").
                        endDate=simpleDateFormat.format(date.getTime());
                        date_maximal=date.getTime();
                        Log.d("DateTime", "onDateSet: "+endDate);
                        dateTimePickerBtn.setText(endDate);


                        Log.v("datetime", "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

    public void showMinDateTimePicker(View view){
        final Calendar currentDate = Calendar.getInstance();
        new DatePickerDialog(AddElectionActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy HH:MM:SS");
                //FirebaseDatabase.getInstance().getReference().child("date").
                startDate=simpleDateFormat.format(date.getTime());
                date_minimal=date.getTime();
                //Log.d("DateTime", "onDateSet: "+selectedDate);
                dateTimePickerBtn.setText(startDate);
                new TimePickerDialog(AddElectionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);
                        // Log.v(TAG, "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
}
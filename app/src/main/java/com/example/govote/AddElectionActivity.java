package com.example.govote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.govote.Adapter.AdminAllElectionAdapter;
import com.example.govote.Model.Election;
import com.example.govote.Service.MyReceiver;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddElectionActivity extends AppCompatActivity {
    private EditText electionName;
    private AppCompatButton chooseBannerBtn,submitBtn;
    private Button endDateTimePickerBtn,startDateTimePickerBtn;
    private EditText pictureName;
    private  String uploadId;
    public static final int PICK_IMAGE_REQUEST=1;
    private Uri mImageUri;
    private Spinner spinner;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private StorageTask storageTask;
    private String electionType;
    private String startDate,endDate;
    private RecyclerView electionListRV;
    private List<Election> electionList=new ArrayList<>();
    private ArrayList<String> dropdownItems=new ArrayList<>();
    private DatabaseReference electionRef;
    AdminAllElectionAdapter adminAllElectionAdapter;
    Date date_minimal,date_maximal;
    Election election;
    Calendar  date = Calendar.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_election);
        electionName=(EditText) findViewById(R.id.electionName);
        chooseBannerBtn=(AppCompatButton) findViewById(R.id.chooseBannerBtn);
        pictureName=(EditText) findViewById(R.id.pictureName);
        submitBtn=(AppCompatButton) findViewById(R.id.submitBtn);
        spinner=(Spinner) findViewById(R.id.spinner);
        endDateTimePickerBtn=findViewById(R.id.endDateTimePickerBtn);
        startDateTimePickerBtn=findViewById(R.id.button);
        electionListRV=findViewById(R.id.electionListRV);
        electionRef=FirebaseDatabase.getInstance().getReference("Election");

        electionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        election = snapshot1.getValue(Election.class);
                        electionList.add(election);
                    }
                    adminAllElectionAdapter= new AdminAllElectionAdapter(electionList, AddElectionActivity.this);
                    electionListRV.setLayoutManager(new LinearLayoutManager(AddElectionActivity.this));
                    electionListRV.setAdapter(adminAllElectionAdapter);
                    adminAllElectionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dropdownItems.add("SGA Elections");
        dropdownItems.add("Other Elections");
        // ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(AddCandidateActivity.this, android.R.layout.simple_spinner_dropdown_item,dropdownItems);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddElectionActivity.this, android.R.layout.simple_spinner_dropdown_item, dropdownItems);
//set the spinners adapter to the previously created one.
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).toString().equals("SGA Elections")){
                    electionType="sga";
                }
                if(adapterView.getItemAtPosition(i).toString().equals("Other Elections")){
                    electionType="other";
                }
               // electionType=(String) adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
                                    ,uri.toString(),pictureName.getText().toString(),endDate,"N",electionType);
                            uploadId=databaseReference.push().getKey();
                            setAlaram(electionName.getText().toString().trim()+" has ended.",endDate);
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
    public void showMinDateTimePicker(View view) {
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
                        startDate=simpleDateFormat.format(date.getTime());
                        date_maximal=date.getTime();
                        Log.d("DateTime", "onDateSet: "+startDate);
                        startDateTimePickerBtn.setText(startDate);


                        Log.v("datetime", "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }
    public void showMaxDateTimePicker(View view){
        final Calendar currentDate = Calendar.getInstance();
        new DatePickerDialog(AddElectionActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                date.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-yyyy hh:mm");
                //FirebaseDatabase.getInstance().getReference().child("date").


                //Log.d("DateTime", "onDateSet: "+selectedDate);

                new TimePickerDialog(AddElectionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        date.set(Calendar.MINUTE, minute);

                        Log.v("alarm_manager", "The choosen one " + date.getTime());
                        endDate=simpleDateFormat.format(date.getTime());
                        endDateTimePickerBtn.setText(endDate);
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();

            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();

    }
    /*
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
                        endDateTimePickerBtn.setText(endDate);


                        Log.v("datetime", "The choosen one " + date.getTime());
                    }
                }, currentDate.get(Calendar.HOUR_OF_DAY), currentDate.get(Calendar.MINUTE), false).show();
            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DATE)).show();
    }

     */


    public void setAlaram(String message, String date) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("message", message);
        intent.putExtra("uploadId",uploadId);
        Log.d("AdminDashboard", "setAlaram: "+uploadId);
        // intent.putExtra("time", time);
        //intent.putExtra("data", date);
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getBroadcast
                    (this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        }
        else
        {
            pendingIntent = PendingIntent.getActivity
                    (this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        //String dateandtime = date + " " + time;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        try {
            Date date1 = formatter.parse(date);
            alarmManager.set(AlarmManager.RTC_WAKEUP, date1.getTime(), pendingIntent);
            Log.d("alaram_manager", "setAlaram: "+date1.getMinutes());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
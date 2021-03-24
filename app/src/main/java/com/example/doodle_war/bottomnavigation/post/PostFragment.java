package com.example.doodle_war.bottomnavigation.post;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.doodle_war.R;
import com.example.doodle_war.base;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class PostFragment extends Fragment {

    private ImageButton imgbutton;
    private Button postbutton;
    private EditText postdes;
    private static final int gallery_pick=1;
    private Uri imageuri;
    private String des;

    private String savecurrentdate, savecurrenttime, postrandomname, downloadurl, current_user_id;

    private ProgressDialog progressbar;

    private StorageReference postimgref;
    private DatabaseReference userref,postref;
    private FirebaseAuth mAuth;


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==gallery_pick && resultCode==RESULT_OK && data!=null )
        {
            imageuri=data.getData();
            imgbutton.setImageURI(imageuri);
        }

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        imgbutton= (ImageButton) view.findViewById(R.id.imgbutton);
        postbutton= (Button) view.findViewById(R.id.postbutton);
        postdes=(EditText) view.findViewById(R.id.postdes);
        progressbar=new ProgressDialog(getContext());
         //gallery open
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,gallery_pick);
            }
        });

        mAuth=FirebaseAuth.getInstance();
        current_user_id=mAuth.getCurrentUser().getUid();

        postimgref= FirebaseStorage.getInstance().getReference();
        userref= FirebaseDatabase.getInstance().getReference().child("Users");
        postref=FirebaseDatabase.getInstance().getReference().child("Posts");

        postbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                des=postdes.getText().toString();
                if(imageuri==null)
                {
                    Toast.makeText(getContext(), "No img", Toast.LENGTH_SHORT).show();
                }
                else if(postdes==null)
                {
                    Toast.makeText(getContext(), "no des", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressbar.setTitle("Posting");
                    progressbar.setMessage(" Wait For Few Seconnds ");
                    progressbar.setCanceledOnTouchOutside(true);
                    progressbar.show();

                    storingimgtofirebasestorage();
                }
            }
        });
        return view;
    }

    private void storingimgtofirebasestorage()
    {

        Calendar callfordate= Calendar.getInstance();
        SimpleDateFormat currentdate=new SimpleDateFormat("dd-MMMM-yyyy");
        savecurrentdate=currentdate.format(callfordate.getTime());

        Calendar callfortime= Calendar.getInstance();
        SimpleDateFormat currenttime=new SimpleDateFormat("HH:mm");
        savecurrenttime=currenttime.format(callfordate.getTime());

        postrandomname = savecurrentdate + savecurrenttime;

        final StorageReference filepath=postimgref.child("Post Images").child(imageuri.getLastPathSegment() + postrandomname + ".jpg");

        filepath.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {
                    downloadurl=task.getResult().getMetadata().getReference().getDownloadUrl().toString();
                    Toast.makeText(getContext(), "img Uploaded", Toast.LENGTH_SHORT).show();
                    savingpostinfotodatabase();
                }
                else
                {
                    String msg=task.getException().getMessage();
                    Toast.makeText(getContext(), "Error Occured " + msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savingpostinfotodatabase()
    {
        userref.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String userfullname=snapshot.child("fullname").getValue().toString();
                    String userprofileimg=snapshot.child("profileimage").getValue().toString();
                    HashMap postmap=new HashMap();
                    postmap.put("uid",current_user_id);
                    postmap.put("date",savecurrentdate);
                    postmap.put("time",savecurrenttime);
                    postmap.put("description",des);
                    postmap.put("postimage",downloadurl);
                    postmap.put("profileimage",userprofileimg);
                    postmap.put("fullname",userfullname);
                    postref.child(current_user_id + postrandomname).updateChildren(postmap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                          if(task.isSuccessful())
                          {
                              Intent in =new Intent(getContext(), base.class);
                              startActivity(in);
                              getActivity().finish();
                              Toast.makeText(getContext(), "Posted", Toast.LENGTH_SHORT).show();
                              progressbar.dismiss();
                          }
                          else
                          {

                              Toast.makeText(getContext(), "Error occured", Toast.LENGTH_SHORT).show();
                              progressbar.dismiss();
                          }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
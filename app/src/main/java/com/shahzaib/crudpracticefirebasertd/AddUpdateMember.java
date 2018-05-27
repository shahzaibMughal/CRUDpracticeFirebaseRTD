package com.shahzaib.crudpracticefirebasertd;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddUpdateMember extends AppCompatActivity {

    public static final String INTENT_KEY_IS_UPDATE = "IS_UPDATE";
    public static final String INTENT_KEY_MEMBER_ID = "MEMBER_ID";
    public static final String LOG_TAG = "123456";

    EditText memberName, memberAge, memberHeight;
    RadioGroup radioGroup;
    RadioButton maleRadioBtn, femaleRadioBtn;
    private boolean isMale = true;
    boolean isUpdate = false;
    String memberID;

    DatabaseReference databaseReferenceToMember;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_member);
        radioGroup = findViewById(R.id.genderRadioGroup);
        memberName = findViewById(R.id.memberNameET);
        memberAge = findViewById(R.id.memberAgeET);
        memberHeight = findViewById(R.id.memberHeightET);
        maleRadioBtn = findViewById(R.id.maleRadio);
        femaleRadioBtn = findViewById(R.id.femaleRadio);
        isUpdate = getIntent().getBooleanExtra(INTENT_KEY_IS_UPDATE,false);

        if(isUpdate)
        {
            // getting & initializing the data
            ((Button)findViewById(R.id.addNewMemberBtn)).setText("Update");
            memberID = getIntent().getStringExtra(INTENT_KEY_MEMBER_ID);

        }



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId)
                {
                    case R.id.maleRadio:
                        isMale = true;
                        break;

                    case  R.id.femaleRadio:
                        isMale = false;
                        break;
                }

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();
        if(isUpdate)
        {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReferenceToMember = firebaseDatabase.getReference().child("GroupMember").child(memberID);
            databaseReferenceToMember.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String name = dataSnapshot.child("Name").getValue(String.class);
                    Long age = dataSnapshot.child("Age").getValue(Long.class);
                    Double height = dataSnapshot.child("Height").getValue(Double.class);
                    Boolean isMale = dataSnapshot.child("isMale").getValue(Boolean.class);

                    // binding data if data is not null
                    if(name !=null && age !=null && height !=null && isMale!=null)
                    {
                        memberName.setText(name);
                        memberAge.setText(String.valueOf(age));
                        memberHeight.setText(String.valueOf(height));
                        if(isMale)
                        {
                            maleRadioBtn.setChecked(true);
                            femaleRadioBtn.setChecked(false);
                        }
                        else
                        {
                            maleRadioBtn.setChecked(false);
                            femaleRadioBtn.setChecked(true);
                        }
                        Toast.makeText(AddUpdateMember.this, "Member Data Read Successfully", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.i("123456","Error while loading the data: "+databaseError.toException().toString());
                }
            });
        }
    }


    public void addNewMember(View view) {
        String name;
        long age;
        double height;


        if(!memberName.getText().toString().isEmpty() && !memberAge.getText().toString().isEmpty() && !memberHeight.getText().toString().isEmpty())
        {
           name = memberName.getText().toString();
           age = Long.parseLong(memberAge.getText().toString());
           height = Double.parseDouble(memberHeight.getText().toString());
            Member member = new Member(name,age,height,isMale);

           if(!isUpdate)
           {

               // practice CRUD  on String, double, long, boolean;
               FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
               DatabaseReference databaseReference = firebaseDatabase.getReference().child("GroupMember");
               String ID = databaseReference.push().getKey();
               Log.i(LOG_TAG,"push id: "+ID);
               databaseReference.child(ID).child("Name").setValue(member.getName()); //String
               databaseReference.child(ID).child("Age").setValue(member.getAge()); //long
               databaseReference.child(ID).child("Height").setValue(member.getHeight()); // double
               databaseReference.child(ID).child("isMale").setValue(member.isMale()).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) { 
                       Log.i(LOG_TAG,"Member Added Successfully: "+task.isSuccessful());
                       if(task.isSuccessful())
                       {
                           Toast.makeText(AddUpdateMember.this, "Member Added Successfully", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Toast.makeText(AddUpdateMember.this, "Failed to add Member", Toast.LENGTH_SHORT).show();
                       }

                       
                   }
               });


//               Log.i("123456",member.toString());
//               Log.i(LOG_TAG,"Member Added Successfully");
           }
           else
           {
               databaseReferenceToMember.child("Name").setValue(member.getName());
               databaseReferenceToMember.child("Age").setValue(member.getAge());
               databaseReferenceToMember.child("Height").setValue(member.getHeight());
               databaseReferenceToMember.child("isMale").setValue(member.isMale()).addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       if(task.isSuccessful())
                       {
                           Toast.makeText(AddUpdateMember.this, "Member Updated Successfully", Toast.LENGTH_SHORT).show();
                       }
                       else
                       {
                           Toast.makeText(AddUpdateMember.this, "Failed to Update Member", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
               Log.i(LOG_TAG,"Member Updated Successfully");
           }
           finish();
        }
        else
        {
            Toast.makeText(this, "Enter Missing Data", Toast.LENGTH_SHORT).show();
        }

    }

}

package com.shahzaib.crudpracticefirebasertd;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.List;

public class ListAdapter extends BaseAdapter {
    List<Member> members;
    Context context;

    public ListAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        if(members != null)
            return members.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view;

        if(convertView != null) {view = convertView; }
        else{view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_single_item_layout,parent,false); }



        //********** initializing
        TextView memberName = view.findViewById(R.id.memberName);
        ImageView memberGenderIcon = view.findViewById(R.id.memberGenderIcon);





        //******** binding data
        memberName.setText(members.get(position).getName());
        if(members.get(position).isMale()){
            memberGenderIcon.setImageResource(R.drawable.ic_male);
        }
        else{
            memberGenderIcon.setImageResource(R.drawable.ic_female);
        }



//        ********* Click listeners
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(position);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClick(position);
                return true;
            }
        });



        return view;
    }




    //*********** Helper functions
    public void setData(List<Member> members)
    {
        this.members = members;
    }
    private void onItemClick(int position)
    {
        Intent intent = new Intent(context,AddUpdateMember.class);
        intent.putExtra(AddUpdateMember.INTENT_KEY_IS_UPDATE,true);
        intent.putExtra(AddUpdateMember.INTENT_KEY_MEMBER_ID,members.get(position).getMemberID());

        context.startActivity(intent);
        Log.i("123456","Item Clicked");
    }

    private void onItemLongClick(int position) {
        FirebaseDatabase firebaseDatabase  = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("GroupMember").child(members.get(position).getMemberID());
        databaseReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(context, "Member Deleted Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(context, "Failed to delete member", Toast.LENGTH_SHORT).show();
                }
            }
        });
        members.remove(position);
        notifyDataSetChanged();
    }


}

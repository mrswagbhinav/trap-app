package com.mrswagbhinav.trapapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private  static  final String TAG = "RecyclerViewAdapter";
    private ArrayList<Trap> trapsList;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Context mContext;

    public RecyclerViewAdapter(ArrayList<Trap> trapsList, FirebaseUser user, FirebaseFirestore db, Context mContext) {
        this.trapsList = trapsList;
        this.user = user;
        this.db = db;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_trapitem, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.getId().equals(trapsList.get(position).getHost())) {
                                    viewHolder.textViewHost.setText((String) document.get("name"));
                                    if(document.getId().equals(user.getUid())) {
                                        viewHolder.imageViewStatus.setImageResource(R.drawable.ic_send_black_24dp);
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        db.collection("traps")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                if(document.getId().equals(trapsList.get(position).getId())) {  //if its this trap
                                    if (((ArrayList) document.get("invites")).contains(user.getUid())) {
                                        viewHolder.imageViewStatus.setImageResource(R.drawable.ic_check_box_outline_blank_black_24dp);
                                    }
                                    if (((ArrayList) document.get("commits")).contains(user.getUid())) {    //if commit then green pic
                                        viewHolder.imageViewStatus.setImageResource(R.drawable.ic_check_box_black_24dp);
                                    } else if (((ArrayList) document.get("declines")).contains(user.getUid())) {    //if decline then red pic
                                        viewHolder.imageViewStatus.setImageResource(R.drawable.ic_indeterminate_check_box_black_24dp);
                                    }
                                }
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        viewHolder.textViewTitle.setText(trapsList.get(position).getTitle());
        viewHolder.textViewLocationName.setText(trapsList.get(position).getLocationName());
        Date date = trapsList.get(position).getTimestamp().toDate();
        viewHolder.textViewTime.setText(getDate(date));

//        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, trapsList.get(position).getLocationAddress(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return trapsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        TextView textViewHost;
        TextView textViewLocationName;
        TextView textViewTime;
        ImageView imageViewStatus;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.id_textViewDialogTitle);
            textViewHost = itemView.findViewById(R.id.id_textViewDialogHost);
            textViewLocationName = itemView.findViewById(R.id.id_textViewDialogLocation);
            textViewTime = itemView.findViewById(R.id.id_textViewDialogTime);
            imageViewStatus = itemView.findViewById(R.id.id_imageViewStatus);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    public String getDate(Date date) {
        String month = null;
        int day = date.getDate();
        int year = date.getYear() + 1900;
        DateFormat format = new SimpleDateFormat("hh:mm a");
        String time = format.format(date);

        switch (date.getMonth()) {
            case 0:
                month = "January";
                break;
            case 1:
                month = "February";
                break;
            case 2:
                month = "March";
                break;
            case 3:
                month = "April";
                break;
            case 4:
                month = "May";
                break;
            case 5:
                month = "June";
                break;
            case 6:
                month = "July";
                break;
            case 7:
                month = "August";
                break;
            case 8:
                month = "September";
                break;
            case 9:
                month = "October";
                break;
            case 10:
                month = "November";
                break;
            case 11:
                month = "December";
                break;
        }

        return month+" "+day+", "+year+" at "+time;
    }

}

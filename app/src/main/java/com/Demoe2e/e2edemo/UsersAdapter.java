package com.Demoe2e.e2edemo;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Demoe2e.e2edemo.Models.Users;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends FirebaseRecyclerAdapter<Users, UsersAdapter.ViewHolder> {

    public UsersAdapter(@NonNull FirebaseRecyclerOptions<Users> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Users model) {
        Log.d("condition", model.getUserId() + " " + FirebaseAuth.getInstance().getUid().equals(model.getUserId()));
        holder.userName.setText(model.getUserName());
        holder.lastMessage.setText(model.getLasMessage());
        Picasso.get().load(model.getProfilePic()).placeholder(R.drawable.img_1).into(holder.profilePic);
        holder.itemView.setOnClickListener(view -> {
//            Log.d("context", "onBindViewHolder: " + holder.itemView.getContext());
            Intent intent = new Intent(holder.itemView.getContext(), ChatDetailActivity.class);
            intent.putExtra("username", model.getUserName());
            intent.putExtra("uid",model.getUserId());
            intent.putExtra("profilePic", model.getProfilePic());
            holder.itemView.getContext().startActivity(intent);

        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profilePic;
        TextView userName, lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.userName);
            lastMessage = itemView.findViewById(R.id.lastMessage);

        }
    }
}

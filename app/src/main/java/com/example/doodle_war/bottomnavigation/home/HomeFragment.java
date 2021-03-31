package com.example.doodle_war.bottomnavigation.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodle_war.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {

    RecyclerView post_list;
    private DatabaseReference Postsref;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        post_list =(RecyclerView) view.findViewById(R.id.all_users_post_list);
        Postsref= FirebaseDatabase.getInstance().getReference().child("Posts");
        post_list.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        post_list.setLayoutManager(linearLayoutManager);
        displayalluserspost();
        return view;
    }

    private void displayalluserspost()
    {

        FirebaseRecyclerOptions<Posts> options= new FirebaseRecyclerOptions.Builder<Posts>().
                setQuery(Postsref,Posts.class)
                .build();

        FirebaseRecyclerAdapter<Posts, PostsViewHolder> adopter = new FirebaseRecyclerAdapter<Posts, PostsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {

                holder.fullname.setText(model.getFullname());
                holder.time.setText(model.getTime());
                holder.date.setText(model.getDate());
                holder.description.setText(model.getDescription());
                Picasso.get().load(model.getProfileimage()).into(holder.profileimage);
                Picasso.get().load(model.getPostimage()).into(holder.postimage);

            }

            @NonNull
            @Override
            public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.feed,parent,false);
                PostsViewHolder viewHolder=new PostsViewHolder(view);
                return viewHolder;
            }
        };
        post_list.setAdapter(adopter);
        adopter.startListening();
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder
    {
        CircularImageView profileimage;
        ImageView postimage;
        TextView date, description, fullname, time;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileimage=itemView.findViewById(R.id.post_profile_img);
            postimage=itemView.findViewById(R.id.post_img);
            date=itemView.findViewById(R.id.date);
            time=itemView.findViewById(R.id.time);
            description=itemView.findViewById(R.id.post_description);
            fullname=itemView.findViewById(R.id.post_user_name);
        }
    }


}
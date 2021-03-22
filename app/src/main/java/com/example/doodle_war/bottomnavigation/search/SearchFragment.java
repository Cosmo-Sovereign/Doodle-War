package com.example.doodle_war.bottomnavigation.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doodle_war.FindFriends;
import com.example.doodle_war.PersonProfileActivity;
import com.example.doodle_war.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class SearchFragment extends Fragment {

    private SearchViewModel notificationsViewModel;
    private ImageButton SearchButton;
    private EditText SearchInputText;
    private RecyclerView searchResultList;
    private DatabaseReference allUsersDatabaseRef;
    String searchBoxInput;
    FirebaseRecyclerAdapter<FindFriends,FindFriendViewHolder> firebaseRecyclerAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        searchResultList = root.findViewById(R.id.searchResultList);
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(getContext()));

        SearchButton = root.findViewById(R.id.searchPeopleFriendsButton);
        SearchInputText = root.findViewById(R.id.searchBoxInput);
        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchBoxInput = SearchInputText.getText().toString();
                Toast.makeText(getContext(),"Searching.... ",Toast.LENGTH_SHORT).show();

                SearchPeopleAndFriend(searchBoxInput);
            }
        });

        return root;
    }

    private void SearchPeopleAndFriend(String searchBoxInput) {
        Query searchPeopleAndFriendsQuery = allUsersDatabaseRef.orderByChild("fullname")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");




        FirebaseRecyclerOptions<FindFriends> options = new FirebaseRecyclerOptions.Builder<FindFriends>().setQuery(searchPeopleAndFriendsQuery,FindFriends.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FindFriends, FindFriendViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindFriendViewHolder holder, int position, @NonNull FindFriends model)
            {

                holder.userName.setText(model.getFullname());
                holder.userBio.setText(model.getBio());
                Picasso.get().load(model.getProfileimage()).placeholder(R.drawable.man).into(holder.profileImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String visit_user_id = getRef(position).getKey();
                        Intent profileIntent = new Intent(getContext(), PersonProfileActivity.class);
                        profileIntent.putExtra("visit_user_id",visit_user_id);
                        startActivity(profileIntent);
                    }
                });

            }

            @NonNull
            @Override
            public FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_display_layout,parent,false);
                FindFriendViewHolder viewHolder = new FindFriendViewHolder(view);
                return viewHolder;
            }
        };

        searchResultList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }


    public static class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView userName,userBio;
        CircularImageView profileImage;

        public FindFriendViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.allUsersProfileName);
            userBio = itemView.findViewById(R.id.allUsersBio);
            profileImage = itemView.findViewById(R.id.allUsersProfileImage);
        }
    }
}
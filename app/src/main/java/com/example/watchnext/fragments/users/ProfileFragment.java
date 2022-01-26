package com.example.watchnext.fragments.users;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.watchnext.R;
import com.example.watchnext.common.interfaces.OnItemClickListener;
import com.example.watchnext.models.entities.Review;
import com.google.android.material.button.MaterialButton;

public class ProfileFragment extends Fragment {

    private RecyclerView profileReviewList;
    private SwipeRefreshLayout swipeRefresh;
    private ProfileReviewListAdapter profileReviewListAdapter;
    private MaterialButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeMembers(view);
        setListeners();
        swipeRefresh.setOnRefreshListener(() -> {}); // TODO: Modal.instance.refreshProfileReviews
        profileReviewList.setHasFixedSize(true);
        profileReviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        profileReviewListAdapter = new ProfileReviewListAdapter();
        profileReviewList.setAdapter(profileReviewListAdapter);
        profileReviewListAdapter.setOnItemClickListener((v, position) -> {
            Navigation.findNavController(v).navigate(ProfileFragmentDirections.actionProfileFragmentToReviewDetailsFragment());
        });
        return view;
    }

    private void initializeMembers(View view) {
        backButton = view.findViewById(R.id.profile_fragment_back_arrow_button);
        profileReviewList = view.findViewById(R.id.profile_fragment_review_list_rv);
        swipeRefresh = view.findViewById(R.id.profile_fragment_my_posts_swiperefresh);
    }

    private void setListeners() {
        setBackButtonOnClickListener();
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    static class ProfileReviewListViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewImageView;
        TextView reviewTitle;

        public ProfileReviewListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            reviewImageView = itemView.findViewById(R.id.profile_review_list_row_card_imageview);
            reviewTitle = itemView.findViewById(R.id.profile_review_list_row_card_title_textview);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }

        void bind(Review review) {
            reviewImageView.setImageResource(R.drawable.placeholder_review_image);
            reviewTitle.setText(review.getTitle());
        }
    }

    class ProfileReviewListAdapter extends RecyclerView.Adapter<ProfileReviewListViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public ProfileReviewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.profile_review_list_row,parent,false);
            return new ProfileReviewListViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull ProfileReviewListViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

    }
}
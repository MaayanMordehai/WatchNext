package com.example.watchnext.fragments.users;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.watchnext.IntroActivity;
import com.example.watchnext.R;
import com.example.watchnext.common.interfaces.OnItemClickListener;
import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.Review;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FeedFragment extends Fragment {

    RecyclerView reviewList;
    SwipeRefreshLayout swipeRefresh;
    ReviewListAdapter reviewListAdapter;
    FloatingActionButton addReviewActionButton;
    ImageFilterView logoutImageFilterView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        initializeMembers(view);
        setListeners();
        swipeRefresh.setOnRefreshListener(() -> {}); // TODO: Modal.instance.refreshStudentList
        reviewList.setHasFixedSize(true);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewListAdapter = new ReviewListAdapter();
        reviewList.setAdapter(reviewListAdapter);
        reviewListAdapter.setOnItemClickListener((v, position) -> {
            Log.d("TAG", "Clicked Row in position: " + position);
        });

        return view;
    }

    private void initializeMembers(View view) {
        addReviewActionButton = view.findViewById(R.id.feed_fragment_add_review_button);
        swipeRefresh = view.findViewById(R.id.feed_fragment_swiperefresh);
        reviewList = view.findViewById(R.id.feed_fragment_review_list_rv);
        logoutImageFilterView = view.findViewById(R.id.feed_fragment_logout_image_view);
    }

    private void setListeners() {
        setOnAddReviewActionButtonClickListener();
        setOnLogoutButtonClickListener();
    }

    private void setOnLogoutButtonClickListener() {
        logoutImageFilterView.setOnClickListener(view -> {
            Model.instance.logout(this::startIntroActivity);
        });
    }

    private void startIntroActivity() {
        if (getActivity() != null) {
            Intent introActivityIntent = new Intent(getActivity(), IntroActivity.class);
            startActivity(introActivityIntent);
            getActivity().finish();
        }
    }

    private void setOnAddReviewActionButtonClickListener() {
        addReviewActionButton.setOnClickListener(
                Navigation.createNavigateOnClickListener(FeedFragmentDirections.actionFeedFragmentToAddReviewFragment())
        );
    }

    static class ReviewListViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewImageView;
        TextView reviewTitle;
        TextView reviewDescription;
        ImageView reviewOwnerImageView;
        TextView reviewOwnerFullName;

        public ReviewListViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            reviewImageView = itemView.findViewById(R.id.review_list_row_card_imageview);
            reviewTitle = itemView.findViewById(R.id.review_list_row_card_title_textview);
            reviewDescription = itemView.findViewById(R.id.review_list_row_card_description_textview);
            reviewOwnerImageView = itemView.findViewById(R.id.review_list_row_owner_imageview);
            reviewOwnerFullName = itemView.findViewById(R.id.review_list_row_card_owner_textview);
            itemView.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                listener.onItemClick(v, pos);
            });
        }

        void bind(Review review) {
            reviewImageView.setImageResource(R.drawable.placeholder_review_image);
            reviewTitle.setText(review.getTitle());
            reviewDescription.setText(review.getDescription());
            reviewOwnerImageView.setImageResource(R.drawable.blank_profile_picture);
            reviewOwnerFullName.setText("Ran Biderman Placeholder");
        }
    }

    class ReviewListAdapter extends RecyclerView.Adapter<ReviewListViewHolder> {

        OnItemClickListener listener;

        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public ReviewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.review_list_row,parent,false);
            return new ReviewListViewHolder(view, listener);
        }

        @Override
        public void onBindViewHolder(@NonNull ReviewListViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 10;
        }

    }

}
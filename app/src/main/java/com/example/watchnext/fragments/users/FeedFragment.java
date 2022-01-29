package com.example.watchnext.fragments.users;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.watchnext.IntroActivity;
import com.example.watchnext.R;
import com.example.watchnext.common.interfaces.OnItemClickListener;
import com.example.watchnext.enums.LoadingStateEnum;
import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.Review;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.viewmodel.ReviewWithOwnerListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Objects;

public class FeedFragment extends Fragment {

    private RecyclerView reviewList;
    private SwipeRefreshLayout swipeRefresh;
    private ReviewListAdapter reviewListAdapter;
    private FloatingActionButton addReviewActionButton;
    private ImageFilterView logoutImageFilterView;
    private ShapeableImageView profileImageView;
    private ReviewWithOwnerListViewModel reviewWithOwnerListViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        reviewWithOwnerListViewModel = new ViewModelProvider(this).get(ReviewWithOwnerListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        initializeMembers(view);
        setListeners();

        swipeRefresh.setOnRefreshListener(Model.instance::refreshReviewWithOwnerList);
        reviewList.setHasFixedSize(true);
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        reviewListAdapter = new ReviewListAdapter();
        reviewList.setAdapter(reviewListAdapter);
        reviewListAdapter.setOnItemClickListener((v, position) -> {
            String reviewId = Objects.requireNonNull(reviewWithOwnerListViewModel.getData().getValue()).get(position).review.getId();
            // TODO: get review id to next
            Navigation.findNavController(v).navigate(FeedFragmentDirections.actionFeedFragmentToReviewDetailsFragment());
        });
        reviewWithOwnerListViewModel.getData().observe(getViewLifecycleOwner(), reviewWithOwnerList -> refresh());
        swipeRefresh.setRefreshing((Model.instance.getReviewWithOwnerListLoadingState().getValue() == LoadingStateEnum.loading));
        Model.instance.getReviewWithOwnerListLoadingState().observe(getViewLifecycleOwner(), reviewWithOwnerListLoadingState -> {
            if (Model.instance.getReviewWithOwnerListLoadingState().getValue() == LoadingStateEnum.loading) {
                swipeRefresh.setRefreshing(true);
            } else {
                swipeRefresh.setRefreshing(false);
            }
        });
        return view;
    }

    private void refresh() {
        reviewListAdapter.notifyDataSetChanged();
    }

    private void initializeMembers(View view) {
        addReviewActionButton = view.findViewById(R.id.feed_fragment_add_review_button);
        swipeRefresh = view.findViewById(R.id.feed_fragment_swiperefresh);
        reviewList = view.findViewById(R.id.feed_fragment_review_list_rv);
        logoutImageFilterView = view.findViewById(R.id.feed_fragment_logout_image_view);
        profileImageView = view.findViewById(R.id.feed_fragment_profile_image_view);
    }

    private void setListeners() {
        setOnAddReviewActionButtonClickListener();
        setOnLogoutButtonClickListener();
        setOnProfileImageClickListener();
    }

    private void setOnProfileImageClickListener() {
        profileImageView.setOnClickListener(Navigation.createNavigateOnClickListener(FeedFragmentDirections.actionFeedFragmentToProfileFragment()));
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

        void bind(Review review, User u) {
            reviewImageView.setImageResource(R.drawable.placeholder_review_image);
            reviewTitle.setText(review.getTitle());
            reviewDescription.setText(review.getDescription());
            reviewOwnerImageView.setImageResource(R.drawable.blank_profile_picture);
            reviewOwnerFullName.setText(String.format("%s %s", u.getFirstName(), u.getLastName()));
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
            Review r = reviewWithOwnerListViewModel.getData().getValue().get(position).review;
            User u = reviewWithOwnerListViewModel.getData().getValue().get(position).user;
            holder.bind(r, u);
        }

        @Override
        public int getItemCount() {
            if (reviewWithOwnerListViewModel.getData().getValue() == null) {
                return 0;
            }
            return reviewWithOwnerListViewModel.getData().getValue().size();
        }

    }

}
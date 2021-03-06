package com.example.watchnext.fragments.users;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.watchnext.R;
import com.example.watchnext.common.interfaces.OnItemClickListener;
import com.example.watchnext.enums.LoadingStateEnum;
import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.Review;
import com.example.watchnext.viewmodel.ReviewWithOwnerSharedViewModel;
import com.example.watchnext.viewmodel.UserWithReviewListViewModel;
import com.example.watchnext.viewmodel.factory.UserWithReviewListByUserIdViewModelFactory;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    private String userIdFromBundle;
    private TextView userName;
    private TextView email;
    private ShapeableImageView userProfileImage;
    private RecyclerView profileReviewList;
    private SwipeRefreshLayout swipeRefresh;
    private ProfileReviewListAdapter profileReviewListAdapter;
    private MaterialButton backButton;
    private MaterialButton editProfileButton;
    private UserWithReviewListViewModel userWithReviewListViewModel;
    private ReviewWithOwnerSharedViewModel reviewWithOwnerSharedViewModel;
    private NavController navController;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        userIdFromBundle = ProfileFragmentArgs.fromBundle(getArguments()).getUserId();
        userWithReviewListViewModel = new ViewModelProvider(this, new UserWithReviewListByUserIdViewModelFactory(userIdFromBundle)).get(UserWithReviewListViewModel.class);
        reviewWithOwnerSharedViewModel = new ViewModelProvider(requireActivity()).get(ReviewWithOwnerSharedViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        Model.instance.refreshReviewWithOwnerListByUserId(userIdFromBundle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initializeMembers(view);
        setListeners();
        handleRefreshingState();
        observeReviewListByUserId();
        observeUser();
        return view;
    }

    private void initializeMembers(View view) {
        userName = view.findViewById(R.id.profile_fragment_username_text_view);
        email = view.findViewById(R.id.profile_fragment_email_text_view);
        userProfileImage = view.findViewById(R.id.profile_fragment_profile_image_view);
        backButton = view.findViewById(R.id.profile_fragment_back_arrow_button);
        profileReviewList = view.findViewById(R.id.profile_fragment_review_list_rv);
        swipeRefresh = view.findViewById(R.id.profile_fragment_my_posts_swiperefresh);
        editProfileButton = view.findViewById(R.id.profile_fragment_edit_profile_button);
        navController = NavHostFragment.findNavController(this);
        initializeRecycleView();
    }

    private void setListeners() {
        setBackButtonOnClickListener();
        setEditProfileButtonOnClickListener();
        setOnRefreshListener();
        setOnAdapterItemClickListener();
    }

    private void setOnAdapterItemClickListener() {
        profileReviewListAdapter.setOnItemClickListener((v, position) -> {
            reviewWithOwnerSharedViewModel.select(Objects.requireNonNull(userWithReviewListViewModel.getReviewList().getValue()).get(position));
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToReviewDetailsFragment());
        });
    }

    private void setOnRefreshListener() {
        swipeRefresh.setOnRefreshListener(() -> {
            Model.instance.refreshReviewWithOwnerListByUserId(userIdFromBundle);
        });
    }

    private void setEditProfileButtonOnClickListener() {
        editProfileButton.setOnClickListener((v) -> {
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(userIdFromBundle));
        });
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            navController.navigateUp();
        });
    }

    private void handleRefreshingState() {
        swipeRefresh.setRefreshing((Model.instance.getReviewWithOwnerListByUserIdLoadingState().getValue() == LoadingStateEnum.loading));
        Model.instance.getReviewWithOwnerListByUserIdLoadingState().observe(getViewLifecycleOwner(), reviewListByUserIdLoadingState -> {
            swipeRefresh.setRefreshing(Model.instance.getReviewWithOwnerListByUserIdLoadingState().getValue() == LoadingStateEnum.loading);
        });
    }

    private void refresh() {
        profileReviewListAdapter.notifyDataSetChanged();
    }

    private void observeReviewListByUserId() {
        userWithReviewListViewModel.getReviewList().observe(getViewLifecycleOwner(), reviewListByUserId -> refresh());
    }

    private void observeUser() {
        userProfileImage.setImageResource(R.drawable.blank_profile_picture);
        userWithReviewListViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                userName.setText(String.format("%s %s", user.getFirstName(), user.getLastName()));
                email.setText(user.getEmail());
                if (user.getImageUrl() != null) {
                    Picasso.get()
                            .load(user.getImageUrl())
                            .into(userProfileImage);
                }
                showAdminPanelIfOwner(userIdFromBundle);
            }
        });
    }

    private void showAdminPanelIfOwner(String ownerId) {
        if (Model.instance.getCurrentUserId().equals(ownerId)) {
            editProfileButton.setVisibility(View.VISIBLE);
        }
    }

    private void initializeRecycleView() {
        profileReviewList.setHasFixedSize(true);
        profileReviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        profileReviewListAdapter = new ProfileReviewListAdapter();
        profileReviewList.setAdapter(profileReviewListAdapter);
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
            if (review.getImageUrl() != null) {
                Picasso.get()
                        .load(review.getImageUrl())
                        .into(reviewImageView);
            }
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
            if (userWithReviewListViewModel.getReviewList().getValue() != null){
                Review review = userWithReviewListViewModel.getReviewList().getValue().get(position).review;
                holder.bind(review);
            }
        }

        @Override
        public int getItemCount() {
            if (userWithReviewListViewModel.getReviewList().getValue() == null) {
                return 0;
            }
            return userWithReviewListViewModel.getReviewList().getValue().size();
        }

    }
}
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

import com.example.watchnext.R;
import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.relations.ReviewWithOwner;
import com.example.watchnext.viewmodel.ReviewWithOwnerSharedViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.squareup.picasso.Picasso;

public class ReviewDetailsFragment extends Fragment {

    private MaterialButton backButton;
    private ReviewWithOwnerSharedViewModel reviewWithOwnerSharedViewModel;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView ownerTextView;
    private ImageView reviewImageView;
    private ImageView ownerImageView;
    private MaterialButton editButton;
    private MaterialButton deleteButton;
    private NavController navController;
    private ReviewWithOwner currentReviewWithOwner;
    private CircularProgressIndicator progressIndicator;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        reviewWithOwnerSharedViewModel = new ViewModelProvider(requireActivity()).get(ReviewWithOwnerSharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_details, container, false);
        initializeMembers(view);
        setListeners();
        observeSelectedReviewWithOwner();
        return view;
    }

    private void initializeMembers(View view) {
        titleTextView = view.findViewById(R.id.review_details_fragment_title_textview);
        descriptionTextView = view.findViewById(R.id.review_details_fragment_description_textview);
        ownerTextView = view.findViewById(R.id.review_details_fragment_owner_textview);
        backButton = view.findViewById(R.id.review_details_fragment_back_arrow_button);
        reviewImageView = view.findViewById(R.id.review_details_fragment_review_image_view);
        ownerImageView = view.findViewById(R.id.review_details_fragment_owner_imageview);
        editButton = view.findViewById(R.id.review_details_fragment_edit_button);
        deleteButton = view.findViewById(R.id.review_details_fragment_delete_button);
        progressIndicator = view.findViewById(R.id.review_details_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);
    }

    private void setListeners() {
        setBackButtonOnClickListener();
        setDeleteButtonOnClickListener();
        setEditButtonOnClickListener();
    }

    private void observeSelectedReviewWithOwner() {
        reviewWithOwnerSharedViewModel.getSelected().observe(getViewLifecycleOwner(), reviewWithOwner -> {
            if (reviewWithOwner != null) {
                currentReviewWithOwner = reviewWithOwner;
                showAdminPanelIfOwner(reviewWithOwner.user.getId());
                titleTextView.setText(reviewWithOwner.review.getTitle());
                descriptionTextView.setText(reviewWithOwner.review.getDescription());
                ownerTextView.setText(String.format("%s %s", reviewWithOwner.user.getFirstName(), reviewWithOwner.user.getLastName()));
                reviewImageView.setImageResource(R.drawable.placeholder_review_image);
                ownerImageView.setImageResource(R.drawable.blank_profile_picture);
                if (reviewWithOwner.review.getImageUrl() != null) {
                    Picasso.get()
                            .load(reviewWithOwner.review.getImageUrl())
                            .into(reviewImageView);
                }
                if (reviewWithOwner.user.getImageUrl() != null) {
                    Picasso.get()
                            .load(reviewWithOwner.user.getImageUrl())
                            .into(ownerImageView);
                }
            }
        });
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            navController.navigateUp();
        });
    }

    private void setDeleteButtonOnClickListener() {
        deleteButton.setOnClickListener(view -> {
            progressIndicator.show();
            editButton.setEnabled(false);
            deleteButton.setEnabled(false);
            Model.instance.deleteReview(currentReviewWithOwner.review, () -> {
                navController.navigateUp();
            });
        });
    }

    private void setEditButtonOnClickListener() {
        editButton.setOnClickListener(view -> {
            navController.navigate(ReviewDetailsFragmentDirections.actionReviewDetailsFragmentToAddReviewFragment(true));
        });
    }

    private void showAdminPanelIfOwner(String ownerId) {
        if (Model.instance.getCurrentUserId().equals(ownerId)) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }
}
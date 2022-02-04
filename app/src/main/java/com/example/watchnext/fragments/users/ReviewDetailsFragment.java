package com.example.watchnext.fragments.users;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.watchnext.R;
import com.example.watchnext.models.Model;
import com.example.watchnext.viewmodel.ReviewWithOwnerSharedViewModel;
import com.google.android.material.button.MaterialButton;
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
    }

    private void setListeners() {
        setBackButtonOnClickListener();
    }

    private void observeSelectedReviewWithOwner() {
        reviewWithOwnerSharedViewModel.getSelected().observe(getViewLifecycleOwner(), reviewWithOwner -> {
            showAdminPanelIfOwner(reviewWithOwner.user.getId());
            titleTextView.setText(reviewWithOwner.review.getTitle());
            descriptionTextView.setText(reviewWithOwner.review.getDescription());
            ownerTextView.setText(String.format("%s %s", reviewWithOwner.user.getFirstName(), reviewWithOwner.user.getLastName()));
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
        });
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigateUp();
        });
    }

    private void showAdminPanelIfOwner(String ownerId) {
        if (Model.instance.getCurrentUserId().equals(ownerId)) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
        }
    }
}
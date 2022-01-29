package com.example.watchnext.fragments.users;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.watchnext.R;
import com.example.watchnext.viewmodel.ReviewWithOwnerSharedViewModel;
import com.google.android.material.button.MaterialButton;

public class ReviewDetailsFragment extends Fragment {

    private MaterialButton backButton;
    private ReviewWithOwnerSharedViewModel reviewWithOwnerSharedViewModel;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView ownerTextView;

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

        reviewWithOwnerSharedViewModel.getSelected().observe(getViewLifecycleOwner(), reviewWithOwner -> {
            titleTextView.setText(reviewWithOwner.review.getTitle());
            descriptionTextView.setText(reviewWithOwner.review.getDescription());
            ownerTextView.setText(String.format("%s %s", reviewWithOwner.user.getFirstName(), reviewWithOwner.user.getLastName()));
        });

        return view;
    }

    private void initializeMembers(View view) {
        titleTextView = view.findViewById(R.id.review_details_fragment_title_textview);
        descriptionTextView = view.findViewById(R.id.review_details_fragment_description_textview);
        ownerTextView = view.findViewById(R.id.review_details_fragment_owner_textview);
        backButton = view.findViewById(R.id.review_details_fragment_back_arrow_button);
    }

    private void setListeners() {
        setBackButtonOnClickListener();
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigateUp();
        });
    }
}
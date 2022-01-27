package com.example.watchnext.fragments.users;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchnext.R;
import com.google.android.material.button.MaterialButton;

public class ReviewDetailsFragment extends Fragment {

    private MaterialButton backButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_details, container, false);
        initializeMembers(view);
        setListeners();
        return view;
    }

    private void initializeMembers(View view) {
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
package com.example.watchnext.fragments.users;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.watchnext.R;
import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.Review;
import com.example.watchnext.utils.CameraUtilFragment;
import com.example.watchnext.utils.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddReviewFragment extends CameraUtilFragment {

    private ImageView reviewImageView;
    private TextInputLayout titleTextInput;
    private TextInputEditText titleEditText;
    private TextInputLayout descriptionTextInput;
    private TextInputEditText descriptionEditText;
    private MaterialButton backButton;
    private MaterialButton postButton;
    private CircularProgressIndicator progressIndicator;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
        initializeMembers(view);
        setListeners();
        return view;
    }

    private void initializeMembers(View view) {
        reviewImageView = view.findViewById(R.id.add_review_fragment_card_imageview);
        titleTextInput = view.findViewById(R.id.add_review_fragment_title_text_input);
        titleEditText = view.findViewById(R.id.add_review_fragment_title_edit_text);
        descriptionTextInput = view.findViewById(R.id.add_review_fragment_description_text_input);
        descriptionEditText = view.findViewById(R.id.add_review_fragment_description_edit_text);
        backButton = view.findViewById(R.id.add_review_fragment_back_arrow_button);
        postButton = view.findViewById(R.id.add_review_fragment_post_button);
        progressIndicator = view.findViewById(R.id.add_review_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);
    }

    private void setListeners() {
        setPostButtonOnClickListener();
        setBackButtonOnClickListener();
        setTitleEditTextOnKeyListener();
        setDescriptionEditTextOnKeyListener();
        setReviewImageViewOnClickListener();
    }

    private void setPostButtonOnClickListener() {
        postButton.setOnClickListener(view -> {
            setErrorIfTitleIsInvalid();
            setErrorIfDescriptionIsInvalid();
            if(isFormValid()) {
                post(view);
            }
        });
    }

    private void post(View view) {
        postButton.setEnabled(false);
        progressIndicator.show();
        Review r = new Review(titleEditText.getText().toString(), descriptionEditText.getText().toString());
        r.setOwnerId(Model.instance.getCurrentUserId());
        Bitmap reviewImage = ((BitmapDrawable)reviewImageView.getDrawable()).getBitmap();
        if (reviewImage == null) {
            Model.instance.addReview(() -> {
                navController.navigateUp();
            }, r);
        } else {
            // TODO: think of better name than title
            Model.instance.uploadReviewImage(reviewImage, r.getTitle() + ".jpg", (url) -> {
                r.setImageUrl(url);
                Model.instance.addReview(() -> {
                    navController.navigateUp();
                }, r);
            });
        }
    }

    private boolean isFormValid() {
        return (InputValidator.isReviewTitleValid(titleEditText.getText()) &&
                InputValidator.isReviewDescriptionValid(descriptionEditText.getText()));
    }

    private void setReviewImageViewOnClickListener() {
        reviewImageView.setOnClickListener(this::showCameraMenu);
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            navController.navigateUp();
        });
    }

    private void setTitleEditTextOnKeyListener() {
        titleEditText.setOnKeyListener((view, i, keyEvent) -> {
            setErrorIfTitleIsInvalid();
            return false;
        });
    }

    private void setDescriptionEditTextOnKeyListener() {
        descriptionEditText.setOnKeyListener((view, i, keyEvent) -> {
            setErrorIfDescriptionIsInvalid();
            return false;
        });
    }

    private void setErrorIfTitleIsInvalid() {
        if (!InputValidator.isReviewTitleValid(titleEditText.getText())) {
            titleTextInput.setError(getString(R.string.required_field));
        } else {
            titleTextInput.setError(null);
        }
    }

    private void setErrorIfDescriptionIsInvalid() {
        if (!InputValidator.isReviewDescriptionValid(descriptionEditText.getText())) {
            descriptionTextInput.setError(getString(R.string.required_field));
        } else {
            descriptionTextInput.setError(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            reviewImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                reviewImageView.setImageURI(selectedImageUri);
            }
        }
    }
}
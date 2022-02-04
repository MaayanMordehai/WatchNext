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

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.watchnext.R;
import com.example.watchnext.models.Model;
import com.example.watchnext.models.entities.User;
import com.example.watchnext.utils.CameraUtilFragment;
import com.example.watchnext.utils.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

public class EditProfileFragment extends CameraUtilFragment {

    private TextInputLayout firstNameTextInput;
    private TextInputEditText firstNameEditText;
    private TextInputLayout lastNameTextInput;
    private TextInputEditText lastNameEditText;
    private MaterialButton saveButton;
    private MaterialButton backButton;
    private ShapeableImageView profileImageView;
    private User currentUser;
    private CircularProgressIndicator progressIndicator;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initializeMembers(view);
        setListeners();
        observeUser();
        return view;
    }

    private void initializeMembers(View view) {
        firstNameTextInput = view.findViewById(R.id.edit_profile_fragment_first_name_text_input);
        firstNameEditText = view.findViewById(R.id.edit_profile_fragment_first_name_edit_text);
        lastNameTextInput = view.findViewById(R.id.edit_profile_fragment_last_name_text_input);
        lastNameEditText = view.findViewById(R.id.edit_profile_fragment_last_name_edit_text);
        saveButton = view.findViewById(R.id.edit_profile_fragment_save_button);
        backButton = view.findViewById(R.id.edit_profile_fragment_back_arrow_button);
        profileImageView = view.findViewById(R.id.edit_profile_fragment_profile_image_view);
        progressIndicator = view.findViewById(R.id.edit_profile_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);
    }

    private void setListeners() {
        setSaveButtonOnClickListener();
        setBackButtonOnClickListener();
        setFirstNameEditTextOnKeyListener();
        setLastNameEditTextOnKeyListener();
        setProfileImageViewOnClickListener();
    }

    private void observeUser() {
        String userIdFromBundle = EditProfileFragmentArgs.fromBundle(getArguments()).getUserId();
        Model.instance.getUserById(userIdFromBundle).observe(getViewLifecycleOwner(), user -> {
            currentUser = user;
            initializeInputsFromUserData();
        });
    }

    private void initializeInputsFromUserData() {
        firstNameEditText.setText(currentUser.getFirstName());
        lastNameEditText.setText(currentUser.getLastName());
        profileImageView.setImageResource(R.drawable.blank_profile_picture);
        if (currentUser.getImageUrl() != null) {
            Picasso.get()
                    .load(currentUser.getImageUrl())
                    .into(profileImageView);
        }
    }

    private void setSaveButtonOnClickListener() {
        saveButton.setOnClickListener(view -> {
            setErrorIfFirstNameIsInvalid();
            setErrorIfLastNameIsInvalid();
            if(isFormValid()) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        saveButton.setEnabled(false);
        progressIndicator.show();
        currentUser.setFirstName(firstNameEditText.getText().toString());
        currentUser.setLastName(lastNameEditText.getText().toString());
        Bitmap profileImage = ((BitmapDrawable)profileImageView.getDrawable()).getBitmap();
        if (profileImage == null) {
            Model.instance.updateUser(() -> {
                navController.navigateUp();
            }, currentUser);
        } else {
            Model.instance.uploadUserImage(profileImage, currentUser.getEmail() + ".jpg", (url) -> {
                currentUser.setImageUrl(url);
                Model.instance.updateUser(() -> {
                    navController.navigateUp();
                }, currentUser);
            });
        }
    }

    private boolean isFormValid() {
        return (InputValidator.isFirstNameValid(firstNameEditText.getText()) &&
                InputValidator.isLastNameValid(lastNameEditText.getText()));
    }

    private void setProfileImageViewOnClickListener() {
        profileImageView.setOnClickListener(this::showCameraMenu);
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            navController.navigateUp();
        });
    }

    private void setFirstNameEditTextOnKeyListener() {
        firstNameEditText.setOnKeyListener((view, i, keyEvent) -> {
            setErrorIfFirstNameIsInvalid();
            return false;
        });
    }

    private void setLastNameEditTextOnKeyListener() {
        lastNameEditText.setOnKeyListener((view, i, keyEvent) -> {
            setErrorIfLastNameIsInvalid();
            return false;
        });
    }

    private void setErrorIfFirstNameIsInvalid() {
        if (!InputValidator.isFirstNameValid(firstNameEditText.getText())) {
            firstNameTextInput.setError(getString(R.string.required_field));
        } else {
            firstNameTextInput.setError(null);
        }
    }

    private void setErrorIfLastNameIsInvalid() {
        if (!InputValidator.isLastNameValid(lastNameEditText.getText())) {
            lastNameTextInput.setError(getString(R.string.required_field));
        } else {
            lastNameTextInput.setError(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImageView.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                profileImageView.setImageURI(selectedImageUri);
            }
        }
    }
}
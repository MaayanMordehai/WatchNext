package com.example.watchnext.fragments.users;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;

import com.example.watchnext.R;
import com.example.watchnext.fragments.guests.RegisterFragmentDirections;
import com.example.watchnext.utils.CameraUtilFragment;
import com.example.watchnext.utils.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class EditProfileFragment extends CameraUtilFragment {

    private TextInputLayout firstNameTextInput;
    private TextInputEditText firstNameEditText;
    private TextInputLayout lastNameTextInput;
    private TextInputEditText lastNameEditText;
    private MaterialButton saveButton;
    private MaterialButton backButton;
    private ShapeableImageView profileImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initializeMembers(view);
        setListeners();
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
    }

    private void setListeners() {
        setSaveButtonOnClickListener();
        setBackButtonOnClickListener();
        setFirstNameEditTextOnKeyListener();
        setLastNameEditTextOnKeyListener();
        setProfileImageViewOnClickListener();
    }

    private void setSaveButtonOnClickListener() {
        saveButton.setOnClickListener(view -> {
            setErrorIfFirstNameIsInvalid();
            setErrorIfLastNameIsInvalid();
            if(isFormValid()) {
                saveProfile(view);
            }
        });
    }

    private void saveProfile(View view) {
        // TODO
        Navigation.findNavController(view).navigateUp();
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
            Navigation.findNavController(view).navigateUp();
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
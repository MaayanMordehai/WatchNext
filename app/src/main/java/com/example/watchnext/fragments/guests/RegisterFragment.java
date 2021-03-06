package com.example.watchnext.fragments.guests;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends CameraUtilFragment {

    private TextInputLayout firstNameTextInput;
    private TextInputEditText firstNameEditText;
    private TextInputLayout lastNameTextInput;
    private TextInputEditText lastNameEditText;
    private TextInputLayout emailTextInput;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordEditText;
    private TextInputLayout confirmPasswordTextInput;
    private TextInputEditText confirmPasswordEditText;
    private MaterialButton registerButton;
    private MaterialButton backButton;
    private ShapeableImageView profileImageView;
    private CircularProgressIndicator progressIndicator;
    private NavController navController;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        initializeMembers(view);
        setListeners();
        return view;
    }

    private void initializeMembers(View view) {
        firstNameTextInput = view.findViewById(R.id.register_fragment_first_name_text_input);
        firstNameEditText = view.findViewById(R.id.register_fragment_first_name_edit_text);
        lastNameTextInput = view.findViewById(R.id.register_fragment_last_name_text_input);
        lastNameEditText = view.findViewById(R.id.register_fragment_last_name_edit_text);
        emailTextInput = view.findViewById(R.id.register_fragment_email_text_input);
        emailEditText = view.findViewById(R.id.register_fragment_email_edit_text);
        passwordTextInput = view.findViewById(R.id.register_fragment_password_text_input);
        passwordEditText = view.findViewById(R.id.register_fragment_password_edit_text);
        confirmPasswordTextInput = view.findViewById(R.id.register_fragment_confirm_password_text_input);
        confirmPasswordEditText = view.findViewById(R.id.register_fragment_confirm_password_edit_text);
        registerButton = view.findViewById(R.id.register_fragment_register_button);
        backButton = view.findViewById(R.id.register_fragment_back_arrow_button);
        profileImageView = view.findViewById(R.id.register_fragment_profile_image_view);
        progressIndicator = view.findViewById(R.id.register_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);
    }

    private void setListeners() {
        setRegisterButtonOnClickListener();
        setBackButtonOnClickListener();
        setFirstNameEditTextOnKeyListener();
        setLastNameEditTextOnKeyListener();
        setEmailEditTextOnKeyListener();
        setPasswordEditTextOnKeyListener();
        setConfirmPasswordEditTextOnKeyListener();
        setProfileImageViewOnClickListener();
    }

    private void setRegisterButtonOnClickListener() {
        registerButton.setOnClickListener(view -> {
            setErrorIfFirstNameIsInvalid();
            setErrorIfLastNameIsInvalid();
            setErrorIfEmailIsInvalid();
            setErrorIfPasswordIsInvalid();
            setErrorIfConfirmPasswordIsInvalid();
            if(isFormValid()) {
                registerIfValid(view);
            }
        });
    }

    private void registerIfValid(View view) {
        Model.instance.isEmailExists(emailEditText.getText().toString(),
            (emailExist) -> {
                if (emailExist) {
                    emailTextInput.setError("This Email is already registered");
                } else {
                    this.register(view);
                }
            },
            (errorMessage) -> {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
            });
    }

    private void register(View view) {
        registerButton.setEnabled(false);
        progressIndicator.show();
        User u = new User(firstNameEditText.getText().toString(),
                lastNameEditText.getText().toString(),
                emailEditText.getText().toString());
        Bitmap profileImage = ((BitmapDrawable)profileImageView.getDrawable()).getBitmap();
        if (profileImage == null) {
            Model.instance.register(this::navigateToFeedAfterRegister, u, passwordEditText.getText().toString());
        } else {
            Model.instance.uploadUserImage(profileImage, u.getEmail() + ".jpg", (url) -> {
                u.setImageUrl(url);
                Model.instance.register(this::navigateToFeedAfterRegister, u, passwordEditText.getText().toString());
            });
        }
    }

    private void navigateToFeedAfterRegister() {
        navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToUsersNavGraph());
    }

    private boolean isFormValid() {
        return (InputValidator.isFirstNameValid(firstNameEditText.getText()) &&
                InputValidator.isLastNameValid(lastNameEditText.getText()) &&
                InputValidator.isEmailValid(emailEditText.getText()) &&
                InputValidator.isPasswordValid(passwordEditText.getText()) &&
                InputValidator.isPasswordMatches(passwordEditText.getText(), confirmPasswordEditText.getText()));
    }

    private void setProfileImageViewOnClickListener() {
        profileImageView.setOnClickListener(this::showCameraMenu);
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            navController.navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
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

    private void setEmailEditTextOnKeyListener() {
        emailEditText.setOnKeyListener((view, i, keyEvent) -> {
            setErrorIfEmailIsInvalid();
            return false;
        });
    }


    private void setPasswordEditTextOnKeyListener() {
        passwordEditText.setOnKeyListener((view, i, keyEvent) -> {
            setErrorIfPasswordIsInvalid();
            return false;
        });
    }

    private void setConfirmPasswordEditTextOnKeyListener() {
        confirmPasswordEditText.setOnKeyListener((view, i, keyEvent) -> {
            setErrorIfConfirmPasswordIsInvalid();
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

    private void setErrorIfEmailIsInvalid() {
        if (!InputValidator.isEmailValid(emailEditText.getText())) {
            emailTextInput.setError(getString(R.string.email_invalid));
        } else {
            emailTextInput.setError(null);
        }
    }

    private void setErrorIfPasswordIsInvalid() {
        if (!InputValidator.isPasswordValid(passwordEditText.getText())) {
            passwordTextInput.setError(getString(R.string.password_invalid));
        } else {
            passwordTextInput.setError(null);
        }
    }

    private void setErrorIfConfirmPasswordIsInvalid() {
        if (!InputValidator.isPasswordMatches(passwordEditText.getText(), confirmPasswordEditText.getText())) {
            confirmPasswordTextInput.setError(getString(R.string.password_not_match));
        } else {
            confirmPasswordTextInput.setError(null);
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
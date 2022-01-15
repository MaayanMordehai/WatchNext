package com.example.watchnext.fragments.guests;

import static android.app.Activity.RESULT_OK;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.watchnext.R;
import com.example.watchnext.utils.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_OPEN_GALLERY = 2;

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
                Navigation.findNavController(view).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
            }
        });
    }

    private boolean isFormValid() {
        return (InputValidator.isFirstNameValid(firstNameEditText.getText()) &&
                InputValidator.isLastNameValid(lastNameEditText.getText()) &&
                InputValidator.isEmailValid(emailEditText.getText()) &&
                InputValidator.isPasswordValid(passwordEditText.getText()) &&
                InputValidator.isPasswordMatches(passwordEditText.getText(), confirmPasswordEditText.getText()));
    }

    private void setProfileImageViewOnClickListener() {
        profileImageView.setOnClickListener(view -> {
            showCameraMenu(view);
        });
    }

    private void setBackButtonOnClickListener() {
        backButton.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment());
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

    public void showCameraMenu(View view) {
        if (this.getContext() != null) {
            PopupMenu cameraPopupMenu = new PopupMenu(this.getContext(), view);
            MenuInflater inflater = cameraPopupMenu.getMenuInflater();
            inflater.inflate(R.menu.camera_menu, cameraPopupMenu.getMenu());
            cameraPopupMenu.setOnMenuItemClickListener(this::setOnMenuItemClickListener);
            cameraPopupMenu.show();
        }
    }

    private boolean setOnMenuItemClickListener(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.camera_menu_open_camera) {
            openCamera();
            return true;
        } else if (menuItem.getItemId() == R.id.camera_menu_open_gallery) {
            openGallery();
            return true;
        }
        return false;
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openGallery() {
        Intent openGalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(openGalleryIntent, "Select Picture"),REQUEST_OPEN_GALLERY);
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
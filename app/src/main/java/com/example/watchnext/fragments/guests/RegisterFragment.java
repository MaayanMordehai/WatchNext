package com.example.watchnext.fragments.guests;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.watchnext.R;
import com.example.watchnext.validations.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterFragment extends Fragment {

    private final InputValidator inputValidator = new InputValidator();

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
    }

    private void setListeners() {
        setRegisterButtonOnClickListener();
        setBackButtonOnClickListener();
        setFirstNameEditTextOnKeyListener();
        setLastNameEditTextOnKeyListener();
        setEmailEditTextOnKeyListener();
        setPasswordEditTextOnKeyListener();
        setConfirmPasswordEditTextOnKeyListener();
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
        return (inputValidator.isFirstNameValid(firstNameEditText.getText()) &&
                inputValidator.isLastNameValid(lastNameEditText.getText()) &&
                inputValidator.isEmailValid(emailEditText.getText()) &&
                inputValidator.isPasswordValid(passwordEditText.getText()) &&
                inputValidator.isPasswordMatches(passwordEditText.getText(), confirmPasswordEditText.getText()));
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
        if (!inputValidator.isFirstNameValid(firstNameEditText.getText())) {
            firstNameTextInput.setError(getString(R.string.required_field));
        } else {
            firstNameTextInput.setError(null);
        }
    }

    private void setErrorIfLastNameIsInvalid() {
        if (!inputValidator.isLastNameValid(lastNameEditText.getText())) {
            lastNameTextInput.setError(getString(R.string.required_field));
        } else {
            lastNameTextInput.setError(null);
        }
    }

    private void setErrorIfEmailIsInvalid() {
        if (!inputValidator.isEmailValid(emailEditText.getText())) {
            emailTextInput.setError(getString(R.string.email_invalid));
        } else {
            emailTextInput.setError(null);
        }
    }

    private void setErrorIfPasswordIsInvalid() {
        if (!inputValidator.isPasswordValid(passwordEditText.getText())) {
            passwordTextInput.setError(getString(R.string.password_invalid));
        } else {
            passwordTextInput.setError(null);
        }
    }

    private void setErrorIfConfirmPasswordIsInvalid() {
        if (!inputValidator.isPasswordMatches(passwordEditText.getText(), confirmPasswordEditText.getText())) {
            confirmPasswordTextInput.setError(getString(R.string.password_not_match));
        } else {
            confirmPasswordTextInput.setError(null);
        }
    }

}
package com.example.watchnext;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.watchnext.validations.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private final InputValidator inputValidator = new InputValidator();

    private TextInputLayout emailTextInput;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailTextInput = view.findViewById(R.id.login_fragment_email_text_input);
        emailEditText = view.findViewById(R.id.login_fragment_email_edit_text);
        passwordTextInput = view.findViewById(R.id.login_fragment_password_text_input);
        passwordEditText = view.findViewById(R.id.login_fragment_password_edit_text);
        loginButton = view.findViewById(R.id.fragment_login_login_button);
        setLoginButtonOnClickListener(loginButton);
        setPasswordEditTextOnKeyListener(passwordEditText, passwordTextInput);
        setEmailEditTextOnKeyListener(emailEditText, emailTextInput);
        return view;
    }

    private void setLoginButtonOnClickListener(MaterialButton loginButton) {
        loginButton.setOnClickListener(view -> {
            handlePasswordValidation();
            handleEmailValidation();
        });
    }

    private void setPasswordEditTextOnKeyListener(TextInputEditText passwordEditText, TextInputLayout passwordTextInput) {
        passwordEditText.setOnKeyListener((view, i, keyEvent) -> {
            handlePasswordValidation();
            return false;
        });
    }

    private void setEmailEditTextOnKeyListener(TextInputEditText emailEditText, TextInputLayout emailTextInput) {
        emailEditText.setOnKeyListener((view, i, keyEvent) -> {
            handleEmailValidation();
            return false; // TODO: Why return something?
        });
    }

    private void handlePasswordValidation() {
        if (!inputValidator.isPasswordValid(passwordEditText.getText())) {
            passwordTextInput.setError(getString(R.string.login_password_error));
        } else {
            passwordTextInput.setError(null);
        }
    }

    public void handleEmailValidation() {
        if (!inputValidator.isEmailValid(emailEditText.getText())) {
            emailTextInput.setError(getString(R.string.login_email_error));
        } else {
            emailTextInput.setError(null);
        }
    }

}
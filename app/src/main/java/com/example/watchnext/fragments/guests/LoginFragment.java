package com.example.watchnext.fragments.guests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.watchnext.R;
import com.example.watchnext.utils.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private TextInputLayout emailTextInput;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private MaterialButton registerButton;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initializeMembers(view);
        setListeners();
        return view;
    }

    private void initializeMembers(View view) {
        emailTextInput = view.findViewById(R.id.login_fragment_email_text_input);
        emailEditText = view.findViewById(R.id.login_fragment_email_edit_text);
        passwordTextInput = view.findViewById(R.id.login_fragment_password_text_input);
        passwordEditText = view.findViewById(R.id.login_fragment_password_edit_text);
        loginButton = view.findViewById(R.id.login_fragment_login_button);
        registerButton = view.findViewById(R.id.login_fragment_register_button);
    }

    private void setListeners() {
        setLoginButtonOnClickListener();
        setRegisterButtonOnClickListener();
        setEmailEditTextOnKeyListener();
        setPasswordEditTextOnKeyListener();
    }

    private void setLoginButtonOnClickListener() {
        loginButton.setOnClickListener(view -> {
            setErrorIfEmailIsInvalid();
            setErrorIfPasswordIsInvalid();
            if(isFormValid()) {
                // TODO: Login
            }
        });
    }

    private boolean isFormValid() {
        return (setErrorIfEmailIsInvalid() &&
                setErrorIfPasswordIsInvalid());
    }

    private void setRegisterButtonOnClickListener() {
        registerButton.setOnClickListener(
                Navigation.createNavigateOnClickListener(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        );
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

    private boolean setErrorIfEmailIsInvalid() {
        if (!InputValidator.isEmailValid(emailEditText.getText())) {
            emailTextInput.setError(getString(R.string.email_invalid));
            return false;
        } else {
            emailTextInput.setError(null);
            return true;
        }
    }

    private boolean setErrorIfPasswordIsInvalid() {
        if (!InputValidator.isPasswordValid(passwordEditText.getText())) {
            passwordTextInput.setError(getString(R.string.password_invalid));
            return false;
        } else {
            passwordTextInput.setError(null);
            return true;
        }
    }

}
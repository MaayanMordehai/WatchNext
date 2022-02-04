package com.example.watchnext.fragments.guests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.watchnext.R;
import com.example.watchnext.models.Model;
import com.example.watchnext.utils.InputValidator;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    private TextInputLayout emailTextInput;
    private TextInputEditText emailEditText;
    private TextInputLayout passwordTextInput;
    private TextInputEditText passwordEditText;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private CircularProgressIndicator progressIndicator;
    private NavController navController;

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
        progressIndicator = view.findViewById(R.id.login_fragment_progress_indicator);
        navController = NavHostFragment.findNavController(this);
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
                loginButton.setEnabled(false);
                registerButton.setEnabled(false);
                progressIndicator.show();
                Model.instance.login(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString(),
                        () -> {
                            navController.navigate(LoginFragmentDirections.actionLoginFragmentToUsersNavGraph());
                        },
                        errorMessage -> {
                            Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show();
                            loginButton.setEnabled(true);
                            registerButton.setEnabled(true);
                            progressIndicator.hide();
                        });
            }
        });
    }

    private boolean isFormValid() {
        return (setErrorIfEmailIsInvalid() &&
                setErrorIfPasswordIsInvalid());
    }

    private void setRegisterButtonOnClickListener() {
        registerButton.setOnClickListener((View view) -> {
                navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment());
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
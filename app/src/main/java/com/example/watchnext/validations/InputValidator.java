package com.example.watchnext.validations;

import android.text.Editable;
import android.util.Patterns;

import androidx.annotation.Nullable;

public class InputValidator {

    public InputValidator() {
    }

    public boolean isFirstNameValid(@Nullable Editable text) {
        return !isFieldEmpty(text);
    }

    public boolean isLastNameValid(@Nullable Editable text) {
        return !isFieldEmpty(text);
    }

    public boolean isPasswordValid(@Nullable Editable text) {
        return (text != null && text.length() >= 8);
    }

    public boolean isEmailValid(@Nullable Editable text) {
        return (text != null && Patterns.EMAIL_ADDRESS.matcher(text).matches());
    }

    private boolean isFieldEmpty(@Nullable Editable text) {
        return (text == null || text.length() == 0);
    }

    public boolean isPasswordMatches(@Nullable Editable password, @Nullable Editable confirmPassword) {
        return (password != null &&
                confirmPassword != null &&
                isPasswordValid(confirmPassword) &&
                password.toString().equals(confirmPassword.toString()));
    }

}

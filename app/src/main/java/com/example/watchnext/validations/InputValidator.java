package com.example.watchnext.validations;

import android.text.Editable;
import android.util.Patterns;

import androidx.annotation.Nullable;

public class InputValidator {

    public InputValidator() {
    }

    public boolean isPasswordValid(@Nullable Editable text) {
        return (text != null && text.length() >= 8);
    }

    public boolean isEmailValid(@Nullable Editable text) {
        return (text != null && Patterns.EMAIL_ADDRESS.matcher(text).matches());
    }

}

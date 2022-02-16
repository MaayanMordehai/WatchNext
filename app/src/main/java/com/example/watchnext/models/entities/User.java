package com.example.watchnext.models.entities;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.watchnext.ContextApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Entity
public class User {

    private static final String LAST_UPDATED = "UserLastUpdated";
    public static final String UPDATE_FIELD = "updateDate";

    @PrimaryKey
    @NonNull
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String imageUrl;
    private Long updateDate;

    @Ignore
    public User(String firstName,
                String lastName,
                String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageUrl = null;
    }

    public User(String id,
                String firstName,
                String lastName,
                String email,
                String imageUrl,
                Long updateDate) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.updateDate = updateDate;
    }

    public static User create(Map<String, Object> user, String id) {
        String firstName = Objects.requireNonNull(user.get("firstName")).toString();
        String lastName = Objects.requireNonNull(user.get("lastName")).toString();
        String email = Objects.requireNonNull(user.get("email")).toString();
        String imageUrl;
        if (user.get("imageUrl") == null) {
            imageUrl = null;
        } else {
            imageUrl = user.get("imageUrl").toString();
        }
        Timestamp ts = (Timestamp) Objects.requireNonNull(user.get("updateDate"));
        Long updateDate = ts.getSeconds();
        return new User(id, firstName, lastName, email, imageUrl, updateDate);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        result.put("imageUrl", imageUrl);
        result.put("updateDate", FieldValue.serverTimestamp());
        return result;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getUpdateDate() {
        return updateDate;
    }

    public static void setLocalLastUpdated(Long timestamp) {
        SharedPreferences.Editor ed = ContextApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).edit();
        ed.putLong(User.LAST_UPDATED, timestamp);
        ed.apply();
    }

    public static Long getLocalLastUpdated() {
        SharedPreferences sp = ContextApplication
                .getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sp.getLong(User.LAST_UPDATED, 0);
    }
}

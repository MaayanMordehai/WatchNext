package com.example.watchnext.models.users;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.watchnext.ContextApplication;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {

    public static final String COLLECTION_NAME = "users";
    public static final String LAST_UPDATED = "UserLastUpdated";
    @PrimaryKey
    @NotNull
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String imageUrl;
    private Long updateDate;

    public User(String id,
                String firstName,
                String lastName,
                String email,
                String password,
                String imageUrl) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public static User create(Map<String, Object> user) {
        String id = (String) user.get("id");
        String firstName = (String) user.get("firstName");
        String lastName = (String) user.get("lastName");
        String email = (String) user.get("email");
        String password = (String) user.get("password");
        String imageUrl = (String) user.get("imageUrl");
        Timestamp ts = (Timestamp)user.get("updateDate");
        User neUser = new User(id, firstName, lastName, email, password, imageUrl);
        if (ts != null) {
            Long updateDate = ts.getSeconds();
            neUser.setUpdateDate(updateDate);
        }
        return neUser;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("firstName", firstName);
        result.put("lastName", lastName);
        result.put("email", email);
        result.put("password", password);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setUpdateDate(Long updateDate) {
        this.updateDate = updateDate;
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

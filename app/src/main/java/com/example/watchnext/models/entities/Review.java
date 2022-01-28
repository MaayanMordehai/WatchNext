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
public class Review {

    public static final String LAST_UPDATED = "ReviewLastUpdated";

    @PrimaryKey
    @NonNull
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private Long updateDate;
    private boolean isDeleted;

    @Ignore
    public Review(String title,
                  String description) {
        this.title = title;
        this.description = description;
        this.imageUrl = ""; // TODO: SET DEFAULT
    }

    public Review(@NonNull String id,
                  String title,
                  String description,
                  String imageUrl,
                  Long updateDate,
                  boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.updateDate = updateDate;
        this.isDeleted = isDeleted;
    }

    public static Review create(Map<String, Object> json, String id) {
        String title = Objects.requireNonNull(json.get("title")).toString();
        String description = Objects.requireNonNull(json.get("description")).toString();
        String imageUrl = Objects.requireNonNull(json.get("imageUrl")).toString();
        Timestamp ts = (Timestamp) Objects.requireNonNull(json.get("updateDate"));
        Long updateDate = ts.getSeconds();
        boolean isDeleted = (boolean) Objects.requireNonNull(json.get("isDeleted"));
        return new Review(id, title, description, imageUrl, updateDate, isDeleted);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("description", description);
        result.put("imageUrl", imageUrl);
        result.put("updateDate", FieldValue.serverTimestamp());
        result.put("isDeleted", isDeleted);
        return result;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        ed.putLong(Review.LAST_UPDATED, timestamp);
        ed.apply();
    }

    public static Long getLocalLastUpdated() {
        SharedPreferences sp = ContextApplication
                .getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE);
        return sp.getLong(Review.LAST_UPDATED, 0);
    }

}

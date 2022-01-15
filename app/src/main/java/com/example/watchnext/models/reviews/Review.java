package com.example.watchnext.models.reviews;

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
public class Review {
    public static final String COLLECTION_NAME = "reviews";
    public static final String LAST_UPDATED = "ReviewLastUpdated";
    @PrimaryKey
    @NotNull
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private Long updateDate;
    private boolean isDeleted;

    public Review(@NonNull String id, String title, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.isDeleted = false;
    }

    public static Review create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String title = (String) json.get("title");
        String description = (String) json.get("description");
        String imageUrl = (String)json.get("imageUrl");
        Timestamp ts = (Timestamp)json.get("updateDate");
        Review review = new Review(id, title, description, imageUrl);
        if (ts != null) {
            Long updateDate = ts.getSeconds();
            review.setUpdateDate(updateDate);
        }
        review.setDeleted((boolean) json.get("isDeleted"));
        return review;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
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

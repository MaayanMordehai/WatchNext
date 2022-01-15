package com.example.watchnext.models.reviews;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Review {
    public static final String COLLECTION_NAME = "reviews";
    @PrimaryKey
    @NotNull
    private String id;
    private String title;
    private String description;
    private String imageUrl;

    public Review(String id, String title, String description, String imageUrl) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static Review create(Map<String, Object> json) {
        String id = (String) json.get("id");
        String title = (String) json.get("title");
        String description = (String) json.get("description");
        String imageUrl = (String)json.get("imageUrl");
        return new Review(id, title, description, imageUrl);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("title", title);
        result.put("description", description);
        result.put("imageUrl", imageUrl);
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}

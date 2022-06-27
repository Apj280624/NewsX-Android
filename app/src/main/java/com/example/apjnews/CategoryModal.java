package com.example.apjnews;

public class CategoryModal {

    private String category;
    private String categoryImageUrl;

    //constructor
    public CategoryModal(String category, String categoryImageUrl) {
        this.category = category;
        this.categoryImageUrl = categoryImageUrl;
    }

    //getters and setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCategoryImageUrl() { return categoryImageUrl; }
    public void setCategoryImageUrl(String categoryImageUrl) { this.categoryImageUrl = categoryImageUrl; }

}

package com.raphaelcunha.screenweb.model;

public enum Category {
    ACTION("Action", "Ação"),
    ROMANCE("Romance", "Romance"),
    COMEDY("Comedy", "Comédia"),
    DRAMA("Drama", "Drama"),
    CRIME("Crime", "Crime");

    private String categoryOmdb;
    private String categoryPortuguese;

    Category(String categoryOmdb, String categoryPortuguese){
        this.categoryOmdb = categoryOmdb;
        this.categoryPortuguese = categoryPortuguese;
    }

    public static Category fromString(String text) {
        for(Category category : Category.values()){
            if (category.categoryOmdb.equalsIgnoreCase(text)){
                return category;
            }
        }
        throw new IllegalArgumentException("There wasn't find any category for this serie");
    }

    public static Category fromPortuguese(String text) {
        for(Category category : Category.values()){
            if (category.categoryPortuguese.equalsIgnoreCase(text)){
                return category;
            }
        }
        throw new IllegalArgumentException("There wasn't find any category for this serie");
    }

}

package com.ismael.openstreamify.enums;

public enum MovieGenre {

    ACTION("Action"),
    ADVENTURE("Adventure"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    HORROR("Horror"),
    THRILLER("Thriller"),
    SCIENCE_FICTION("Science Fiction"),
    FANTASY("Fantasy"),
    ANIMATION("Animation"),
    DOCUMENTARY("Documentary"),
    MYSTERY("Mistery"),
    ROMANCE("Romance"),
    CRIME("Crime"),
    MUSICAL("Musical"),
    WAR("War"),
    WESTERN("Western");

    String name;

    MovieGenre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

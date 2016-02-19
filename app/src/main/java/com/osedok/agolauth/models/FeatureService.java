package com.osedok.agolauth.models;

/**
 * Created by osedok on 19/02/2016.
 * Feature Service object stores basic information about Feature Service
 */
public class FeatureService {


    private String id;
    private String type;
    private String name;
    private String title;
    private String description;
    private String url;
    private String thumbnail;


    public FeatureService(Builder b) {

        this.id = b.id;
        this.type = b.type;
        this.name = b.name;
        this.title = b.title;
        this.description = b.description;
        this.url = b.url;
        this.thumbnail = b.thumbnail;

    }


    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }


    public static class Builder {

        private String id;
        private String type;
        private String name;
        private String title;
        private String description;
        private String url;
        private String thumbnail;


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public FeatureService build() {
            return new FeatureService(this);
        }

    }

}

package com.chatmicroservices.mediaservice.config.api;

public class RestProperties {

    public static final String ROOT = "/api";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static class AUTH {
        public static final String ROOT = "/auth";
        public static final String CHECK_TOKEN = "/check-token";
    }

    public static class USER {
        public static final String ROOT = "/users";

        public static final String UPDATE_PROFILE_PICTURE = "/profile-picture";
    }

    public static class MEDIA {
        public static final String ROOT = "/media";
    }

}

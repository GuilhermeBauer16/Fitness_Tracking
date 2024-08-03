package com.github.GuilhermeBauer16.FitnessTracking.enums;


public enum UserProfile {
    USER("USER"),
    ADMIN("ADMIN");

    private final String userProfile;

    UserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public static UserProfile fromString(String userProfile) {
        for (UserProfile profile : UserProfile.values()) {
            if (profile.userProfile.equalsIgnoreCase(userProfile)) {
                return profile;
            }
        }
        throw new IllegalArgumentException("Unknown user profile: " + userProfile);
    }
}

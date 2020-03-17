package com.softserve.rms.constants;

public enum Message {

    RESOURCE_PERMISSION_SUBJECT("Resource Permission"),
    GROUP_PERMISSION_SUBJECT("Group Permission"),

    ACCESS("User %s gave you %S access to %s.\n%s"),
    DENIED("User %s denied you %S access to %s.\n%s"),
    OWNER("User %s made you owner to %s.\n%s"),

    LINK("To view %s, please follow the link http://localhost:3000/");

    private String message;

    Message(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}

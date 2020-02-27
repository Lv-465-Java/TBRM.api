package com.softserve.rms.exceptions;

public interface Message {

    String USER_EMAIL_NOT_FOUND_EXCEPTION="User with email '%s' not found";

    String USER_NOT_FOUND_EXCEPTION="User with id '%s' not found";

    String BAD_CREDENTIAL_EXCEPTION="Authentication Failed. Username or Password not valid.";

    String JWT_PARSER_EXCEPTION="Error when parse token";

    String REFRESH_TOKEN_EXCEPTION="Error when refresh token";

    String JWT_EXPIRE_TOKEN_EXCEPTION="JWT Token expired";

    String JWT_AUTHENTICATION_EXCEPTION="JWT Authentication failed. Please sign in again";

    String NON_ACTIVE_ACCOUNT_EXCEPTION="Your account is not active";
}

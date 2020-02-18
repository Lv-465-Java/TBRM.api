package com.softserve.rms.security.oauth;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OidcUserInfo {

    public Map<String, Object> attributes;
    private String id;
    private String email;
    private String name;
    private String imageUrl;

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //@Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    // @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    //@Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

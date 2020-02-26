package com.softserve.rms.security.oauth.user;

import java.util.Map;

/**
 * The following classes are used to get the required details of the user
 * from the generic map of key-value pairs
 * @author Kravets Maryana
 */
public abstract class OAuth2UserInfo {
        protected Map<String, Object> attributes;

        public OAuth2UserInfo(Map<String, Object> attributes) {
            this.attributes = attributes;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public abstract String getId();

        public abstract String getName();

        public abstract String getEmail();

        public abstract String getImageUrl();
    }

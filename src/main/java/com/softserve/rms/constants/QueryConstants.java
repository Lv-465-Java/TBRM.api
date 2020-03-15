package com.softserve.rms.constants;

public enum QueryConstants {


    DELETED_ACCOUNTS("select to_timestamp(r.revtstmp/ 1000)::timestamp ,roles.name as role,u.id, u.first_name,u.last_name, u.email, u.phone , u.image_url from users_aud u,\n" +
            "revinfo r,roles where u.revtype=2 and u.rev=r.rev and roles.id=u.role_id"),
    ALL_HISTORY (
            "select u.id,u.image_url,u.revtype,u.first_name,u.last_name,roles.name as role,u.email,u.enabled,u.phone, to_timestamp(r.revtstmp/ 1000)  from users_aud u ,revinfo r,roles where  u.rev=r.rev and roles.id=u.role_id"),
    USER_BY_ID ("select u.id, roles.name as role,u.image_url, u.revtype,to_timestamp(r.revtstmp/ 1000)::timestamp,u.first_name, u.last_name, u.email, u.phone,u.password,\n" +
            "u.reset_token from users_aud u ,revinfo r, roles where  \n" +
            "u.rev=r.rev and roles.id=u.role_id and u.id = ?"),
    FILTER_BY_DATE("select u.id, u.image_url,roles.name as role, u.revtype,u.reset_token ,to_timestamp(r.revtstmp/ 1000)::date , u.first_name,u.last_name,u.email,u.enabled,u.phone\n" +
            "from users_aud u ,revinfo r, roles where  u.rev=r.rev and to_timestamp(r.revtstmp/ 1000)::date=(?::date) and roles.id=u.role_id"),
    ;


    private String value;

    QueryConstants(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}

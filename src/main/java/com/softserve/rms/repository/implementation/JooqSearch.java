package com.softserve.rms.repository.implementation;

import com.softserve.rms.service.UserService;
import org.jooq.DSLContext;

import java.util.HashMap;
import java.util.Map;

public class JooqSearch {
    private DSLContext dslContext;
    private UserService userService;

    public JooqSearch(DSLContext dslContext, UserService userService) {
        this.dslContext = dslContext;
        this.userService = userService;
    }

    Map<String, Object> myMap = new HashMap<>();



}
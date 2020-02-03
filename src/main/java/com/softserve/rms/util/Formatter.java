package com.softserve.rms.util;

import org.springframework.stereotype.Component;

@Component
public class Formatter {

    public String sidFormatter(String name){
        String result;
        result = name.startsWith("Grant")
                ? name.substring(20, name.length() - 1) : name.substring(13, name.length() - 1);
        return result;
    }

    public String permissionFormatter(String name){
        String result;
        result = name.substring(31).equals("R") ? "READ" : "WRITE";
        return  result;
    }
}
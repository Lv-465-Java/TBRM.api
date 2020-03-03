package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.FieldConstants;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.entities.ResourceTemplate;
import com.softserve.rms.service.UserService;
import org.jooq.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.jooq.impl.DSL.*;

//@Repository
public class JooqSearching {

    private DSLContext dslContext;
    private UserService userService;
    private Map<String, Object> myMap = new HashMap<>();

    public JooqSearching(DSLContext dslContext, UserService userService) {
        this.dslContext = dslContext;
        this.userService = userService;
    }
//    String string = new String("name=room");
//    Condition condition(string);
//    myMap.put("name", "room");

    String tab = new String("room");

    public List<ResourceRecord> searchResourceTemplate(List<Condition> conditionList) {
//        Condition withBoolean = field("is_published").eq("false");
//        Condition withB = field("is_published").eq(false);

        Result<Record> searchResult = dslContext.selectFrom(tab)
                .where(conditionList).fetch();
        return convertOneRecordsToList(searchResult);
    }


//    public List<ResourceTemplate> searchResourceTemplate(Map<Field<?>, Object> myMap) {
////        String myString = "is_published = false and creator_id = 5";
//        Condition withString = condition("is_published = true and creator_id = 5");
//        Condition withField = field("name").containsIgnoreCase("Room");
//
//        List<Condition> list = new ArrayList<>();
//        list.add(withField);
//        list.add(withString);
//        System.out.println(list);
//
////        String myStr = "name != 'Room'";
////        Condition my = condition(myMap);
//        Result<Record> searchResult = dslContext.selectFrom(tab)
//                .where(list).fetch();
//
////                .where(condition())
////                .where(field("is_published").contains(false));
////                .fetch();
//        return convertOneRecordsToList(searchResult);
//    }
}
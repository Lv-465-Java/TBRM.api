package com.softserve.rms.repository.implementation;

import com.softserve.rms.constants.ErrorMessage;
import com.softserve.rms.entities.ResourceRecord;
import com.softserve.rms.exceptions.SqlGrammarException;
import com.softserve.rms.service.UserService;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.List;

//@Repository
public class JooqSearching {

    private DSLContext dslContext;
    private UserService userService;
    private ResourceRecordRepositoryImpl resourceRepository;
    private static final Logger LOG = LoggerFactory.getLogger(JooqSearching.class);

    @Autowired
    public JooqSearching(DSLContext dslContext, UserService userService, ResourceRecordRepositoryImpl resourceRepository) {
        this.dslContext = dslContext;
        this.userService = userService;
        this.resourceRepository = resourceRepository;
    }

    public List<ResourceRecord> searchResourceTemplate(List<Condition> conditionList, String tableName) {
        Result<Record> searchResult;
        try {
            searchResult = dslContext.selectFrom(tableName)
                    .where(conditionList).fetch();
        } catch (BadSqlGrammarException exception) {
            throw new SqlGrammarException(ErrorMessage.WRONG_SEARCH_CRITERIA.getMessage());
        }
        return resourceRepository.convertRecordsToResourceList((searchResult));
    }

    //    String string = new String("name=room");
//    Condition condition(string);
//    myMap.put("name", "room");

    //        Condition withBoolean = field("is_published").eq("false");
//        Condition withB = field("is_published").eq(false);

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
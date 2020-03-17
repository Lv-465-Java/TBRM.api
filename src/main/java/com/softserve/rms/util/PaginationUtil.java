package com.softserve.rms.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

public class PaginationUtil {
    private PaginationUtil() {
    }

    public static Integer validatePage(Integer page) {
        return page <= 0 ? 0 : page - 1;
    }

    public static Integer validatePageSize(Integer pageSize) {
        return pageSize <= 0 ? 1 : pageSize;
    }

    public static <T> Page<T> buildPage(List<T> items, Integer page, Integer pageSize) {
        List<T> batchOfItems;
        page = validatePage(page);
        pageSize = validatePageSize(pageSize);
        int totalItems = items.size();
        if (isIndexOutOfBound(totalItems, (page * pageSize + pageSize))) {
            if(isIndexOutOfBound(totalItems, page * pageSize)) {
                return new PageImpl<T>(Collections.emptyList(),
                        PageRequest.of(page, pageSize), totalItems);
            }
            batchOfItems = items.subList(page * pageSize, totalItems);
            return new PageImpl<T>(batchOfItems,
                    PageRequest.of(page, pageSize), totalItems);
        }
        batchOfItems = items.subList(page * pageSize, (page * pageSize + pageSize));
        return new PageImpl<T>(batchOfItems, PageRequest.of(page, pageSize), totalItems);
    }

    private static boolean isIndexOutOfBound(Integer size, Integer index) {
        return size <= index;
    }
}
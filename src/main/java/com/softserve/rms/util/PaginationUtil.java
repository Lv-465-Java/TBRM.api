package com.softserve.rms.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

public class PaginationUtil {
    private PaginationUtil() {
    }

    /**
     * Methods validate page.
     *
     * @param page current page from front
     * @return valid page
     * @author Artur Sydor
     */
    public static Integer validatePage(Integer page) {
        return page <= 0 ? 0 : page - 1;
    }

    /**
     * Methods validate pageSize.
     *
     * @param pageSize current pageSize from front
     * @return valid pageSize
     * @author Artur Sydor
     */
    public static Integer validatePageSize(Integer pageSize) {
        return pageSize <= 0 ? 1 : pageSize;
    }

    /**
     * Methods build page from list.
     *
     * @param items list of items
     * @param page current page
     * @param pageSize size of current page
     * @return content for current page
     * @author Artur Sydor
     */
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

    /**
     * Method checks if offset isn`t out of bound.
     *
     * @param size list size
     * @param index offset
     * @return true if offset is out of bound of list
     * @author Artur Sydor
     */
    private static boolean isIndexOutOfBound(Integer size, Integer index) {
        return size <= index;
    }
}
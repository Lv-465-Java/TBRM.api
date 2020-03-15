package com.softserve.rms.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserHistoryRepository {
    List<Map<String, Object>> getDeletedAccounts();
    List<Map<String, Object>> getAllAccounts();
    List<Map<String, Object>> getUserHistory(Long id);
    List<Map<String, Object>> getAllByData(String date);
}

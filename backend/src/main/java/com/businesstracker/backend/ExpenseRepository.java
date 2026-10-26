package com.businesstracker.backend;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends MongoRepository<BaseExpense, String> {

    List<BaseExpense> findAllByOrderByDateDesc();

    List<BaseExpense> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<BaseExpense> findByTitleContainingIgnoreCaseOrderByDateDesc(String keyword);

    List<BaseExpense> findByDateBetweenOrderByDateDesc(LocalDate startDate, LocalDate endDate);

    List<BaseExpense> findByCategoryIgnoreCaseAndDateBetweenOrderByDateDesc(String category, LocalDate startDate, LocalDate endDate);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mass.sica.utils.specification;

import java.util.Calendar;
import java.util.Date;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

/**
 *
 * @author Aristide MASSAGA
 * @param <T>
 */
public class GenericSpecification<T extends Object> implements Specification<T> {

    private SearchCriteria criteria;

    public GenericSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            if (criteria.getType().equals("number")) {
                return builder.greaterThanOrEqualTo(
                        root.<Long>get(criteria.getKey()).as(Long.class), Long.parseLong(criteria.getValue().toString()));
            } else {
                return builder.greaterThanOrEqualTo(
                        root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            if (criteria.getType().equals("number")) {
                return builder.lessThanOrEqualTo(
                        root.<Long>get(criteria.getKey()).as(Long.class), Long.parseLong(criteria.getValue().toString()));
            } else {
                return builder.lessThanOrEqualTo(
                        root.<String>get(criteria.getKey()), criteria.getValue().toString());
            }
        } else if (criteria.getOperation().equalsIgnoreCase("~")) {
            switch(criteria.getMode()){
                case "stricte": 
                    return builder.equal(root.get(criteria.getKey()), criteria.getValue());
                case "large": 
                    return builder.like(root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
                default: 
                    return builder.like(root.<String>get(criteria.getKey()), criteria.getValue() + "%");
            }
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            return builder.equal(root.get(criteria.getKey()), criteria.getValue());
        } else {
            String field = criteria.field();

            if (!field.equals("createdAt")) {
                if (criteria.getType().equals("number")) {
                    if (criteria.direction().equals("DESC")) {
                        query.orderBy(builder.desc(root.<Long>get(field).as(Long.class)));
                    } else {
                        query.orderBy(builder.asc(root.<Long>get(field).as(Long.class)));
                    }
                }
            } else {
                if (criteria.direction().equals("DESC")) {
                    query.orderBy(builder.desc(root.<String>get(field)));
                } else {
                    query.orderBy(builder.asc(root.<String>get(field)));
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "GenericSpecification{" + "criteria=" + criteria + '}';
    }

}

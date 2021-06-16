package com.dev.nc.ibank.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cleaning up in-memory database tables after each integration test
 */
@Service
@Profile("database-cleanup")
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;
    private List<String> tableNames;

    @Transactional
    public void truncate() {
        entityManager.flush();
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();

        for (final String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
        }

        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()//wrap
                .filter(e -> e.getJavaType().getAnnotation(Table.class) != null)//wrap
                .map(e -> e.getJavaType().getAnnotation(Table.class).name())//wrap
                .collect(Collectors.toList());
    }
}

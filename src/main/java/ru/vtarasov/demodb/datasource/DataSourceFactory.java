package ru.vtarasov.demodb.datasource;

import javax.sql.DataSource;

/**
 * @author vtarasov
 * @since 03.11.18
 */
public interface DataSourceFactory {
    DataSource get();
}

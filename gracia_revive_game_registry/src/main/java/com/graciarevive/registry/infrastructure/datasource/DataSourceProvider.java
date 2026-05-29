/*
 * Copyright (C) 2026 GraciaRevive
 *
 * This file is part of GraciaRevive.
 *
 * GraciaRevive is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * GraciaRevive is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * GraciaRevive. If not, see <http://www.gnu.org/licenses/>.
 */
package com.graciarevive.registry.infrastructure.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import com.graciarevive.registry.config.RegistryConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

 /**
 * DataSourceProvider.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public final class DataSourceProvider implements AutoCloseable {

    private final HikariDataSource dataSource;

    public DataSourceProvider(RegistryConfig config) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(config.dbUrl());
        hikariConfig.setUsername(config.dbUser());
        hikariConfig.setPassword(config.dbPassword());
        hikariConfig.setMaximumPoolSize(config.dbPoolSize());
        hikariConfig.setPoolName("GraciaRevive-DB-Pool");
        hikariConfig.setAutoCommit(true);

        this.dataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    @Override
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
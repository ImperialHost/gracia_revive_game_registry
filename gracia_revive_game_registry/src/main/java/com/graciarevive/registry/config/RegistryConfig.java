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
package com.graciarevive.registry.config;

import java.util.Objects;

/**
 * Immutable registry database configuration.
 *
 * @author Marian
 * @since 1.0
 * @version 1.1
 */
public record RegistryConfig(
        String dbUrl,
        String dbUser,
        String dbPassword,
        int dbPoolSize
) {

    public RegistryConfig {
        Objects.requireNonNull(dbUrl, "dbUrl must not be null");
        Objects.requireNonNull(dbUser, "dbUser must not be null");
        Objects.requireNonNull(dbPassword, "dbPassword must not be null");

        if (dbUrl.isBlank()) {
            throw new IllegalArgumentException("dbUrl must not be blank");
        }

        if (dbUser.isBlank()) {
            throw new IllegalArgumentException("dbUser must not be blank");
        }

        if (dbPoolSize <= 0) {
            throw new IllegalArgumentException(
                    "dbPoolSize must be greater than zero");
        }
    }
}

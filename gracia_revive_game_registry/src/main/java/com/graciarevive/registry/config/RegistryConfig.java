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

 /**
 * RegistryConfig.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public record RegistryConfig(
        String dbUrl,
        String dbUser,
        String dbPassword,
        int dbPoolSize
) {
    public RegistryConfig {
        if (dbPoolSize <= 0) {
            dbPoolSize = 10;
        }
    }
}
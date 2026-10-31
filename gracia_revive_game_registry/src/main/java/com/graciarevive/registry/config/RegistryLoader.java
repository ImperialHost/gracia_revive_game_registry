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
 * GraciaRevive. If not, see http://www.gnu.org/licenses/.
 */
package com.graciarevive.registry.config;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads and validates registry database configuration.
 *
 * @author Marian
 * @since 1.0
 * @version 1.1
 */
public final class RegistryLoader {

    private static final Logger LOG = LoggerFactory.getLogger(RegistryLoader.class);

    private static final Path CONFIG_PATH =
            Path.of(System.getProperty("user.dir"), "config", "database.properties");

    private RegistryLoader() {
        // Utility class
    }

    public static RegistryConfig load() {
        try {
            final var props = new Properties();

            try (BufferedReader reader =
                    Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                props.load(reader);
            }

            final String mode = get(props, "db.mode", "ipv4")
                    .toLowerCase(Locale.ROOT);

            if (!mode.equals("ipv4") && !mode.equals("ipv6")) {
                throw new IllegalArgumentException(
                        "Invalid db.mode value: " + mode + ". Allowed values: ipv4, ipv6");
            }

            final String url = switch (mode) {
                case "ipv6" -> getRequired(props, "db.url.ipv6");
                case "ipv4" -> getRequired(props, "db.url.ipv4");
                default -> throw new IllegalStateException("Unexpected mode: " + mode);
            };

            final String user = getRequired(props, "db.user");

            final String password = get(props, "db.password");
            if (password == null) {
                throw new IllegalStateException(
                        "Missing required property: db.password (even if empty, it must exist)");
            }

            if (password.isBlank()) {
                LOG.warn("Database password is empty. This is NOT recommended for production.");
            }

            final int poolSize;
            try {
                poolSize = Integer.parseInt(get(props, "db.pool.size", "10"));

                if (poolSize <= 0) {
                    throw new IllegalArgumentException(
                            "Invalid db.pool.size value: " + poolSize + ". Value must be > 0");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "Invalid numeric value for db.pool.size", e);
            }

            final RegistryConfig config =
                    new RegistryConfig(url, user, password, poolSize);

            LOG.info(
                    "Registry configuration loaded successfully (mode: {}) from {}",
                    mode,
                    CONFIG_PATH);

            return config;

        } catch (Exception e) {
            LOG.error(
                    "Failed to load registry configuration from {}: {}",
                    CONFIG_PATH,
                    e.getMessage(),
                    e);

            throw new IllegalStateException(
                    "Cannot start application without valid configuration",
                    e);
        }
    }

    private static String get(Properties props, String key) {
        return normalize(props.getProperty(key));
    }

    private static String get(Properties props, String key, String defaultValue) {
        return normalize(props.getProperty(key, defaultValue));
    }

    private static String getRequired(Properties props, String key) {
        String value = get(props, key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing required property: " + key);
        }

        return value;
    }

    private static String normalize(String value) {
        return value == null ? null : value.strip();
    }
}

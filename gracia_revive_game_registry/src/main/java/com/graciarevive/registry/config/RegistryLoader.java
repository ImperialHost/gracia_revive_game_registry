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

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /**
 * RegistryLoader.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public final class RegistryLoader {

    private static final Logger LOG = LoggerFactory.getLogger(RegistryLoader.class);

    private static final Path CONFIG_PATH =
            Path.of(System.getProperty("user.dir"), "config", "database.properties");

    private RegistryLoader() {}

    public static RegistryConfig load() {
        try {
            Properties props = new Properties();

            try (BufferedReader reader =
                         Files.newBufferedReader(CONFIG_PATH, StandardCharsets.UTF_8)) {
                props.load(reader);
            }

            String mode = get(props, "db.mode", "ipv4").toLowerCase();

            if (!mode.equals("ipv4") && !mode.equals("ipv6")) {
                throw new IllegalArgumentException(
                        "Invalid db.mode value: " + mode + ". Allowed: ipv4, ipv6");
            }

            String url = switch (mode) {
                case "ipv6" -> get(props, "db.url.ipv6");
                default -> get(props, "db.url.ipv4");
            };

            if (url == null || url.isBlank()) {
                throw new IllegalStateException(
                        "Database URL missing for mode: " + mode +
                        ". Expected key: db.url." + mode);
            }

            String user = get(props, "db.user");
            if (user == null || user.isBlank()) {
                throw new IllegalStateException("Missing required property: db.user");
            }

         String password = get(props, "db.password");

         if (password == null) {
             throw new IllegalStateException("Missing required property: db.password (even if empty, it must exist)");
         }

         if (password.isBlank()) {
             LOG.warn("⚠️ Database password is empty! This is NOT recommended for production.");
         }

            int poolSize;
            try {
                poolSize = Integer.parseInt(get(props, "db.pool.size", "10"));
                if (poolSize <= 0) {
                    throw new IllegalArgumentException("db.pool.size must be > 0");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid db.pool.size value", e);
            }

            RegistryConfig config = new RegistryConfig(url, user, password, poolSize);

            LOG.info("✅ Registry config loaded successfully (mode: {}) from {}", mode, CONFIG_PATH);

            return config;

        } catch (Exception e) {
            LOG.error("❌ Failed to load registry config from {}: {}", CONFIG_PATH, e.getMessage(), e);
            throw new IllegalStateException("Cannot start without valid config", e);
        }
    }

    private static String get(Properties props, String key) {
        return normalize(props.getProperty(key));
    }

    private static String get(Properties props, String key, String def) {
        return normalize(props.getProperty(key, def));
    }

    private static String normalize(String value) {
        return value == null ? null : value.trim();
    }
}

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
package com.graciarevive.registry.infrastructure.loader;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

 /**
 * ServerNameLoader.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public final class ServerNameLoader {

    private static final Logger log =
            LoggerFactory.getLogger(ServerNameLoader.class);

    private static final String RESOURCE_PATH =
            "/servername.json";

    private final ObjectMapper mapper = new ObjectMapper();

    public Map<Integer, String> load() {

        try (InputStream in = ServerNameLoader.class.getResourceAsStream(RESOURCE_PATH)) {

            if (in == null) {
                throw new IllegalStateException(
                        "Resource not found: " + RESOURCE_PATH
                );
            }

            JsonNode root = mapper.readTree(in)
                    .path("servers_list")
                    .path("server");

            Map<Integer, String> map = new LinkedHashMap<>();

            for (JsonNode node : root) {
                int id = node.path("id").asInt();
                String name = node.path("name").asText();
                map.put(id, name);
            }

            log.info("ℹ️ Loaded {} server names from resource {}",
                    map.size(),
                    RESOURCE_PATH);

            return map;

        } catch (Exception e) {

            log.error("❌ Failed to load server names from resource {}",
                    RESOURCE_PATH,
                    e);

            throw new IllegalStateException(
                    "Failed to load server names",
                    e);
        }
    }
}
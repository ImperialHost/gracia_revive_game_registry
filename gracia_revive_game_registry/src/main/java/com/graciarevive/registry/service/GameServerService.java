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
package com.graciarevive.registry.service;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graciarevive.registry.infrastructure.datasource.DataSourceProvider;
import com.graciarevive.registry.infrastructure.file.FileIO;
import com.graciarevive.registry.security.identity.ServerIdentityGenerator;

 /**
 * GameServerService.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public final class GameServerService {

    private static final Logger LOG = LoggerFactory.getLogger(GameServerService.class);

    private static final String SELECT_SERVERS_SQL = """
            SELECT server_id, hexid, created_at
            FROM gameservers
            """;

        private static final String INSERT_SERVER_SQL = """
            INSERT INTO gameservers (server_id, hexid)
            VALUES (?, ?)
            """;

        private static final String DELETE_SERVER_SQL = """
            DELETE FROM gameservers
            WHERE server_id = ?
            """;

    private static final HexFormat HEX = HexFormat.of();
    private final DataSourceProvider dataSource;
    private final ServerIdentityGenerator identityGenerator;

    public GameServerService(DataSourceProvider dataSource,
                             ServerIdentityGenerator identityGenerator) {
        this.dataSource = dataSource;
        this.identityGenerator = identityGenerator;
    }

    public void createTable(Path schemaFile) {
        try {
            String sql = FileIO.readString(schemaFile);

            try (Connection con = dataSource.getConnection();
                 Statement stmt = con.createStatement()) {

                stmt.execute(sql);
                LOG.info("✅ gameservers table ensured/created");
            }

        } catch (Exception e) {
            LOG.error("❌ Failed to create gameservers table", e);
            throw new IllegalStateException(e);
        }
    }

    public Map<Integer, String> listServers() {
        Map<Integer, String> map = new LinkedHashMap<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(SELECT_SERVERS_SQL);
             ResultSet rs = ps.executeQuery()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            while (rs.next()) {
                int serverId = rs.getInt("server_id");
                String hexId = HEX.formatHex(rs.getBytes("hexid"));

                LocalDateTime createdAt = rs.getTimestamp("created_at")
                                            .toLocalDateTime();

                String serverInfo = String.format(
                    "%s | Registered at: %s",
                    hexId,
                    createdAt.format(formatter)
                );

                map.put(serverId, serverInfo);
            }

            LOG.info("ℹ️ Loaded {} servers", map.size());
            return map;

        } catch (Exception e) {
            LOG.error("❌ Failed to list servers", e);
            throw new IllegalStateException(e);
        }
    }

    public void registerServer(int serverId, Path outputFile) {
        try {
            byte[] hexId = identityGenerator.generate();

            String hexString = identityGenerator.toHex(hexId);

            String content =
                    "#the hexID to auth into login\n" +
                    "HexID=" + hexString + "\n" +
                    "ServerID=" + serverId;

            FileIO.writeString(outputFile, content);

            try (Connection con = dataSource.getConnection();
                 PreparedStatement ps = con.prepareStatement(INSERT_SERVER_SQL)) {

                ps.setInt(1, serverId);
                ps.setBytes(2, hexId);
                ps.executeUpdate();
            }

            LOG.info("✅💾 Server {} registered successfully", serverId);

        } catch (Exception e) {
            LOG.error("❌ Failed to register server {}", serverId, e);
            throw new IllegalStateException(e);
        }
    }

    public void deleteServer(int serverId) {
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(DELETE_SERVER_SQL)) {

            ps.setInt(1, serverId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                LOG.info("🗑️ Server {} deleted", serverId);
            } else {
                LOG.warn("⚠️ Server {} not found", serverId);
            }

        } catch (Exception e) {
            LOG.error("❌ Failed to delete server {}", serverId, e);
            throw new IllegalStateException(e);
        }
    }
}
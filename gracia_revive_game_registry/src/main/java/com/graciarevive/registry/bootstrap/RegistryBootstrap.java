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
package com.graciarevive.registry.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graciarevive.registry.config.RegistryConfig;
import com.graciarevive.registry.config.RegistryLoader;
import com.graciarevive.registry.infrastructure.datasource.DataSourceProvider;
import com.graciarevive.registry.security.identity.ServerIdentityGenerator;
import com.graciarevive.registry.service.GameServerService;
import com.graciarevive.registry.ui.console.RegistryCli;

 /**
 * RegistryBootstrap.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public final class RegistryBootstrap {

    private static final Logger LOG =
            LoggerFactory.getLogger(RegistryBootstrap.class);

    public static void main(String[] args) {
        new RegistryBootstrap().run();
    }

    private void run() {
        LOG.info("🚀 Starting Registry...");

        RegistryConfig config = RegistryLoader.load();

        DataSourceProvider dataSource = new DataSourceProvider(config);
        ServerIdentityGenerator identityGenerator = new ServerIdentityGenerator();
        GameServerService gameServerService =
                new GameServerService(dataSource, identityGenerator);

        RegistryCli cli = new RegistryCli(gameServerService);

        addShutdownHook(dataSource);

        try {
            cli.start();
        } catch (Exception e) {
            LOG.error("❌ Registry failed unexpectedly", e);
            throw e;
        } finally {
            safeClose(dataSource);
        }
    }

    private void addShutdownHook(DataSourceProvider dataSource) {
        Thread shutdownHook = new Thread(() -> {
            LOG.info("ℹ️ Shutting down Registry...");
            safeClose(dataSource);
        }, "registry-shutdown-hook");

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    private void safeClose(DataSourceProvider dataSource) {
        try {
            dataSource.close();
        } catch (Exception e) {
            LOG.warn("⚠️ Failed to close DataSource cleanly", e);
        }
    }
}
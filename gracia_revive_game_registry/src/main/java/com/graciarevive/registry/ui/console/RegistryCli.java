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
package com.graciarevive.registry.ui.console;

import java.nio.file.Path;
import java.util.Scanner;

import com.graciarevive.registry.i18n.MessageService;
import com.graciarevive.registry.infrastructure.loader.ServerNameLoader;
import com.graciarevive.registry.service.GameServerService;

 /**
 * RegistryCli.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public final class RegistryCli {

    private final GameServerService gameServerService;
    private final Scanner scanner = new Scanner(System.in);
    private final MessageService messages;

    public RegistryCli(GameServerService gameServerService) {
        this.gameServerService = gameServerService;

        String lang = System.getProperty("app.language", "ro");
        this.messages = new MessageService(lang);
    }

    public void start() {
        showSplashScreen();

        while (true) {
            printMenu();

            System.out.print("👉 " + messages.get("prompt.select") + " ");

            int opt;

            if (!scanner.hasNextInt()) {
                System.out.println(messages.get("prompt.invalid"));
                scanner.next();
                continue;
            }

            opt = scanner.nextInt();

            System.out.printf("👋 %d%n", opt);

            switch (opt) {

                case 1 -> gameServerService.createTable(
                        Path.of("schema/gameservers.sql"));

                case 2 -> showServer();

                case 3 -> registerServer();

                case 4 -> gameServerService.listServers()
                        .forEach((id, name) -> System.out.println(id + " → " + name));

                case 5 -> deleteServer();

                case 6 -> aboutProject();

                case 7 -> {
                    System.out.println("👋 " + messages.get("menu.option7"));
                    return;
                }

                default -> System.out.println(messages.get("prompt.invalid"));
            }
        }
    }

    private void printMenu() {

        System.out.println("\n===== " + messages.get("menu.title") + " =====\n");

        System.out.println("📂 1. " + messages.get("menu.option1"));
        System.out.println("📄 2. " + messages.get("menu.option2"));
        System.out.println("💾 3. " + messages.get("menu.option3"));
        System.out.println("📋 4. " + messages.get("menu.option4"));
        System.out.println("🗑️ 5. " + messages.get("menu.option5"));
        System.out.println("ℹ️ 6. " + messages.get("menu.option6"));
        System.out.println("👋 7. " + messages.get("menu.option7"));
    }

    private void showServer() {
        ServerNameLoader loader = new ServerNameLoader();

        loader.load()
                .forEach((id, name) ->
                        System.out.println(id + " → " + name));
    }

    private void registerServer() {

        System.out.print(messages.get("server.chooseId") + " ");

        if (!scanner.hasNextInt()) {
            System.out.println(messages.get("server.invalidNumber"));
            scanner.next();
            return;
        }

        int id = scanner.nextInt();

        boolean exists = gameServerService.listServers().containsKey(id);

        if (exists) {
            System.out.println(messages.get("server.invalidId"));
            return;
        }

        System.out.print(messages.get("server.confirmRegister", id));

        String confirm = scanner.next();

        if (!confirm.equalsIgnoreCase("y")) {
            System.out.println(messages.get("server.cancelled"));
            return;
        }

        try {
            gameServerService.registerServer(
                    id,
                    Path.of("hexid", "hexid.txt")
            );

            System.out.println(messages.get("server.registered"));

        } catch (Exception e) {
            System.out.println(messages.get("server.failed"));
        }
    }

    private void deleteServer() {

        System.out.print(messages.get("delete.chooseId") + " ");

        if (!scanner.hasNextInt()) {
            System.out.println(messages.get("server.invalidNumber"));
            scanner.next();
            return;
        }

        int id = scanner.nextInt();

        System.out.print(messages.get("delete.confirm", id));

        String confirm = scanner.next();

        if (confirm.equalsIgnoreCase("y")) {
            gameServerService.deleteServer(id);
            System.out.println(messages.get("delete.success"));
        } else {
            System.out.println(messages.get("delete.cancelled"));
        }
    }

    private void showSplashScreen() {

        System.out.println("""

                    ━━━━━━━━━━━━━━━━━━━━━━
                          GRACIA REVIVE
                    ━━━━━━━━━━━━━━━━━━━━━━

                    ⚙️ GraciaRevive Game Registry
                    🚀 Starting system...
                """);

        int total = 30;

        for (int i = 0; i <= total; i++) {

            int percent = (i * 100) / total;

            String bar = "█".repeat(i) + "░".repeat(total - i);

            System.out.print("\r⚙️ " +
                    messages.get("system.loading") +
                    ": [" + bar + "] " + percent + "%");

            try {
                Thread.sleep(60);
            } catch (InterruptedException ignored) {}
        }

        System.out.println("\n\n✅ " + messages.get("system.ready") + "\n");
    }

    private void aboutProject() {

        System.out.println("""

            ℹ️ ABOUT THE PROJECT
            ────────────────────────────────────────────────────────────
            Name:        GraciaRevive Game Registry
            Author:      GraciaRevive Team
            Launch:      May 2026

            Description:
            This project provides a simple and intuitive way to manage
            GraciaRevive game servers directly from the command line.

            Features:
            - Manage the gameservers table
            - View and list servers
            - Add and delete servers
            - Interactive CLI experience

            Slogan:
            "Revive your game. Take control of your server."
            ────────────────────────────────────────────────────────────
        """);
    }
}
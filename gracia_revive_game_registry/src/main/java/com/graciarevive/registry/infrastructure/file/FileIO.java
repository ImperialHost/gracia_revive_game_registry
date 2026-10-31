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
package com.graciarevive.registry.infrastructure.file;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Utility class for file operations.
 *
 * @author Marian
 * @since 1.0
 * @version 1.1
 */
public final class FileIO {

    private FileIO() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static String readString(Path path) {
        Objects.requireNonNull(path, "path must not be null");

        try {
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new FileIOException(
                    "Failed to read string from file: " + path, e);
        }
    }

    public static byte[] readBytes(Path path) {
        Objects.requireNonNull(path, "path must not be null");

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new FileIOException(
                    "Failed to read bytes from file: " + path, e);
        }
    }

    public static void writeString(Path path, String content) {
        Objects.requireNonNull(path, "path must not be null");
        Objects.requireNonNull(content, "content must not be null");

        try {
            ensureParentDirectory(path);

            Files.writeString(
                    path,
                    content,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new FileIOException(
                    "Failed to write string to file: " + path, e);
        }
    }

    public static void writeBytes(Path path, byte[] data) {
        Objects.requireNonNull(path, "path must not be null");
        Objects.requireNonNull(data, "data must not be null");

        try {
            ensureParentDirectory(path);

            Files.write(
                    path,
                    data,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new FileIOException(
                    "Failed to write bytes to file: " + path, e);
        }
    }

    public static void deleteIfExists(Path path) {
        Objects.requireNonNull(path, "path must not be null");

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new FileIOException(
                    "Failed to delete file: " + path, e);
        }
    }

    public static boolean exists(Path path) {
        Objects.requireNonNull(path, "path must not be null");

        return Files.exists(path);
    }

    public static void ensureParentDirectory(Path path) {
        Objects.requireNonNull(path, "path must not be null");

        try {
            final Path parent = path.getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException e) {
            throw new FileIOException(
                    "Failed to create parent directories for: " + path, e);
        }
    }
}

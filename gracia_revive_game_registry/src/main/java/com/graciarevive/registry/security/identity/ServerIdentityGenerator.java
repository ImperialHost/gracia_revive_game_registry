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
package com.graciarevive.registry.security.identity;

import java.security.SecureRandom;
import java.util.HexFormat;

/**
 * Utility class responsible for generating secure server identities.
 *
 * <p>This class generates cryptographically secure random identifiers
 * used for server authentication and identification within the system.
 *
 * <p>The generated ID is returned as raw bytes or as a hexadecimal string
 * representation for storage or transmission purposes.
 *
 * <p>It uses {@link SecureRandom} to ensure unpredictability and
 * {@link HexFormat} to convert binary data into a readable hex format.
 *
 * <p>Typical usage:
 * <pre>
 *     ServerIdentityGenerator generator = new ServerIdentityGenerator();
 *     byte[] id = generator.generate();
 *     String hex = generator.toHex(id);
 * </pre>
 *
 * ServerIdentityGenerator.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public final class ServerIdentityGenerator {

    private static final int ID_SIZE = 32;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a cryptographically secure random server ID.
     *
     * @return a byte array containing the generated server identity
     */
    public byte[] generate() {
        byte[] id = new byte[ID_SIZE];
        secureRandom.nextBytes(id);
        return id;
    }

    /**
     * Converts a binary server ID into its hexadecimal string representation.
     *
     * @param id the raw byte array representing the server identity
     * @return hexadecimal string representation of the ID
     */
    public String toHex(byte[] id) {
        return HexFormat.of().formatHex(id);
    }
}
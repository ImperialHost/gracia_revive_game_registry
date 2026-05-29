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
package com.graciarevive.registry.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

 /**
 * MessageService.
 * @author Marian
 * @since 1.0
 * @version 1.0
 */
public class MessageService {

    private final ResourceBundle bundle;

    public MessageService(String language) {

        Locale locale = switch (language.toLowerCase()) {
            case "ro" -> Locale.of("ro");
            case "en" -> Locale.of("en");
            default -> Locale.of("en");
        };

        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    public String get(String key) {
        return bundle.getString(key);
    }

    public String get(String key, Object... args) {
        return MessageFormat.format(bundle.getString(key), args);
    }
}
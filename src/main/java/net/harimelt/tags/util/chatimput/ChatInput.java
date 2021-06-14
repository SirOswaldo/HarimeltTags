/*
 * Copyright (C) 2021 SirOswaldo
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.harimelt.tags.util.chatimput;

import org.bukkit.entity.Player;

public abstract class ChatInput {

    private final String name;

    public ChatInput(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract boolean onChatInput(Player player, String input);

    public abstract void onPlayerSneak(Player player);
}
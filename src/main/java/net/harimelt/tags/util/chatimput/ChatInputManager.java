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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

public class ChatInputManager implements Listener {

    public ChatInputManager(JavaPlugin javaPlugin) {
        javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
    }

    private final HashMap<String, ChatInput> chatInputs = new HashMap<>();

    public void addChatInput(ChatInput chatInput) {
        chatInputs.put(chatInput.getName(), chatInput);
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (chatInputs.containsKey(name)) {
            ChatInput chatInput = chatInputs.get(name);
            if (!chatInput.onChatInput(player, event.getMessage())) {
                chatInputs.remove(name);
            }
        }
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();
        if (chatInputs.containsKey(name)) {
            ChatInput chatInput = chatInputs.get(name);
            chatInput.onPlayerSneak(player);
            chatInputs.remove(name);
        }
    }

}
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

package net.harimelt.tags;

import net.harimelt.tags.commands.*;
import net.harimelt.tags.inventories.TagEditInventory;
import net.harimelt.tags.listeners.AsyncPlayerPreLoginListener;
import net.harimelt.tags.placeholderapi.HarimeltTagsExpansion;
import net.harimelt.tags.playerdata.PlayerDataManager;
import net.harimelt.tags.storage.Storage;
import net.harimelt.tags.storage.storages.MySqlStorage;
import net.harimelt.tags.storage.storages.YamlStorage;
import net.harimelt.tags.tag.TagManager;
import net.harimelt.tags.util.chatimput.ChatInputManager;
import net.harimelt.tags.util.mysql.MySQL;
import net.harimelt.tags.util.papi.PlaceholderApi;
import net.harimelt.tags.util.yaml.Yaml;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class HarimeltTags extends JavaPlugin {

    private final Yaml configuration = new Yaml(this, "configuration");
    public Yaml getConfiguration() {
        return configuration;
    }
    private final Yaml messages = new Yaml(this, "messages");
    public Yaml getMessages() {
        return messages;
    }

    private PlayerDataManager playerDataManager;
    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    private Storage storage;
    public Storage getStorage() {
        return storage;
    }

    private final TagManager tagManager = new TagManager(this);
    public TagManager getTagManager() {
        return tagManager;
    }

    private final HashMap<UUID, String[]> editing = new HashMap<>();
    public HashMap<UUID, String[]> getEditing() {
        return editing;
    }

    private ChatInputManager chatInputManager;
    public ChatInputManager getChatInputManager() {
        return chatInputManager;
    }

    private MySQL playersMySql;
    public MySQL getPlayersMySql() {
        return playersMySql;
    }
    private  MySQL tagsMySql;
    public MySQL getTagsMySql() {
        return tagsMySql;
    }

    private String storageType;
    public String getStorageType() {
        return storageType;
    }

    @Override
    public void onEnable() {
        // Yaml
        configuration.registerFileConfiguration();
        messages.registerFileConfiguration();
        // Storage
        setupStorage();
        // Load All Tags
        tagManager.loadAll();
        // PlaceholderAPI
        PlaceholderApi.registerExpansion(new HarimeltTagsExpansion(this));
        // Commands
        HarimeltTagsCommand harimeltTagsCommand = new HarimeltTagsCommand(this);
        CreateTagCommand createTagCommand = new CreateTagCommand(this);
        DeleteTagCommand deleteTagCommand = new DeleteTagCommand(this);
        EditTagCommand editTagCommand = new EditTagCommand(this);
        ListTagsCommand listTagsCommand = new ListTagsCommand(this);
        SelectTagCommand selectTagCommand = new SelectTagCommand(this);
        // Listeners
        getServer().getPluginManager().registerEvents(new AsyncPlayerPreLoginListener(this), this);
        getServer().getPluginManager().registerEvents(new TagEditInventory(this), this);
        // PlayerData
        playerDataManager = new PlayerDataManager(this);
        // Load Online Players PlayerData
        playerDataManager.loadOnlinePlayerData();
        // InputManager
        chatInputManager = new ChatInputManager(this);
    }

    @Override
    public void onDisable() {
        // Unload Online Players PlayerData
        playerDataManager.unloadOnlinePlayerData();
        // Load All Tags
        tagManager.unloadAll();
    }

    public void setupStorage() {
        storageType = configuration.getFileConfiguration().getString("storage");
        if ("MYSQL".equals(storageType)) {
            String ip = configuration.getString("mysql.ip");
            String port = configuration.getString("mysql.port");
            String username = configuration.getString("mysql.username");
            String password = configuration.getString("mysql.password");
            String database = configuration.getString("mysql.database");
            String playersTable = configuration.getString("mysql.players-table-name");
            String tagsTable = configuration.getString("mysql.tags-table-name");
            String[][] players = new String[][] {
                    {"PLAYER_NAME", "VARCHAR(64)"},
                    {"TAG", "VARCHAR(32)"}
            };
            playersMySql = new MySQL(ip,port,username,password, database, playersTable, players);
            String[][] tags = new String[][] {
                    {"TAG_NAME", "VARCHAR(64)"},
                    {"PREFIX", "VARCHAR(32)"},
                    {"SUFFIX", "VARCHAR(32)"}
            };
            tagsMySql = new MySQL(ip,port,username,password, database, tagsTable, tags);
            if (!playersMySql.startConnection() || !tagsMySql.startConnection()) {
                getLogger().info("MYSQL: FAILED TO CONNECT, VERIFY AND RESTART.");
                getPluginLoader().disablePlugin(this);
            } else {
                playersMySql.createTable();
                playersMySql.closeConnection();
                tagsMySql.createTable();
                tagsMySql.closeConnection();
            }
            storage = new MySqlStorage(this, tagsMySql, playersMySql);
        } else {
            storage = new YamlStorage(this);
        }
    }

}

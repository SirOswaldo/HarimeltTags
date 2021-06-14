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

package net.harimelt.tags.playerdata;

import org.bukkit.entity.Player;
import net.harimelt.tags.storage.Storage;
import net.harimelt.tags.HarimeltTags;
import net.harimelt.tags.tasks.LoadPlayerDataTask;

import java.util.HashMap;

public class PlayerDataManager {

    private final HarimeltTags harimeltTags;

    public PlayerDataManager(HarimeltTags harimeltTags) {
        this.harimeltTags = harimeltTags;
    }

    private final HashMap<String, PlayerData> players = new HashMap<>();

    public void loadOnlinePlayerData() {
        for (Player player: harimeltTags.getServer().getOnlinePlayers()) {
            String name = player.getName();
            LoadPlayerDataTask loadPlayerDataTask = new LoadPlayerDataTask(harimeltTags, name);
            loadPlayerDataTask.startScheduler();
        }
    }

    public void loadPlayerData(String name) {
        Storage storage = harimeltTags.getStorage();
        String tag = storage.getTag(name);
        PlayerData playerData = new PlayerData(tag);
        players.put(name, playerData);
    }

    public void savePlayerData(String name) {
        Storage storage = harimeltTags.getStorage();
        PlayerData playerData = players.get(name);
        String tag = playerData.getTag();
        storage.setTag(name, tag);
    }

    public void unloadOnlinePlayerData() {
        players.clear();
    }

    public void unloadPlayerData(String name) {
        players.remove(name);
    }

    public PlayerData getPlayerData(String name) {
        return players.get(name);
    }

    public boolean containPlayerData(String name) {
        return players.containsKey(name);
    }

}

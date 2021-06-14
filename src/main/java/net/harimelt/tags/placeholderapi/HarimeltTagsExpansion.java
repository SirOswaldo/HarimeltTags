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

package net.harimelt.tags.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import net.harimelt.tags.playerdata.PlayerData;
import net.harimelt.tags.playerdata.PlayerDataManager;
import net.harimelt.tags.HarimeltTags;
import net.harimelt.tags.tag.Tag;
import net.harimelt.tags.tag.TagManager;

public class HarimeltTagsExpansion extends PlaceholderExpansion {

    private final HarimeltTags harimeltTags;

    public HarimeltTagsExpansion(HarimeltTags harimeltTags) {
        this.harimeltTags = harimeltTags;
    }

    @Override
    public String getName() {
        return "HarimeltTags";
    }

    @Override
    public String getIdentifier() {
        return "HarimeltTags";
    }

    @Override
    public String getAuthor() {
        return "SirOswaldo";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        PlayerDataManager playerDataManager = harimeltTags.getPlayerDataManager();
        if (player.isOnline()) {
            String name = player.getName();
            if (playerDataManager.containPlayerData(name)) {
                PlayerData playerData = playerDataManager.getPlayerData(name);
                TagManager tagManager = harimeltTags.getTagManager();
                String tagName = playerData.getTag();
                if (tagManager.exist(tagName)) {
                    Tag tag = tagManager.get(tagName);
                    switch (params) {
                        case "prefix":
                            return tag.getPrefix();
                        case "suffix":
                            return tag.getSuffix();
                        case "tag":
                            return tag.getName();
                    }
                }
            }
        }
        return "";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        PlayerDataManager playerDataManager = harimeltTags.getPlayerDataManager();
        String name = player.getName();
        if (playerDataManager.containPlayerData(name)) {
            PlayerData playerData = playerDataManager.getPlayerData(name);
            TagManager tagManager = harimeltTags.getTagManager();
            String tagName = playerData.getTag();
            if (tagManager.exist(tagName)) {
                Tag tag = tagManager.get(tagName);
                switch (params) {
                    case "prefix":
                        return tag.getPrefix();
                    case "suffix":
                        return tag.getSuffix();
                    case "tag":
                        return tag.getName();
                }
            }
        }
        return "";
    }

}
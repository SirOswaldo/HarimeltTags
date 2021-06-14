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

package net.harimelt.tags.inputs;

import net.harimelt.tags.inventories.TagEditInventory;
import net.harimelt.tags.util.chatimput.ChatInput;
import org.bukkit.entity.Player;
import net.harimelt.tags.HarimeltTags;
import net.harimelt.tags.tag.TagManager;

import java.util.UUID;

public class RenameInput extends ChatInput {

    private final HarimeltTags harimeltTags;

    public RenameInput(HarimeltTags harimeltTags, Player player) {
        super(player.getName());
        this.harimeltTags = harimeltTags;
    }

    @Override
    public boolean onChatInput(Player player, String text) {
        UUID uuid = player.getUniqueId();
        if (harimeltTags.getEditing().containsKey(uuid)) {
            String name = harimeltTags.getEditing().get(uuid)[1];
            TagManager tagManager = harimeltTags.getTagManager();
            if (!text.contains(" ")) {
                tagManager.rename(name, text);
                harimeltTags.getEditing().put(uuid, new String[] {"MENU", text});
                TagEditInventory tagEditInventory = new TagEditInventory(harimeltTags);
                player.openInventory(tagEditInventory.getInventory(text));
            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onPlayerSneak(Player player) {
        UUID uuid = player.getUniqueId();
        if (harimeltTags.getEditing().containsKey(uuid)) {
            String name = harimeltTags.getEditing().get(uuid)[1];
            harimeltTags.getEditing().put(uuid, new String[] {"MENU", name});
            TagEditInventory tagEditInventory = new TagEditInventory(harimeltTags);
            player.openInventory(tagEditInventory.getInventory(name));
        }
    }

}
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

package net.harimelt.tags.storage.storages;

import net.harimelt.tags.storage.Storage;
import net.harimelt.tags.storage.enums.StorageType;
import net.harimelt.tags.HarimeltTags;
import net.harimelt.tags.util.mysql.MySQL;

import java.util.List;

public class MySqlStorage extends Storage {

    private final MySQL tagsMySql;
    private final MySQL playersMySql;

    public MySqlStorage(HarimeltTags harimeltTags, MySQL tagsMySql, MySQL playersMySql) {
        super(StorageType.MYSQL);
        this.tagsMySql = tagsMySql;
        this.playersMySql = playersMySql;
    }

    @Override
    public String getTag(String name) {
        String prefix = "";
        playersMySql.startConnection();
        if (playersMySql.existsPrimaryKey(name)) {
            prefix = playersMySql.getString(name, "TAG");
        }
        playersMySql.closeConnection();
        return prefix;
    }

    @Override
    public void setTag(String name, String tag) {
        playersMySql.startConnection();
        if (!playersMySql.existsPrimaryKey(name)) {
            playersMySql.createRow(new Object[] {name, ""});
        }
        playersMySql.setString(name, "TAG", tag);
        playersMySql.closeConnection();
    }

    @Override
    public void deletePlayer(String name) {
        playersMySql.startConnection();
        if (playersMySql.existsPrimaryKey(name)) {
            playersMySql.drop(name);
        }
        playersMySql.closeConnection();
    }

    @Override
    public List<String> getAllTagNames() {
        List<String> names;
        tagsMySql.startConnection();
        names = tagsMySql.getAllValues();
        tagsMySql.closeConnection();
        return names;
    }

    @Override
    public String getTagPrefix(String tag) {
        String prefix = "";
        tagsMySql.startConnection();
        if (tagsMySql.existsPrimaryKey(tag)) {
            prefix = tagsMySql.getString(tag, "PREFIX");
        }
        tagsMySql.closeConnection();
        return prefix;
    }

    @Override
    public void setTagPrefix(String tag, String prefix) {
        tagsMySql.startConnection();
        if (!tagsMySql.existsPrimaryKey(tag)) {
            tagsMySql.createRow(new Object[] {tag, prefix, ""});
        }
        tagsMySql.setString(tag, "PREFIX", prefix);
        tagsMySql.closeConnection();
    }

    @Override
    public String getTagSuffix(String tag) {
        String suffix = "";
        tagsMySql.startConnection();
        if (tagsMySql.existsPrimaryKey(tag)) {
            suffix = tagsMySql.getString(tag, "SUFFIX");
        }
        tagsMySql.closeConnection();
        return suffix;
    }

    @Override
    public void setTagSuffix(String tag, String suffix) {
        tagsMySql.startConnection();
        if (!tagsMySql.existsPrimaryKey(tag)) {
            tagsMySql.createRow(new Object[] {tag, suffix, ""});
        }
        tagsMySql.setString(tag, "SUFFIX", suffix);
        tagsMySql.closeConnection();
    }

    @Override
    public void deleteTag(String tag) {
        tagsMySql.startConnection();
        if (tagsMySql.existsPrimaryKey(tag)) {
            tagsMySql.drop(tag);
        }
        tagsMySql.closeConnection();
    }


}

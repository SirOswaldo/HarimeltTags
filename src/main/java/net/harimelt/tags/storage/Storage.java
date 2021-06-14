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

package net.harimelt.tags.storage;

import net.harimelt.tags.storage.enums.StorageType;

import java.util.List;

public abstract class Storage {

    private final StorageType storageType;
    public StorageType getStorageType() {
        return storageType;
    }

    public Storage(StorageType storageType) {
        this.storageType = storageType;
    }

    public abstract String getTag(String name);
    public abstract void setTag(String name, String prefix);

    public abstract void deletePlayer(String name);

    public abstract List<String> getAllTagNames();

    public abstract String getTagPrefix(String tag);

    public abstract void setTagPrefix(String tag, String prefix);

    public abstract String getTagSuffix(String tag);

    public abstract void setTagSuffix(String tag, String suffix);

    public abstract void deleteTag(String tag);

}
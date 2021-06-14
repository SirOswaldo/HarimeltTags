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

import org.bukkit.configuration.file.FileConfiguration;
import net.harimelt.tags.storage.enums.StorageType;
import net.harimelt.tags.HarimeltTags;
import net.harimelt.tags.storage.Storage;
import net.harimelt.tags.util.yaml.Yaml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YamlStorage extends Storage {

    private final HarimeltTags harimeltTags;

    public YamlStorage(HarimeltTags harimeltTags) {
        super(StorageType.YAML);
        this.harimeltTags = harimeltTags;
    }

    @Override
    public String getTag(String name) {
        Yaml yaml = new Yaml(harimeltTags, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        return fileConfiguration.getString("tag", "");
    }

    @Override
    public void setTag(String name, String prefix) {
        Yaml yaml = new Yaml(harimeltTags, "players", name);
        yaml.registerFileConfiguration();
        FileConfiguration fileConfiguration = yaml.getFileConfiguration();
        fileConfiguration.set("tag", prefix);
        yaml.saveFileConfiguration();
    }

    @Override
    public void deletePlayer(String name) {
        Yaml yaml = new Yaml(harimeltTags, "players", name);
        if (yaml.existFileConfiguration()) {
            yaml.deleteFileConfiguration();
        }
    }

    @Override
    public List<String> getAllTagNames() {
        List<String> names = new ArrayList<>();
        File directory = new File(harimeltTags.getDataFolder() + File.separator + "tags");
        if (directory.exists()) {
            if (directory.isDirectory()) {
                File[] files = directory.listFiles(pathname -> pathname.getName().endsWith(".yml"));
                for (File file:files) {
                    names.add(file.getName().replaceAll(".yml", ""));
                }
            }
        }
        return names;
    }

    @Override
    public String getTagPrefix(String tag) {
        Yaml yaml = new Yaml(harimeltTags, "tags", tag);
        yaml.registerFileConfiguration();
        return yaml.getString("prefix", "");
    }

    @Override
    public void setTagPrefix(String tag, String prefix) {
        Yaml yaml = new Yaml(harimeltTags, "tags", tag);
        yaml.registerFileConfiguration();
        yaml.set("prefix", prefix);
        yaml.saveFileConfiguration();
    }

    @Override
    public String getTagSuffix(String tag) {
        Yaml yaml = new Yaml(harimeltTags, "tags", tag);
        yaml.registerFileConfiguration();
        return yaml.getString("suffix", "");
    }

    @Override
    public void setTagSuffix(String tag, String suffix) {
        Yaml yaml = new Yaml(harimeltTags, "tags", tag);
        yaml.registerFileConfiguration();
        yaml.set("suffix", suffix);
        yaml.saveFileConfiguration();
    }

    @Override
    public void deleteTag(String tag) {
        Yaml yaml = new Yaml(harimeltTags, "tags", tag);
        if (yaml.existFileConfiguration()) {
            yaml.deleteFileConfiguration();
        }
    }

}

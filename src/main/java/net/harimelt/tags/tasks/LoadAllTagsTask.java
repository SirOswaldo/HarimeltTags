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

package net.harimelt.tags.tasks;

import net.harimelt.tags.util.mysql.MySQL;
import net.harimelt.tags.util.task.Task;
import net.harimelt.tags.HarimeltTags;
import net.harimelt.tags.tag.Tag;
import net.harimelt.tags.util.yaml.Yaml;


import java.io.File;
import java.io.FileFilter;
import java.util.List;

public class LoadAllTagsTask extends Task {

    private final HarimeltTags harimeltTags;
    private final List<String> tags;

    public LoadAllTagsTask(HarimeltTags harimeltTags) {
        super(harimeltTags, 20);
        this.harimeltTags = harimeltTags;
        tags = harimeltTags.getStorage().getAllTagNames();
    }

    @Override
    public void actions() {
        if (harimeltTags.getStorageType().equals("MYSQL")) {
            MySQL mySQL = harimeltTags.getTagsMySql();
            mySQL.startConnection();
            for (String tagName:tags) {
                Tag tag = new Tag(tagName);
                tag.setPrefix(mySQL.getString(tagName, "PREFIX"));
                tag.setPrefix(mySQL.getString(tagName, "SUFFIX"));
                harimeltTags.getTagManager().add(tag);
            }
            mySQL.closeConnection();
        } else {
            File directory = new File(harimeltTags.getDataFolder() + File.separator + "tags");
            if (directory.exists()) {
                File[] files = directory.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File pathname) {
                        return pathname.getName().endsWith(".yml");
                    }
                });
                if (files != null) {
                    if (files.length > 0) {
                        for (File file:files) {
                            String name = file.getName().replaceAll(".yml", "");
                            Yaml yaml = new Yaml(harimeltTags, "tags", name);
                            yaml.registerFileConfiguration();
                            Tag tag = new Tag(name);
                            tag.setPrefix(yaml.getString("prefix", ""));
                            tag.setSuffix(yaml.getString("suffix", ""));
                            harimeltTags.getTagManager().add(tag);
                        }
                    }
                }
            }
        }
        stopScheduler();
    }

}
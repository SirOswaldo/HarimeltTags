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
        if (tags.isEmpty()) {
            stopScheduler();
        } else {
            MySQL mySQL = harimeltTags.getTagsMySql();
            mySQL.startConnection();
            for (String tagName:tags) {
                Tag tag = new Tag(tagName);
                tag.setPrefix(mySQL.getString(tagName, "PREFIX"));
                tag.setPrefix(mySQL.getString(tagName, "SUFFIX"));
                harimeltTags.getTagManager().add(tag);
            }
            mySQL.closeConnection();
        }
    }

}
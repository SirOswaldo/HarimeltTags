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

package net.harimelt.tags.tag;

import net.harimelt.tags.storage.Storage;
import net.harimelt.tags.tasks.DeleteTagTask;
import net.harimelt.tags.tasks.LoadAllTagsTask;
import net.harimelt.tags.tasks.SaveTagTask;
import net.harimelt.tags.HarimeltTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TagManager {

    private final HarimeltTags harimeltTags;
    private final HashMap<String, Tag> tags = new HashMap<>();

    public TagManager(HarimeltTags harimeltTags) {
        this.harimeltTags = harimeltTags;
    }

    public void loadAll() {
        LoadAllTagsTask loadAllTagsTask = new LoadAllTagsTask(harimeltTags);
        loadAllTagsTask.startScheduler();
    }

    public void load(String name) {
        Storage storage = harimeltTags.getStorage();
        Tag tag = new Tag(name);
        tag.setPrefix(storage.getTagPrefix(name));
        tag.setSuffix(storage.getTagSuffix(name));
        tags.put(name, tag);
    }

    public void unloadAll() {
        tags.clear();
    }

    public void unload(String name) { tags.remove(name); }

    public void save(String name) {
        Storage storage = harimeltTags.getStorage();
        Tag tag = tags.get(name);
        storage.setTagPrefix(name, tag.getPrefix());
        storage.setTagSuffix(name, tag.getSuffix());
    }

    public void delete(String name) {
        Storage storage = harimeltTags.getStorage();
        storage.deleteTag(name);
        tags.remove(name);
    }

    public void rename(String name, String newName) {
        Tag tag = tags.get(name);
        Tag newTag = new Tag(newName);
        newTag.setPrefix(tag.getPrefix());
        newTag.setSuffix(tag.getSuffix());
        DeleteTagTask deleteTagTask = new DeleteTagTask(harimeltTags, name);
        deleteTagTask.startScheduler();
        add(newTag);
        SaveTagTask saveTagTask = new SaveTagTask(harimeltTags, newName);
        saveTagTask.startScheduler();
    }

    public void add(Tag tag) {
        tags.put(tag.getName(), tag);
    }

    public boolean exist(String name) {
        return tags.containsKey(name);
    }

    public Tag get(String name) {
        return tags.get(name);
    }

    public List<String> getNames() {
        return new ArrayList<>(tags.keySet());
    }

}

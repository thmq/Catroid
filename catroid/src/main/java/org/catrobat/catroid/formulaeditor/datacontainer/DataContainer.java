/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2018 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.formulaeditor.datacontainer;

import android.content.Context;

import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.adapter.DataAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataContainer extends BaseDataContainer {

	private transient List<UserVariable> projectVariables = new ArrayList<>();
	private transient List<UserList> projectLists = new ArrayList<>();

	public DataContainer() {
		spriteVariables = new HashMap<>();
		spriteListOfLists = new HashMap<>();
	}

	public DataContainer(Project project) {
		spriteVariables = new HashMap<>();
		spriteListOfLists = new HashMap<>();

		projectVariables = project.getProjectVariables();
		projectLists = project.getProjectLists();
	}

	public DataAdapter createDataAdapter(Context context, Sprite sprite) {
		return null;
	}

	public DataAdapter createDataAdapter(Context context, UserBrick userBrick, Sprite sprite) {
		return null;
	}

	public boolean containsProjectVariable(String name) {
		for (UserVariable var : projectVariables) {
			if (var.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsVariableInAnySprite(String name) {
		for (Sprite sprite : spriteVariables.keySet()) {
			if (containsSpriteVariable(sprite, name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsSpriteVariable(Sprite sprite, String name) {
		return getUserVariable(sprite, name) != null;
	}

	public boolean containsProjectList(String name) {
		for (UserList var : projectLists) {
			if (var.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsListInAnySprite(String name) {
		for (Sprite sprite : spriteListOfLists.keySet()) {
			if (containsSpriteList(sprite, name)) {
				return true;
			}
		}

		return false;
	}

	public boolean containsSpriteList(Sprite sprite, String name) {
		return getUserList(sprite, name) != null;
	}

	public UserVariable getUserVariable(Sprite sprite, String name) {
		if (spriteVariables.containsKey(sprite)) {
			for (UserVariable var : spriteVariables.get(sprite)) {
				if (var.getName().equals(name)) {
					return var;
				}
			}
		}

		for (UserVariable var : projectVariables) {
			if (var.getName().equals(name)) {
				return var;
			}
		}

		return null;
	}

	public UserVariable getUserVariable(Sprite sprite, String name, UserBrick userBrick) {
		if (userBrickVariables.containsKey(userBrick)) {
			for (UserVariable var : userBrickVariables.get(userBrick)) {
				if (var.getName().equals(name)) {
					return var;
				}
			}
		}
		return getUserVariable(sprite, name);
	}

	public List<UserVariable> getSpriteVariables(Sprite sprite) {
		List<UserVariable> vars = new ArrayList<>();

		if (spriteVariables.containsKey(sprite)) {
			vars.addAll(spriteVariables.get(sprite));
		}
		return vars;
	}

	public UserList getUserList(Sprite sprite, String name) {
		if (spriteListOfLists.containsKey(sprite)) {
			for (UserList list : spriteListOfLists.get(sprite)) {
				if (list.getName().equals(name)) {
					return list;
				}
			}
		}
		for (UserList list : projectLists) {
			if (list.getName().equals(name)) {
				return list;
			}
		}
		return null;
	}

	public List<UserList> getSpriteLists(Sprite sprite) {
		List<UserList> lists = new ArrayList<>();
		if (spriteListOfLists.containsKey(sprite)) {
			lists.addAll(spriteListOfLists.get(sprite));
		}
		return lists;
	}

	public List<UserVariable> getUserBrickVariables(UserBrick userBrick) {
		if (userBrickVariables.containsKey(userBrick)) {
			return userBrickVariables.get(userBrick);
		}
		return null;
	}

	public UserVariable addSpriteUserVariable(Sprite sprite, String name) {
		UserVariable var = new UserVariable(name);

		if (spriteVariables.containsKey(sprite)) {
			spriteVariables.get(sprite).add(var);
		} else {
			spriteVariables.put(sprite, Collections.singletonList(var));
		}

		return var;
	}

	public UserVariable addProjectUserVariable(String name) {
		UserVariable var = new UserVariable(name);
		projectVariables.add(var);
		return var;
	}

	public UserList addSpriteUserList(Sprite sprite, String name) {
		UserList list = new UserList(name);

		if (spriteListOfLists.containsKey(sprite)) {
			spriteListOfLists.get(sprite).add(list);
		} else {
			spriteListOfLists.put(sprite, Collections.singletonList(list));
		}

		return list;
	}

	public UserList addProjectUserList(String name) {
		UserList list = new UserList(name);
		projectLists.add(list);
		return list;
	}

	public void removeAllDataObjectsOfClones() {
		for (Iterator<Map.Entry<Sprite, List<UserVariable>>> iterator = spriteVariables.entrySet().iterator();
				iterator.hasNext(); ) {
			if (iterator.next().getKey().isClone()) {
				iterator.remove();
			}
		}

		for (Iterator<Map.Entry<Sprite, List<UserList>>> iterator = spriteListOfLists.entrySet().iterator();
				iterator.hasNext(); ) {
			if (iterator.next().getKey().isClone()) {
				iterator.remove();
			}
		}
	}

	public void removeAllDataObjects(Sprite sprite) {
		spriteVariables.remove(sprite);
		spriteListOfLists.remove(sprite);

		for (UserBrick userBrick : sprite.getUserBrickList()) {
			userBrickVariables.remove(userBrick);
		}
	}

	public void resetAllDataObjects() {
		for (UserVariable var : projectVariables) {
			var.setValue(0.0);
		}

		for (UserList list : projectLists) {
			list.getList().clear();
		}

		for (List<UserVariable> vars : spriteVariables.values()) {
			for (UserVariable var : vars) {
				var.setValue(0.0);
			}
		}

		for (List<UserList> lists : spriteListOfLists.values()) {
			for (UserList var : lists) {
				var.getList().clear();
			}
		}
	}

	public void setProjectUserDataObjects(Project project) {
		projectVariables = project.getProjectVariables();
		projectLists = project.getProjectLists();
	}

	public void setUserBrickVariables(UserBrick key, List<UserVariable> userVariables) {
		userBrickVariables.put(key, userVariables);
	}

	public Map<Sprite, List<UserVariable>> getSpriteVariableMap() {
		return spriteVariables;
	}

	public List<UserVariable> getProjectVariables() {
		return projectVariables;
	}

	public List<UserList> getProjectLists() {
		return projectLists;
	}
}

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataContainer implements Serializable {

	private static final long serialVersionUID = 1L;

//	@XStreamAlias("objectVariableList")
//	private Map<Sprite, List<UserVariable>> spriteVariableMap = new HashMap<>();
//
//	@XStreamAlias("objectListOfList")
//	private Map<Sprite, List<UserList>> spriteListMap = new HashMap<>();
//
//	@XStreamAlias("userBrickVariableList")
//	private Map<UserBrick, List<UserVariable>> userBrickVariableMap = new HashMap<>();

	private transient UserDataMap<Sprite, UserVariable> variableContainer = new UserDataMap<>(spriteVariableMap);
	private transient UserDataMap<Sprite, UserList> listContainer = new UserDataMap<>(spriteListMap);
	private transient UserDataMap<UserBrick, UserVariable> userBrickVariableContainer = new UserDataMap<>(userBrickVariableMap);

	private transient UserDataArrayList<UserVariable> projectVariables = new UserDataArrayList<>(new ArrayList<UserVariable>());
	private transient UserDataArrayList<UserList> projectLists = new UserDataArrayList<>(new ArrayList<UserList>());

	public DataContainer() {
	}

	public DataContainer(Project project) {
		projectVariables = new UserDataArrayList<>(project.getProjectVariables());
		projectLists = new UserDataArrayList<>(project.getProjectLists());
	}

	public List<UserVariable> getGlobalVariables() {
		return projectVariables.getAll();
	}

	public List<UserList> getGlobalLists() {
		return projectLists.getAll();
	}

	public UserVariable getLocalVariable(Sprite sprite, String name) {
		return variableContainer.getUserData(sprite, name);
	}

	public List<UserVariable> getLocalVariables(Sprite sprite) {
		return variableContainer.get(sprite);
	}

	public List<UserList> getLocalLists(Sprite sprite) {
		return listContainer.get(sprite);
	}

	public UserList getLocalList(Sprite sprite, String name) {
		return listContainer.getUserData(sprite, name);
	}

	public UserVariable getLocalVariable(UserBrick userBrick, String name) {
		return userBrickVariableContainer.getUserData(userBrick, name);
	}

	public List<UserVariable> getUserBrickVariables(UserBrick userBrick) {
		return userBrickVariableContainer.get(userBrick);
	}

	public UserVariable addGlobalVariable(String name) {
		return null;
	}

	public UserList addGlobalList(String name) {
		return null;
	}

	public UserVariable addLocalVariable(Sprite sprite, String name) {
		return null;
	}

	public UserList addLocalList(Sprite sprite, String name) {
		return null;
	}

	public void removeAllDataObjectsOfClones() {
		for (Iterator<Map.Entry<Sprite, List<UserVariable>>> iterator = spriteVariableMap.entrySet().iterator();
				iterator.hasNext(); ) {
			if (iterator.next().getKey().isClone()) {
				iterator.remove();
			}
		}

		for (Iterator<Map.Entry<Sprite, List<UserList>>> iterator = spriteListMap.entrySet().iterator();
				iterator.hasNext(); ) {
			if (iterator.next().getKey().isClone()) {
				iterator.remove();
			}
		}
	}

	public void removeAllDataObjects(Sprite sprite) {
		spriteVariableMap.remove(sprite);
		spriteListMap.remove(sprite);

		for (UserBrick userBrick : sprite.getUserBrickList()) {
			userBrickVariableMap.remove(userBrick);
		}
	}

	public void resetAllDataObjects() {
		for (UserVariable var : projectVariables.getAll()) {
			var.setValue(0.0);
		}

		for (UserList list : projectLists.getAll()) {
			list.getList().clear();
		}

		for (List<UserVariable> vars : spriteVariableMap.values()) {
			for (UserVariable var : vars) {
				var.setValue(0.0);
			}
		}

		for (List<UserList> lists : spriteListMap.values()) {
			for (UserList var : lists) {
				var.getList().clear();
			}
		}
	}

	public void setProjectUserDataObjects(Project project) {
		projectVariables = new UserDataArrayList<>(project.getProjectVariables());
		projectLists = new UserDataArrayList<>(project.getProjectLists());
	}

	public void setUserBrickVariables(UserBrick key, List<UserVariable> userVariables) {
		userBrickVariableMap.put(key, userVariables);
	}

	public DataAdapter createDataAdapter(Context context, Sprite sprite) {
		return null;
	}

	public DataAdapter createDataAdapter(Context context, UserBrick userBrick, Sprite sprite) {
		return null;
	}
}

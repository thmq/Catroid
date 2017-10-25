/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
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

package org.catrobat.catroid.gui.fragment;

import android.content.Intent;
import android.util.Log;

import org.catrobat.catroid.R;
import org.catrobat.catroid.data.ProjectInfo;
import org.catrobat.catroid.data.SceneInfo;
import org.catrobat.catroid.gui.activity.SceneListActivity;
import org.catrobat.catroid.gui.adapter.RecyclerViewAdapter;
import org.catrobat.catroid.gui.dialog.RenameItemDialog;
import org.catrobat.catroid.projecthandler.projectcreators.DefaultProjectCreator;
import org.catrobat.catroid.projecthandler.ProjectHolder;
import org.catrobat.catroid.projecthandler.ProjectListDeserializerTask;
import org.catrobat.catroid.projecthandler.DeserializerTask;
import org.catrobat.catroid.projecthandler.ProjectCreatorTask;
import org.catrobat.catroid.storage.DirectoryPathInfo;
import org.catrobat.catroid.storage.StorageManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectListFragment extends RecyclerViewListFragment<ProjectInfo> implements
		ProjectListDeserializerTask.ProjectListDeserializerListener,
		DeserializerTask.DeserializerListener,
		ProjectCreatorTask.ProjectCreatorListener {

	public static final String TAG = ProjectListFragment.class.getSimpleName();

	@Override
	protected void createAdapter() {
		ProjectListDeserializerTask loader = new ProjectListDeserializerTask(this);
		loader.execute();
	}

	@Override
	public void onDeserializationComplete(ArrayList<ProjectInfo> projects) {
		adapter = new RecyclerViewAdapter<ProjectInfo>(projects, this, this) {

			@Override
			public void updateProject() {
			}
		};

		onAdapterReady();
	}

	@Override
	public void onDeserializationComplete(ProjectInfo project) {
		ProjectHolder.getInstance().setCurrentProject(project);
		Intent intent = new Intent(getActivity(), SceneListActivity.class);
		startActivity(intent);
	}

	@Override
	public void onCreationComplete(ProjectInfo project) {
		onItemClick(project);
	}

	@Override
	protected Class getItemType() {
		return ProjectInfo.class;
	}

	@Override
	protected DirectoryPathInfo getCurrentDirectory() {
		return StorageManager.getProjectsDirectory();
	}

	@Override
	public void addItem(String name) {
		setProgressBarVisibility(true);
		ProjectCreatorTask task = new ProjectCreatorTask(new DefaultProjectCreator(), this, getContext());
		task.execute(name);
	}

	@Override
	public void onItemClick(ProjectInfo item) {
		setProgressBarVisibility(true);
		DeserializerTask task = new DeserializerTask(this);
		task.execute(item.getName());
	}

	@Override
	protected void copyItems(List<ProjectInfo> items) {
		actionMode.finish();
		try {
			for (ProjectInfo project : items) {
				ProjectInfo clone = new ProjectInfo(getUniqueItemName(project.getName()));

				for (SceneInfo scene : project.getScenes()) {
					clone.getScenes().add(scene.clone());
				}

				clone.copyResourcesToDirectory(clone.getDirectoryPathInfo());
				ProjectHolder.getInstance().serialize(clone);

				adapter.add(clone);
			}
		} catch (IOException e) {
			Log.e(TAG, "Cannot create project folder: " + Log.getStackTraceString(e));
		} catch (CloneNotSupportedException e) {
			Log.e(TAG, "Cannot clone scene in project" + Log.getStackTraceString(e));
		}
	}

	@Override
	protected void showRenameDialog(String name) {
		RenameItemDialog dialog = new RenameItemDialog(R.string.rename_project, name, this);
		dialog.show(getFragmentManager(), RenameItemDialog.TAG);
	}
}

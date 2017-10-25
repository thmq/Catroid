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

package org.catrobat.catroid.projecthandler;

import android.content.Context;
import android.os.AsyncTask;

import org.catrobat.catroid.R;
import org.catrobat.catroid.data.LookInfo;
import org.catrobat.catroid.data.ProjectInfo;
import org.catrobat.catroid.data.SceneInfo;
import org.catrobat.catroid.data.SoundInfo;
import org.catrobat.catroid.data.SpriteInfo;
import org.catrobat.catroid.data.brick.PlaceAtBrick;
import org.catrobat.catroid.data.brick.SetLookBrick;
import org.catrobat.catroid.data.brick.SetXBrick;
import org.catrobat.catroid.data.brick.WhenStartedBrick;
import org.catrobat.catroid.formula.Formula;
import org.catrobat.catroid.storage.StorageManager;

import java.io.IOException;

public class ProjectCreatorTask extends AsyncTask<String, Void, ProjectInfo> {

	private ProjectCreator projectCreator;
	private ProjectCreatorListener listener;
	private Context context;

	public ProjectCreatorTask(ProjectCreator projectCreator, ProjectCreatorListener listener, Context context) {
		this.projectCreator = projectCreator;
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected ProjectInfo doInBackground(String... strings) {
		try {
			ProjectInfo project = projectCreator.createProject(strings[0], context);
			ProjectHolder.getInstance().serialize(project);
			return project;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ProjectInfo(strings[0]);
	}

	@Override
	protected void onPostExecute(ProjectInfo project) {
		super.onPostExecute(project);
		listener.onCreationComplete(project);
	}

	public interface ProjectCreator {

		ProjectInfo createProject(String name, Context context) throws IOException;
	}

	public interface ProjectCreatorListener {

		void onCreationComplete(ProjectInfo project);
	}
}

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

package org.catrobat.catroid.projecthandler.projectcreators;

import android.content.Context;

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
import org.catrobat.catroid.projecthandler.ProjectCreatorTask;
import org.catrobat.catroid.storage.StorageManager;

import java.io.IOException;

public final class DefaultProjectCreator implements ProjectCreatorTask.ProjectCreator {

	public static final String TAG = DefaultProjectCreator.class.getSimpleName();

	public ProjectInfo createProject(String name, Context context) throws IOException {
		ProjectInfo project = new ProjectInfo(name);

		SceneInfo scene0 = new SceneInfo("Scene 0", project.getDirectoryPathInfo());
		SceneInfo scene1 = new SceneInfo("Scene 1", project.getDirectoryPathInfo());

		SpriteInfo background = new SpriteInfo("Background", project.getDirectoryPathInfo());
		SpriteInfo bird = new SpriteInfo("Bird", project.getDirectoryPathInfo());
		SpriteInfo cloud0 = new SpriteInfo("Clouds 1", project.getDirectoryPathInfo());
		SpriteInfo cloud1 = new SpriteInfo("Clouds 2", project.getDirectoryPathInfo());

		LookInfo look0 = new LookInfo("Background", StorageManager.saveDrawableToSDCard(R.drawable
				.default_project_background_portrait, project.getDirectoryPathInfo(), context));
		LookInfo look1 = new LookInfo("Bird wings up", StorageManager.saveDrawableToSDCard(R.drawable
				.default_project_bird_wing_up, project.getDirectoryPathInfo(), context));
		LookInfo look2 = new LookInfo("Bird wings down", StorageManager.saveDrawableToSDCard(R.drawable
				.default_project_bird_wing_down, project.getDirectoryPathInfo(), context));
		LookInfo look3 = new LookInfo("Cloud 1", StorageManager.saveDrawableToSDCard(R.drawable
				.default_project_clouds_portrait, project.getDirectoryPathInfo(), context));
		LookInfo look4 = new LookInfo("Cloud 2", StorageManager.saveDrawableToSDCard(R.drawable
				.default_project_clouds_portrait, project.getDirectoryPathInfo(), context));

		bird.getBricks().add(new WhenStartedBrick());
		bird.getBricks().add(new SetXBrick(new Formula(100)));
		bird.getBricks().add(new SetLookBrick(look1));
		bird.getBricks().add(new PlaceAtBrick(new Formula(50), new Formula(60)));

		background.getLooks().add(look0);

		bird.getLooks().add(look1);
		bird.getLooks().add(look2);

		bird.getSounds().add(new SoundInfo("Tweet 1", StorageManager.saveSoundResourceToSDCard(R.raw
						.default_project_tweet_1, project.getDirectoryPathInfo(), context)));
		bird.getSounds().add(new SoundInfo("Tweet 2", StorageManager.saveSoundResourceToSDCard(R.raw
						.default_project_tweet_2, project.getDirectoryPathInfo(), context)));

		cloud0.getLooks().add(look3);
		cloud1.getLooks().add(look4);

		scene0.getSprites().add(background);
		scene0.getSprites().add(bird);
		scene0.getSprites().add(cloud0);
		scene0.getSprites().add(cloud1);

		project.getScenes().add(scene0);
		project.getScenes().add(scene1);

		return project;
	}
}

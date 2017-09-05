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

package org.catrobat.catroid.test.copypaste;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.copypaste.Clipboard;
import org.catrobat.catroid.copypaste.ClipboardHandler;
import org.catrobat.catroid.data.LookInfo;
import org.catrobat.catroid.data.ProjectInfo;
import org.catrobat.catroid.data.SceneInfo;
import org.catrobat.catroid.data.SoundInfo;
import org.catrobat.catroid.data.SpriteInfo;
import org.catrobat.catroid.storage.StorageManager;
import org.catrobat.catroid.test.StorageUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class ClipboardTest {

	private ProjectInfo project0;
	private ProjectInfo project1;

	private SpriteInfo sprite0;
	private SpriteInfo sprite1;

	@Before
	public void setUp() throws Exception {
		StorageUtil.createTestDirectory();

		project0 = new ProjectInfo("Project 0");
		project1 = new ProjectInfo("Project 1");

		SceneInfo scene0 = new SceneInfo("Scene 0", project0.getDirectoryPathInfo());
		sprite0 = new SpriteInfo("Sprite 0", project0.getDirectoryPathInfo());

		SceneInfo scene1 = new SceneInfo("Scene 1", project1.getDirectoryPathInfo());
		sprite1 = new SpriteInfo("Sprite 1", project1.getDirectoryPathInfo());

		LookInfo look0 = new LookInfo("Look 0", StorageManager.saveDrawableToSDCard(org.catrobat.catroid.test
				.R.raw.blue_image, project0.getDirectoryPathInfo(), InstrumentationRegistry.getContext()));
		LookInfo look1 = new LookInfo("Look 1", StorageManager.saveDrawableToSDCard(org.catrobat.catroid.test
				.R.raw.yellow_image, project0.getDirectoryPathInfo(), InstrumentationRegistry.getContext()));
		LookInfo look2 = new LookInfo("Look 2", StorageManager.saveDrawableToSDCard(org.catrobat.catroid.test
				.R.raw.blue_image, project1.getDirectoryPathInfo(), InstrumentationRegistry.getContext()));
		LookInfo look3 = new LookInfo("Look 3", StorageManager.saveDrawableToSDCard(org.catrobat.catroid.test
				.R.raw.yellow_image, project1.getDirectoryPathInfo(), InstrumentationRegistry.getContext()));

		SoundInfo sound0 = new SoundInfo("Sound 0", StorageManager.saveSoundResourceToSDCard(org.catrobat.catroid
				.test.R.raw.longsound, project0.getDirectoryPathInfo(), InstrumentationRegistry.getContext()));

		SoundInfo sound1 = new SoundInfo("Sound 1", StorageManager.saveSoundResourceToSDCard(org.catrobat.catroid
				.test.R.raw.longsound, project0.getDirectoryPathInfo(), InstrumentationRegistry.getContext()));

		sprite0.getLooks().add(look0);
		sprite0.getLooks().add(look1);
		sprite1.getLooks().add(look2);
		sprite1.getLooks().add(look3);

		sprite0.getSounds().add(sound0);
		sprite0.getSounds().add(sound1);

		scene0.getSprites().add(sprite0);
		project0.getScenes().add(scene0);

		scene1.getSprites().add(sprite1);
		project1.getScenes().add(scene1);
	}

	@Test
	public void testCopyLooksBetweenSprites() throws Exception {
		Clipboard<LookInfo> clipboard = new Clipboard<>(LookInfo.class);

		clipboard.addToClipboard(sprite0.getLooks());
		ClipboardHandler.setClipboard(clipboard);

		File clipboardDirectory = new File(ClipboardHandler.getClipboardDirectory().getAbsolutePath());

		assertTrue(StorageUtil.directoryContains(clipboardDirectory, extractLookFileNames(sprite0.getLooks())));

		List<LookInfo> looksFromClipboard = clipboard.getItemsFromClipboard();

		for (LookInfo look : looksFromClipboard) {
			look.copyResourcesToDirectory(sprite1.getDirectoryPathInfo());
			sprite1.getLooks().add(look);
		}

		File sprite1Directory = new File(sprite1.getDirectoryPathInfo().getAbsolutePath());
		assertTrue(StorageUtil.directoryContains(sprite1Directory, extractLookFileNames(sprite1.getLooks())));

		for (LookInfo look : sprite0.getLooks()) {
			look.removeResources();
		}

		assertTrue(StorageUtil.directoryContains(sprite1Directory, extractLookFileNames(sprite1.getLooks())));
	}

	@Test
	public void testCopySoundsBetweenSprites() throws Exception {
		Clipboard<SoundInfo> clipboard = new Clipboard<>(SoundInfo.class);

		clipboard.addToClipboard(sprite0.getSounds());
		ClipboardHandler.setClipboard(clipboard);

		File clipboardDirectory = new File(ClipboardHandler.getClipboardDirectory().getAbsolutePath());

		assertTrue(StorageUtil.directoryContains(clipboardDirectory, extractSoundFileNames(sprite0.getSounds())));

		List<SoundInfo> soundsFromClipboard = clipboard.getItemsFromClipboard();

		for (SoundInfo sound : soundsFromClipboard) {
			sound.copyResourcesToDirectory(sprite1.getDirectoryPathInfo());
			sprite1.getSounds().add(sound);
		}

		File sprite1Directory = new File(sprite1.getDirectoryPathInfo().getAbsolutePath());
		assertTrue(StorageUtil.directoryContains(sprite1Directory, extractSoundFileNames(sprite1.getSounds())));

		for (SoundInfo sound : sprite0.getSounds()) {
			sound.removeResources();
		}

		assertTrue(StorageUtil.directoryContains(sprite1Directory, extractSoundFileNames(sprite1.getSounds())));
	}

	private List<String> extractLookFileNames(List<LookInfo> looks) {
		List<String> lookFileNames = new ArrayList<>();
		for (LookInfo look : looks) {
			lookFileNames.add(look.getFilePathInfo().getRelativePath());
		}

		return lookFileNames;
	}

	private List<String> extractSoundFileNames(List<SoundInfo> sounds) {
		List<String> soundFileNames = new ArrayList<>();
		for (SoundInfo sound : sounds) {
			soundFileNames.add(sound.getFilePathInfo().getRelativePath());
		}

		return soundFileNames;
	}
}

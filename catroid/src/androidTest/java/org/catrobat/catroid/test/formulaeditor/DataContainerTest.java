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
package org.catrobat.catroid.test.formulaeditor;

import android.support.test.runner.AndroidJUnit4;

import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.UserList;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class DataContainerTest {

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void testGetUserVariableFromSpritePrecedence() {
		DataContainer dataContainer = new DataContainer();

		String variableName = "variable0";

		Sprite sprite = new Sprite("sprite");
		UserVariable spriteVar = dataContainer.addSpriteUserVariable(sprite, variableName);

		assertEquals(spriteVar, dataContainer.getUserVariable(sprite, variableName));
	}

	@Test
	public void testGetUserListFromSpritePrecedence() {
		DataContainer dataContainer = new DataContainer();

		String variableName = "list0";

		Sprite sprite = new Sprite("sprite");
		UserList spriteList = dataContainer.addSpriteUserList(sprite, variableName);

		assertEquals(spriteList, dataContainer.getUserList(sprite, variableName));
	}
}

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

package org.catrobat.catroid.content.actions;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;
import com.badlogic.gdx.utils.Array;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.UserBrick;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.formulaeditor.datacontainer.DataContainer;
import org.catrobat.catroid.stage.ShowTextActor;
import org.catrobat.catroid.stage.StageActivity;

import java.util.List;
import java.util.Map;

public class HideTextAction extends TemporalAction {

	private UserVariable variableToHide;

	private Sprite sprite;
	private UserBrick userBrick;

	@Override
	protected void begin() {
		DataContainer dataContainer = ProjectManager.getInstance().getSceneToPlay().getDataContainer();

		List<UserVariable> variableList = dataContainer.getProjectVariables();
		Map<Sprite, List<UserVariable>> spriteVariableMap = dataContainer.getSpriteVariableMap();

		if (StageActivity.stageListener != null) {
			Array<Actor> stageActors = StageActivity.stageListener.getStage().getActors();
			ShowTextActor dummyActor = new ShowTextActor(new UserVariable("dummyActor"), 0, 0, sprite, userBrick);

			for (Actor actor : stageActors) {
				if (actor.getClass().equals(dummyActor.getClass())) {
					ShowTextActor showTextActor = (ShowTextActor) actor;
					if (showTextActor.getVariableNameToCompare().equals(variableToHide.getName())
							&& showTextActor.getSprite().equals(sprite)
							&& (userBrick == null || showTextActor.getUserBrick().equals(userBrick))) {
						actor.remove();
					}
				}
			}
		}

		setVariablesVisible(variableList);

		if (sprite != null) {
			List<UserVariable> spriteVariableList = spriteVariableMap.get(sprite);
			setVariablesVisible(spriteVariableList);
		}
	}

	private void setVariablesVisible(List<UserVariable> variableList) {
		if (variableList == null) {
			return;
		}
		for (UserVariable userVariable : variableList) {
			if (userVariable.getName().equals(variableToHide.getName())) {
				userVariable.setVisible(false);
				break;
			}
		}
	}

	@Override
	protected void update(float percent) {
	}

	public void setVariableToHide(UserVariable userVariable) {
		this.variableToHide = userVariable;
	}

	public void setUserBrick(UserBrick userBrick) {
		this.userBrick = userBrick;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
}

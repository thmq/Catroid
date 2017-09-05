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

package org.catrobat.catroid.data.brick;

import android.content.Intent;
import android.view.View;

import com.badlogic.gdx.scenes.scene2d.Action;

import org.catrobat.catroid.R;
import org.catrobat.catroid.formula.Formula;
import org.catrobat.catroid.gui.activity.FormulaEditorActivity;

class PlaceAtBrick(val setX: Formula, val setY: Formula) : Brick(R.layout.place_at_brick) {

    init {
        brickFields.put(R.id.brick_place_at_x, setX)
		brickFields.put(R.id.brick_place_at_y, setY)
    }

    override fun getAction(): Action {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clone() = PlaceAtBrick(Formula(0.0), Formula(0.0))

    override fun onClick(v: View?) {
        v?.context?.startActivity(Intent(v.context, FormulaEditorActivity::class.java))
    }
}

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

package org.catrobat.catroid.data

import android.graphics.drawable.Drawable
import org.catrobat.catroid.data.brick.Brick
import org.catrobat.catroid.gui.adapter.ListItem
import org.catrobat.catroid.storage.DirectoryPathInfo
import java.io.IOException

class SpriteInfo(override var name: String, var directoryPathInfo: DirectoryPathInfo) : ListItem {

    val looks = ArrayList<LookInfo>()
    val sounds = ArrayList<SoundInfo>()
    val bricks = ArrayList<Brick>()

    override val thumbnail: Drawable
        get() = if (looks.isEmpty()) super.thumbnail else looks[0].thumbnail

    @Throws(CloneNotSupportedException::class)
    override fun clone(): SpriteInfo {
        val clone = SpriteInfo(name, DirectoryPathInfo(directoryPathInfo.parent, directoryPathInfo.relativePath))

        looks.forEach { look -> clone.looks.add(look.clone()) }
        sounds.forEach { sound -> clone.sounds.add(sound.clone()) }
        bricks.forEach { brick -> clone.bricks.add(brick.clone()) }

        return clone
    }

    @Throws(IOException::class)
    override fun copyResourcesToDirectory(directoryPathInfo: DirectoryPathInfo) {
        this.directoryPathInfo = directoryPathInfo

        looks.forEach { look -> look.copyResourcesToDirectory(directoryPathInfo) }
        sounds.forEach { sound -> sound.copyResourcesToDirectory(directoryPathInfo) }
        bricks.forEach { brick -> brick.copyResourcesToDirectory(directoryPathInfo) }
    }

    @Throws(IOException::class)
    override fun removeResources() {
        looks.forEach { look -> look.removeResources() }
        sounds.forEach { sound -> sound.removeResources() }
    }
}
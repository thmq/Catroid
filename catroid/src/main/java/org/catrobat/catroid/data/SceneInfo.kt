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

import org.catrobat.catroid.gui.adapter.ListItem
import org.catrobat.catroid.storage.DirectoryPathInfo
import java.io.IOException
import java.util.*

class SceneInfo(override var name: String, var directoryPathInfo: DirectoryPathInfo) : ListItem {

    val sprites = ArrayList<SpriteInfo>()

    @Throws(CloneNotSupportedException::class)
    override fun clone(): SceneInfo {
        val clone = SceneInfo(name, DirectoryPathInfo(directoryPathInfo.parent, directoryPathInfo.relativePath))

        sprites.forEach { sprite -> clone.sprites.add(sprite.clone()) }
        return clone
    }

    @Throws(IOException::class)
    override fun copyResourcesToDirectory(directoryPathInfo: DirectoryPathInfo) {
        this.directoryPathInfo = directoryPathInfo

        sprites.forEach { sprite -> sprite.copyResourcesToDirectory(directoryPathInfo) }
    }

    @Throws(IOException::class)
    override fun removeResources() {
        for (sprite in sprites) sprite.removeResources()
    }

    fun getSpriteByName(name: String) = sprites.first { it.name == name }
}

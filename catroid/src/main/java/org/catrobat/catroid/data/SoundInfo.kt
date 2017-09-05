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
import org.catrobat.catroid.storage.FilePathInfo
import org.catrobat.catroid.storage.StorageManager
import java.io.IOException

class SoundInfo(override var name: String, var filePathInfo: FilePathInfo) : ListItem {

    @Throws(CloneNotSupportedException::class)
    override fun clone() = SoundInfo(name, FilePathInfo(filePathInfo.parent, filePathInfo.relativePath))

    @Throws(IOException::class)
    override fun copyResourcesToDirectory(directoryPathInfo: DirectoryPathInfo) {
        filePathInfo = StorageManager.copyFile(filePathInfo, directoryPathInfo)
    }

    @Throws(IOException::class)
    override fun removeResources() {
        StorageManager.deleteFile(filePathInfo)
    }
}

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

package org.catrobat.catroid.gui.adapter

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import org.catrobat.catroid.copypaste.ClipboardItem
import java.io.IOException

interface ListItem : ClipboardItem {

    companion object {
        val bitmapConfig = Bitmap.Config.ARGB_8888
        const val thumbnailWidth = 120
        const val thumbnailHeight = 120
    }

    var name: String

	val thumbnail: Drawable
        get() {
            val bitmap = Bitmap.createBitmap(thumbnailWidth, thumbnailHeight, bitmapConfig)
            bitmap.eraseColor(Color.GRAY)
            val thumbnail = RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap)
            thumbnail.isCircular = true

            return thumbnail
        }

    @Throws(IOException::class)
	fun removeResources()
}

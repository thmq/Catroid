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

package org.catrobat.catroid.data;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import org.catrobat.catroid.gui.adapter.ListItem;
import org.catrobat.catroid.storage.DirectoryPathInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SceneInfo implements ListItem {

	private String name;
	private DirectoryPathInfo pathInfo;

	private List<SpriteInfo> sprites = new ArrayList<>();

	private transient RoundedBitmapDrawable thumbnail;

	public SceneInfo(String name, DirectoryPathInfo pathInfo) {
		this.name = name;
		this.pathInfo = pathInfo;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public DirectoryPathInfo getDirectoryInfo() {
		return pathInfo;
	}

	public List<SpriteInfo> getSprites() {
		return sprites;
	}

	@Override
	public void createThumbnail() {
		Bitmap bitmap = Bitmap.createBitmap(THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT, BITMAP_CONFIG);
		bitmap.eraseColor(Color.GRAY);

		thumbnail = RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap);
		thumbnail.setCircular(true);
	}

	@Override
	public Drawable getThumbnail() {
		if (thumbnail == null) {
			createThumbnail();
		}
		return thumbnail;
	}

	@Override
	public SceneInfo clone() throws CloneNotSupportedException {
		SceneInfo clone = new SceneInfo(name, new DirectoryPathInfo(pathInfo.getParent(), pathInfo.getRelativePath()));

		for (SpriteInfo sprite : sprites) {
			clone.addSprite(sprite.clone());
		}

		return clone;
	}

	@Override
	public void copyResourcesToDirectory(DirectoryPathInfo directoryPathInfo) throws IOException {
		pathInfo = directoryPathInfo;

		for (SpriteInfo sprite : sprites) {
			sprite.copyResourcesToDirectory(directoryPathInfo);
		}
	}

	@Override
	public void removeResources() throws IOException {
		for (SpriteInfo sprite : sprites) {
			sprite.removeResources();
		}
	}

	public void addSprite(SpriteInfo sprite) {
		sprites.add(sprite);
	}

	public SpriteInfo getSpriteByName(String name) {
		for (SpriteInfo sprite : sprites) {
			if (sprite.getName().equals(name)) {
				return sprite;
			}
		}

		return null;
	}
}

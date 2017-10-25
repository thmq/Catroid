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

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import org.catrobat.catroid.R
import org.catrobat.catroid.projecthandler.ProjectHolder
import java.util.*
import kotlin.collections.ArrayList


open class RecyclerViewAdapter<T : ListItem>(private val onItemClickListener: OnItemClickListener<T>,
                                             private val selectionListener: SelectionListener)
    : RecyclerView.Adapter<ViewHolder>(), TouchHelperCallback.AdapterInterface {

    constructor(items: ArrayList<T>, onItemClickListener: OnItemClickListener<T>, selectionListener: SelectionListener)
            : this(onItemClickListener, selectionListener) {
        this.items.addAll(items)
    }

    interface OnItemClickListener<in T> {

        fun onItemClick(item: T)
        fun onReorderIconClick(viewHolder: ViewHolder)
    }

    interface SelectionListener {

        fun onSelectionChange(isSelectionActive: Boolean)
    }

    val items = ArrayList<T>()
    private val selectionManager = SelectionManager()
    val selectedItems: ArrayList<T>
        get() = createSelectedItemList()

    private fun createSelectedItemList(): ArrayList<T> {
        val selectedItems = ArrayList<T>()
        selectionManager.selectedPositions.mapTo(selectedItems) { items[it] }
        return selectedItems
    }

    val selectedItemCount
        get() = selectionManager.selectedPositions.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
            ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.recycler_view_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val item = items[position]

        holder?.itemView?.setOnClickListener { _ ->
            if(selectionManager.selectionActive) {
                toggleSelection(holder.adapterPosition, holder)
            } else {
                onItemClickListener.onItemClick(item)
            }
         }

        holder?.itemView?.setOnLongClickListener { _ ->
            toggleSelection(holder.adapterPosition, holder)
            true
        }

        holder?.reorderIcon?.setOnLongClickListener { _ ->
            onItemClickListener.onReorderIconClick(holder)
            true
        }

        holder?.nameView?.text = item.name
        holder?.imageSwitcher?.setImageDrawable(item.thumbnail)

        holder?.updateBackground(selectionManager.isSelected(holder.adapterPosition))
    }

    private fun toggleSelection(itemPosition: Int, holder: ViewHolder) {
        selectionManager.toggle(itemPosition)
        selectionListener.onSelectionChange(selectionManager.selectionActive)
        holder.updateBackground(selectionManager.isSelected(itemPosition))
    }

    override fun onItemMove(from: Int, to: Int): Boolean {
        Collections.swap(items, from, to)
        notifyItemMoved(from, to)
        selectionManager.updatePosition(from, to)
        return true
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun add(item: T) {
        if (selectionManager.selectionActive) {
            throw ArrayIndexOutOfBoundsException("ERROR: Cannot Add or Remove items while in multiSelection.")
        }

        items.add(item)
        notifyDataSetChanged()
        updateProject()
    }

    @Throws(ArrayIndexOutOfBoundsException::class)
    fun remove(item: T) {
        if (selectionManager.selectionActive) {
            throw ArrayIndexOutOfBoundsException("ERROR: Cannot Add or Remove items while in multiSelection.")
        }

        items.remove(item)
        notifyDataSetChanged()
        updateProject()
    }

    fun isItemNameUnique(name: String): Boolean = items.none { it.name == name }

    fun clearSelection() {
        selectionManager.clear()
        notifyDataSetChanged()
        updateProject()
    }

    open fun updateProject() {
         ProjectHolder.getInstance().serialize(ProjectHolder.getInstance().currentProject)
    }

    override fun getItemCount() = items.size
}

/*******************************************************************************
 Look4Sat. Amateur radio satellite tracker and pass predictor.
 Copyright (C) 2019, 2020 Arty Bishop (bishop.arty@gmail.com)

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License along
 with this program; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 ******************************************************************************/

package com.rtbishop.look4sat.repo.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rtbishop.look4sat.data.SatEntry

@Dao
interface EntriesDao {

    @Query("SELECT * FROM entries ORDER BY name ASC")
    fun getEntries(): LiveData<List<SatEntry>>

    @Transaction
    suspend fun updateEntries(entries: List<SatEntry>) {
        clearEntries()
        insertEntries(entries)
    }

    @Query("DELETE FROM entries")
    suspend fun clearEntries()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<SatEntry>)

    @Query("SELECT catNum FROM entries WHERE isSelected = 1")
    suspend fun getEntriesSelection(): List<Int>

    @Transaction
    suspend fun updateEntriesSelection(satIds: List<Int>) {
        clearSelection()
        satIds.forEach { updateSelection(it) }
    }

    @Query("UPDATE entries SET isSelected = 0")
    suspend fun clearSelection()

    @Query("UPDATE entries SET isSelected = 1 WHERE catNum = :satId")
    suspend fun updateSelection(satId: Int)
}

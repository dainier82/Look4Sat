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

package com.rtbishop.look4sat.data

import com.github.amsacode.predict4java.Position
import org.osmdroid.views.overlay.Overlay

data class SelectedSat(
    val pass: SatPass,
    val catNum: Int,
    val name: String,
    val range: Double,
    val altitude: Double,
    val velocity: Double,
    val qthLoc: String,
    val osmPos: Position,
    val coverage: Double,
    val footprint: Overlay,
    val groundTrack: Overlay
)
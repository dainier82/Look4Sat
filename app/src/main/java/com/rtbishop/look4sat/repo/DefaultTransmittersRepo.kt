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

package com.rtbishop.look4sat.repo

import androidx.lifecycle.LiveData
import com.rtbishop.look4sat.data.SatTrans
import com.rtbishop.look4sat.repo.local.TransmittersDao
import com.rtbishop.look4sat.repo.remote.TransmittersApi
import javax.inject.Inject

class DefaultTransmittersRepo @Inject constructor(
    private val transmittersDao: TransmittersDao,
    private val transmittersApi: TransmittersApi
) : TransmittersRepo {

    override fun getTransmittersForSat(satId: Int): LiveData<List<SatTrans>> {
        return transmittersDao.getTransmittersForSat(satId)
    }

    override suspend fun updateTransmitters() {
        transmittersDao.updateTransmitters(transmittersApi.getTransmitters())
    }
}
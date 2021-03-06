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

package com.rtbishop.look4sat.di

import android.content.ContentResolver
import android.content.Context
import com.rtbishop.look4sat.repo.*
import com.rtbishop.look4sat.repo.local.EntriesDao
import com.rtbishop.look4sat.repo.local.SourcesDao
import com.rtbishop.look4sat.repo.local.TransmittersDao
import com.rtbishop.look4sat.repo.remote.TransmittersApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import okhttp3.OkHttpClient

@Module
@InstallIn(ActivityComponent::class)
object RepoModule {

    @Provides
    fun getEntriesRepo(resolver: ContentResolver, client: OkHttpClient, entriesDao: EntriesDao)
            : EntriesRepo {
        return DefaultEntriesRepo(resolver, client, entriesDao)
    }

    @Provides
    fun getSourcesRepo(sourcesDao: SourcesDao): SourcesRepo {
        return DefaultSourcesRepo(sourcesDao)
    }

    @Provides
    fun getTransmittersRepo(transDao: TransmittersDao, transApi: TransmittersApi)
            : TransmittersRepo {
        return DefaultTransmittersRepo(transDao, transApi)
    }

    @Provides
    fun getContentResolver(@ActivityContext context: Context): ContentResolver {
        return context.applicationContext.contentResolver
    }
}

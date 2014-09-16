/*
 * Copyright 2014 Christophe Beyls
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.gulch.linuxday.android.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.Loader;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import it.gulch.linuxday.android.R;
import it.gulch.linuxday.android.db.manager.EventManager;
import it.gulch.linuxday.android.db.manager.impl.EventManagerImpl;
import it.gulch.linuxday.android.enums.DatabaseOrder;
import it.gulch.linuxday.android.loaders.BaseLiveLoader;
import it.gulch.linuxday.android.model.db.Event;

@EFragment
public class NextLiveListFragment extends BaseLiveListFragment
{
	@Bean(EventManagerImpl.class)
	EventManager eventManager;

	@Override
	protected String getEmptyText()
	{
		return getString(R.string.next_empty);
	}

	@Override
	public Loader<List<Event>> onCreateLoader(int id, Bundle args)
	{
		return new NextLiveLoader(getActivity());
	}

	private class NextLiveLoader extends BaseLiveLoader
	{
		private static final int INTERVAL_IN_MINUTES = 30;

		public NextLiveLoader(Context context)
		{
			super(context);
		}

		@Override
		protected List<Event> getObject()
		{
			Calendar minStart = GregorianCalendar.getInstance();
			Calendar maxStart = (Calendar) minStart.clone();
			maxStart.add(Calendar.MINUTE, INTERVAL_IN_MINUTES);

			try {
				return eventManager.search(minStart.getTime(), maxStart.getTime(), null, DatabaseOrder.ASCENDING);
			} catch(SQLException e) {
				return Collections.emptyList();
			}
		}
	}
}

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

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.gulch.linuxday.android.activities.EventDetailsActivity;
import it.gulch.linuxday.android.adapters.EventsAdapter;
import it.gulch.linuxday.android.model.db.Event;

public abstract class BaseLiveListFragment extends ListFragment implements LoaderCallbacks<List<Event>>
{
	private static final int EVENTS_LOADER_ID = 1;

	private List<Event> events;

	private EventsAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		events = new ArrayList<>();

		adapter = new EventsAdapter(getActivity(), events, false);
		setListAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		setEmptyText(getEmptyText());
		setListShown(false);

		getLoaderManager().initLoader(EVENTS_LOADER_ID, null, this);
	}

	protected abstract String getEmptyText();

	@Override
	public void onLoadFinished(Loader<List<Event>> loader, List<Event> data)
	{
		if(data != null) {
			events.clear();
			events.addAll(data);
			adapter.notifyDataSetChanged();
		}

		// The list should now be shown.
		if(isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	@Override
	public void onLoaderReset(Loader<List<Event>> loader)
	{
		events.clear();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id)
	{
		Event event = adapter.getItem(position);
		Intent intent =
			new Intent(getActivity(), EventDetailsActivity.class).putExtra(EventDetailsActivity.EXTRA_EVENT, event);
		startActivity(intent);
	}
}

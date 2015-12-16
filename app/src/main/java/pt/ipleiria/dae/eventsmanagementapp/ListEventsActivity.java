package pt.ipleiria.dae.eventsmanagementapp;

import android.os.Bundle;
import android.app.Activity;
import android.widget.ListView;

public class ListEventsActivity extends Activity {
    private ListView eventsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeListView();
        setContentView(R.layout.list_events_activity);

    }

    private void initializeListView() {
        eventsList = (ListView) findViewById(R.id.eventsListView);

    }

}

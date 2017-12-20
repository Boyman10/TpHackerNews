package com.ocr.test.tphacknews;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.oc.hnapp.android.HNQueryTask;
import com.ocr.test.tphacknews.Model.HN2ArticlesAdapter;
import com.ocr.test.tphacknews.Model.HNArticlesAdapter;

public class MainActivity extends AppCompatActivity {

    private HNQueryTask _task = null;

    private int _page = 0;
//        HNArticlesAdapter adapter = new HNArticlesAdapter();

    private HN2ArticlesAdapter _adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        _adapter = new HN2ArticlesAdapter(this);

        recyclerView.setAdapter(_adapter);

        loadNext();

        // adding progress bar callback function to get notified when list fully loaded :
        final ProgressBar progress = (ProgressBar) findViewById(R.id.progress);
        // we Observe from the adapter :
        _adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                // we remove the progress bar !
                progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _task.cancel(true);
    }

    public void loadNext() {

        if (_task != null && _task.getStatus() != AsyncTask.Status.FINISHED)
            return ;

        _task = new HNQueryTask(_adapter, 80, ++_page);
        _task.execute();


    }
}

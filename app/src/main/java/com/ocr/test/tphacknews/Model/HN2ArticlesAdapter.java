package com.ocr.test.tphacknews.Model;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.oc.hnapp.android.HNArticle;
import com.oc.hnapp.android.HNQueryCallback;
import com.ocr.test.tphacknews.MainActivity;
import com.ocr.test.tphacknews.R;

import java.util.ArrayList;
import java.util.List;

public class HN2ArticlesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements HNQueryCallback {

    private final MainActivity _mainActivity;


    private static final int VIEW_TYPE_ARTICLE = 0;
    private static final int VIEW_TYPE_PROGRESS = 1;

    private final List<HNArticle> _articles = new ArrayList<HNArticle>();

    private boolean _hasMore = false;

    public HN2ArticlesAdapter(MainActivity mainActivity) {
        _mainActivity = mainActivity;
    }


    @Override
    public void onArticlesReceived(List<HNArticle> articles, boolean hasMore) {
        _articles.addAll(articles);
        _hasMore = hasMore;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return _articles.size() + (_hasMore ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < _articles.size())
            return VIEW_TYPE_ARTICLE;
        else
            return VIEW_TYPE_PROGRESS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ARTICLE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
                return new HNArticleViewHolder(view);
            }
            case VIEW_TYPE_PROGRESS: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_progress, parent, false);
                return new ProgressViewHolder(view);
            }
            default:
                throw new IllegalStateException("Unknown type" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HNArticleViewHolder)
            ((HNArticleViewHolder) holder).bind(_articles.get(position));
        else if (holder instanceof ProgressViewHolder) {
            _mainActivity.loadNext();
        }
    }

    public static class HNArticleViewHolder extends RecyclerView.ViewHolder {

        private final TextView _title;

        public HNArticleViewHolder(View view) {
            super(view);
            _title = (TextView) view.findViewById(R.id.title);
        }

        public void bind(HNArticle article) {
            _title.setText(article.title);
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressViewHolder(View itemView) { super(itemView); }
    }
}
package com.example.haggai.lookaround;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
//    String[] strings = {"1", "2", "3", "4", "5", "6", "7"};

    private List<PointOfInterest> list = new ArrayList<PointOfInterest>() ; // just in case ...
    private PocListAdapter listAdapter = new PocListAdapter(list) ; ;

    public ListFragment() {}

    public void setList(List<PointOfInterest> list){
        this.list = list;
        listAdapter.setDataSource(list);
        listAdapter.notifyDataSetChanged();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RecyclerView rv = new RecyclerView(getContext());
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(listAdapter);
        return rv;
    }

    /**
     * A Simple Adapter for the RecyclerView
     */
    public class PocListAdapter extends RecyclerView.Adapter<PocViewHolder> {
        private List<PointOfInterest> dataSource;

        public PocListAdapter(List<PointOfInterest> dataArgs){
            dataSource = dataArgs;
        }

        public void setDataSource(List<PointOfInterest> dataSource) {
            this.dataSource = dataSource;
        }

        @Override
        public PocViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = new TextView(parent.getContext());
            PocViewHolder viewHolder = new PocViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PocViewHolder holder, int position) {
            holder.textView.setText(dataSource.get(position).getName());
        }

        @Override
        public int getItemCount() {
            if(dataSource == null){
                return 0 ;
            }
            return dataSource.size();
        }
    }

    /**
     * A Simple ViewHolder for the RecyclerView
     */
    public static class PocViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public PocViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }
    }
}

package com.example.haggai.lookaround;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

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
            LayoutInflater li = LayoutInflater.from(parent.getContext());
            View view = li.inflate(R.layout.list_item_layout, null);

            PocViewHolder viewHolder = new PocViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(PocViewHolder holder, int position) {
            PointOfInterest poi = dataSource.get(position);
            holder.poiName.setText(poi.getName());
            holder.poiMarker.setVisibility(poi.isSelectedForDisplay() ? View.VISIBLE : View.INVISIBLE);
            String imageUrl = "https://maps.googleapis.com/maps/api/place/photo?";
            imageUrl += "key="+getActivity().getString(R.string.google_maps_key) + "&";
            imageUrl += "photoreference="+poi.getPhotoReference() + "&";
            imageUrl += "maxwidth="+holder.poiPicture.getWidth() + "&";
            imageUrl += "maxheight="+holder.poiPicture.getHeight();

            RequestCreator reCr = Picasso.get().load(imageUrl) ;
            reCr.placeholder(R.drawable.no_image_paceholder);
            reCr.into(holder.poiPicture);
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
        public ImageView  poiPicture;
        public TextView poiName ;
        public ImageView poiMarker ;

        public PocViewHolder(View itemView) {
            super(itemView);
            poiPicture = (ImageView)itemView.findViewById(R.id.poi_picture) ;
            poiMarker = (ImageView)itemView.findViewById(R.id.poi_marker) ;
            poiName = (TextView) itemView.findViewById(R.id.poi_name) ;
        }
    }
}

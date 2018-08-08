package com.example.haggai.lookaround;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {

    private List<PointOfInterest> list = new ArrayList<PointOfInterest>() ; // just in case ...
    private PoiListAdapter listAdapter = new PoiListAdapter(list) ; ;

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

    public class PoiListAdapter extends RecyclerView.Adapter<PocViewHolder> {
        private List<PointOfInterest> dataSource;

        public PoiListAdapter(List<PointOfInterest> dataArgs){
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
        public void onBindViewHolder(final PocViewHolder holder, final int position) {
            final PointOfInterest poi = dataSource.get(position);
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

            holder.markImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    poi.setSelectedForDisplay(!poi.isSelectedForDisplay());
                    holder.poiMarker.setVisibility(poi.isSelectedForDisplay() ? View.VISIBLE : View.INVISIBLE);
                    holder.swipeLayout.close();
                }
            });

            holder.trashImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(poi);
                    listAdapter.notifyDataSetChanged();
                }
            });
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
        public ImageButton trashImageButton ;
        public ImageButton markImageButton ;
        public SwipeLayout swipeLayout ;

        public PocViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout)itemView;
            swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
            poiPicture = (ImageView)itemView.findViewById(R.id.poi_picture) ;
            poiMarker = (ImageView)itemView.findViewById(R.id.poi_marker) ;
            poiName = (TextView) itemView.findViewById(R.id.poi_name) ;
            trashImageButton = (ImageButton) itemView.findViewById(R.id.trash_image_button) ;
            markImageButton = (ImageButton) itemView.findViewById(R.id.marker_image_button) ;
        }
    }
}

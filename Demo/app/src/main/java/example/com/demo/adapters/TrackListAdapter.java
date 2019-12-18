package example.com.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import example.com.demo.R;
import example.com.demo.model.AlbumDataModel;

public class TrackListAdapter extends RecyclerView.Adapter<TrackListAdapter.TrackLViewHolder> {
    private Context mContext;
    private ArrayList<AlbumDataModel.Album> data;
    private OnItemClicklistener clicklistener;
    private int parentPos;


    public TrackListAdapter(@NonNull Context context, ArrayList<AlbumDataModel.Album> data, OnItemClicklistener clicklistener, int pos) {
        mContext = context;
        this.data = data;
        this.clicklistener = clicklistener;
        parentPos = pos;

    }

    public void setData(ArrayList<AlbumDataModel.Album> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public TrackListAdapter.TrackLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem;
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(R.layout.item_tracks, parent, false);
        return new TrackListAdapter.TrackLViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackListAdapter.TrackLViewHolder holder, int position) {
        AlbumDataModel.Album item = data.get(position);
        holder.mItemName.setText(item.name);
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    class TrackLViewHolder extends RecyclerView.ViewHolder {

        TextView mItemName;

        public TrackLViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.textViewName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clicklistener.onItemClicklistener(parentPos, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClicklistener {
        public void onItemClicklistener(int parentPos, int childPos);
    }

}

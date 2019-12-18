package example.com.demo.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import example.com.demo.R;
import example.com.demo.model.AlbumDataModel;
import example.com.demo.utils.Constants;
import example.com.demo.utils.StringUtil;

public class DrawerItemCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<AlbumDataModel.Items> data;
    int dataShowType;
    TrackListAdapter.OnItemClicklistener clicklistener;

    public DrawerItemCustomAdapter(@NonNull Context context, ArrayList<AlbumDataModel.Items> data, int dataFrom, TrackListAdapter.OnItemClicklistener clicklistener) {
        mContext = context;
        this.data = data;
        dataShowType = dataFrom;
        this.clicklistener = clicklistener;
    }

    public void setData(ArrayList<AlbumDataModel.Items> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem;
        if (dataShowType == Constants.DRAWER_TYPE) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            listItem = inflater.inflate(R.layout.item_album, parent, false);
            return new DrawerViewHolder(listItem);
        } else {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            listItem = inflater.inflate(R.layout.item_album_home, parent, false);
            return new HomeViewHolder(listItem);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final AlbumDataModel.Items item = data.get(position);
        if (dataShowType == Constants.DRAWER_TYPE) {
            final DrawerViewHolder viewHolder = (DrawerViewHolder) holder;
            viewHolder.mItemName.setText(item.album.name);
            if (item.album.tracks != null && item.album.tracks.items != null && item.album.tracks.items.size() > 0) {
                viewHolder.mTrackRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                TrackListAdapter adapter = new TrackListAdapter(mContext, item.album.tracks.items, clicklistener, position);
                viewHolder.mTrackRecyclerView.setAdapter(adapter);
            } else {
                viewHolder.mTrackRecyclerView.setVisibility(View.GONE);
            }
            viewHolder.mItemName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.album.tracks != null && item.album.tracks.items != null && item.album.tracks.items.size() > 0) {
                        if (viewHolder.mTrackRecyclerView.getVisibility() == View.VISIBLE) {
                            viewHolder.mTrackRecyclerView.setVisibility(View.GONE);
                        } else {
                            viewHolder.mTrackRecyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } else {
            final HomeViewHolder homeViewHolder = (HomeViewHolder) holder;
            homeViewHolder.mAlbumName.setText(item.album.name);
            if (item.album.artists != null && item.album.artists.size() > 0) {
                homeViewHolder.mArtistName.setText("Artist - " + item.album.artists.get(0).name);
            } else {
                homeViewHolder.mArtistName.setText("Artist - Unknown");
            }

            if (item.album.tracks != null && item.album.tracks.items != null) {
                homeViewHolder.mTracksCount.setText("tracks : " + item.album.tracks.items.size());
            } else {
                homeViewHolder.mTracksCount.setText("tracks : " + 0);
            }

            homeViewHolder.mReleaseDate.setText("Release Date - " + item.album.release_date);

            //Using picasso here
            if (StringUtil.isNullOrEmpty(item.album.images.get(0).url)) {
                homeViewHolder.mAlbumIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_launcher_background));
            } else {
                try {
                    Picasso.with(homeViewHolder.mAlbumIcon.getContext())
                            .load(item.album.images.get(0).url)
                            .placeholder(mContext.getDrawable(R.drawable.ic_launcher_background))
                            .error(mContext.getDrawable(R.drawable.ic_launcher_background))
                            .resize(
                                    90, 90
                            )
                            .centerCrop()
                            .onlyScaleDown()
                            .into(homeViewHolder.mAlbumIcon);
                } catch (Exception e) {
                    homeViewHolder.mAlbumIcon.setImageDrawable(mContext.getDrawable(R.drawable.ic_launcher_background));
                }
            }


        }

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class DrawerViewHolder extends RecyclerView.ViewHolder {

        TextView mItemName;
        RecyclerView mTrackRecyclerView;

        public DrawerViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.textViewName);
            mTrackRecyclerView = itemView.findViewById(R.id.track_list_view);
        }
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView mAlbumName, mArtistName, mTracksCount, mReleaseDate;
        ImageView mAlbumIcon;

        public HomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mAlbumName = itemView.findViewById(R.id.album_name);
            mArtistName = itemView.findViewById(R.id.artist_name);
            mTracksCount = itemView.findViewById(R.id.track_count);
            mReleaseDate = itemView.findViewById(R.id.release_date);
            mAlbumIcon = itemView.findViewById(R.id.album_icon);
        }
    }


}

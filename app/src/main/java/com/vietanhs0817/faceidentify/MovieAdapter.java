package com.vietanhs0817.faceidentify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vietanhs0817.faceidentify.model.Actor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YukiNoHara on 10/29/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private List<Actor.Film> mList;
    private Context mContext;
    private OnItemClickListener mListener;

    public MovieAdapter(Context context) {
        this.mContext = context;
        mList = new ArrayList<>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(mContext).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(root);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        Actor.Film film = mList.get(position);
        holder.bind(film, position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<Actor.Film> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    public Actor.Film getItem(int position){
        return mList.get(position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imvPoster;
        private TextView tvTitle;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imvPoster = itemView.findViewById(R.id.imv_poster);
            tvTitle = itemView.findViewById(R.id.tv_title);
            imvPoster.setOnClickListener(this);
        }

        public void bind(Actor.Film film, int position){
            switch (position){
                case 0: {
                    imvPoster.setImageResource(R.drawable.movie_1);
                    break;
                }
                case 1: {
                    imvPoster.setImageResource(R.drawable.movie_2);
                    break;
                }
                case 2: {
                    imvPoster.setImageResource(R.drawable.movie_3);
                    break;
                }
                case 3: {
                    imvPoster.setImageResource(R.drawable.movie_4);
                    break;
                }
                case 4: {
                    imvPoster.setImageResource(R.drawable.movie_5);
                    break;
                }
                case 5: {
                    imvPoster.setImageResource(R.drawable.movie_6);
                    break;
                }
                case 6: {
                    imvPoster.setImageResource(R.drawable.movie_7);
                    break;
                }
                case 7: {
                    imvPoster.setImageResource(R.drawable.movie_8);
                    break;
                }

            }
            tvTitle.setText(film.getTitle());
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(getAdapterPosition());
        }
    }

    interface OnItemClickListener{
        void onClick(int position);
    }
}

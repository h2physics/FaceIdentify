package com.vietanhs0817.faceidentify;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vietanhs0817.faceidentify.model.Actor;
import com.vietanhs0817.faceidentify.network.Callback;
import com.vietanhs0817.faceidentify.network.NetworkManagement;
import com.vietanhs0817.faceidentify.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.OnItemClickListener{
    private static final String LOG_TAG = MovieActivity.class.getSimpleName();

    @BindView(R.id.imv_avatar)
    ImageView imvAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_birthplace)
    TextView tvBirthPlace;
    @BindView(R.id.tv_career)
    TextView tvCareer;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.rv_movie)
    RecyclerView rvMovie;

    private MovieAdapter mAdapter;
    private Actor mActor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        initView();
        initData();
    }

    private void initView(){
        ButterKnife.bind(this);
        mAdapter = new MovieAdapter(this);
        int spacing = getResources().getDimensionPixelSize(R.dimen.margin_grid_element);
        GridItemDecoration itemDecoration = new GridItemDecoration(2, spacing, true, 0);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        rvMovie.setLayoutManager(gridLayoutManager);
        rvMovie.addItemDecoration(itemDecoration);
    }

    private void initData(){
        mAdapter.setOnItemClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra("person") && intent.getStringExtra("person") != null){
            final String personName = intent.getStringExtra("person");
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.show();
            NetworkManagement.getActors(personName, new Callback() {
                @Override
                public void onSuccess(Actor actors) {
                    mActor = actors;
                    Glide.with(MovieActivity.this)
                            .load(mActor.getImage().getPoster())
                            .into(imvAvatar);
                    tvName.setText(mActor.getName());
                    String career = "";
                    for(String s : mActor.getTypes()){
                        career += s + ", ";
                    }
                    tvBirthday.setText(mActor.getBirthday());
                    tvBirthPlace.setText(mActor.getBirthPlace());
                    tvCareer.setText(career.substring(0, career.length() - 2));
                    tvDescription.setText(mActor.getDescription());
                    List<Actor.Film> films = mActor.getFilms().getList();
                    List<Actor.Film> subFilms = films.subList(0, 8);
                    mAdapter.setData(subFilms);
                    Log.e(LOG_TAG, mAdapter.getItemCount() + "");
                    rvMovie.setAdapter(mAdapter);
                    dialog.hide();
                }

                @Override
                public void onFailed() {
                    Toast.makeText(MovieActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    dialog.hide();
                }
            });

            imvAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://en.wikipedia.org/wiki/" + AppUtils.validatePersonName(personName);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

        }

    }

    @Override
    public void onClick(int position) {
        String url = mAdapter.getItem(position).getUrl();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}

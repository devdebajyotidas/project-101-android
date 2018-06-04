package com.example.newproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.responseModels.AllTimelines;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Methods;
import utils.ObjectHolder;
import views.CircleImageView;

public class ProviderHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TimelineAdapter timelineAdapter;

    @BindView(R.id.serviceRecycler)
    RecyclerView serviceRecycler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(
                    ContextCompat.getColor(this, android.R.color.transparent));
        }

        setContentView(R.layout.provider_home);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        timelineAdapter = new TimelineAdapter();
        serviceRecycler.setAdapter(timelineAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ObjectHolder.latestContext = this;
        getTimelines();
    }

    private void getTimelines() {
        Methods.showProgress();
        int accountId = ObjectHolder.loginRes.data.account_id;
        Call<AllTimelines> call = ObjectHolder.apiInterface.getAllTimelines(accountId);
        call.enqueue(new Callback<AllTimelines>() {
            @Override
            public void onResponse(Call<AllTimelines> call, Response<AllTimelines> response) {
                Methods.dismissProgress();
                try{
                    AllTimelines allServiceRes = response.body();
                    if (allServiceRes!= null){
                        if (allServiceRes.success){
                            timelineAdapter.refreshAdapter(allServiceRes.dataList);
                        }else {
                            Methods.showMessage(allServiceRes.message);
                        }
                    }
                }catch (Exception e){
                    Methods.printException(e);
                }
            }
            @Override
            public void onFailure(Call<AllTimelines> call, Throwable t) {
                Methods.dismissProgress();
                Methods.showMessage(t.getMessage());
                call.cancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.provider_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.profile) {
            startActivity(new Intent(ProviderHome.this,ProviderProfile.class));
            overridePendingTransition(R.anim.enterright, R.anim.exitleft);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.MyViewHolder> {

        private List<AllTimelines.Data> mlist;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.provider_home_list, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            AllTimelines.Data data = mlist.get(position);

            if (data.progress.equalsIgnoreCase("Completed")){
                holder.close.setVisibility(View.INVISIBLE);
                holder.status.setText(data.progress+" (Rs. "+data.amount+")");
            }else {
                holder.status.setText(data.progress);
                holder.close.setVisibility(View.VISIBLE);
            }

            holder.serviceName.setText(data.serviceName);
            holder.createDate.setText(getFormatedTimelineDate(data.takenAt));
            holder.providerName.setText(data.name);
            holder.duration.setText(data.duration+" hours");
            holder.providerName.setText(data.name);
            Picasso.get()
                    .load(data.photo)
                    .placeholder(R.mipmap.user)
                    .error(R.mipmap.user)
                    .into(holder.profileImage);
        }

        @Override
        public int getItemCount() {
            if (mlist == null) return 0;
            return mlist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.status)
            TextView status;
            @BindView(R.id.close)
            LinearLayout close;
            @BindView(R.id.serviceName)
            TextView serviceName;
            @BindView(R.id.createDate)
            TextView createDate;
            @BindView(R.id.providerName)
            TextView providerName;
            @BindView(R.id.duration)
            TextView duration;
            @BindView(R.id.profileImage)
            CircleImageView profileImage;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public void refreshAdapter(List<AllTimelines.Data> mlist){
            this.mlist = mlist;
            notifyDataSetChanged();
        }

    }

    private SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat outSDF = new SimpleDateFormat("dd MMM yyyy");

    private String getFormatedTimelineDate(String date){
        try {
            Date inDate = inSDF.parse(date);
            return outSDF.format(inDate);
        }catch (Exception e){
            Methods.printException(e);
            return "";
        }
    }

}

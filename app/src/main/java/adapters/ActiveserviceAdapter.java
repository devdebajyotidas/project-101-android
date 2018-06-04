package adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newproject.R;

import butterknife.ButterKnife;

/**
 * Created by Sudip on 4/27/2018.
 */

public class ActiveserviceAdapter extends RecyclerView.Adapter<ActiveserviceAdapter.MyViewHolder> {

    public ActiveserviceAdapter() {
    }

    @Override
    public ActiveserviceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_listservice, parent, false);
        ActiveserviceAdapter.MyViewHolder holder = new ActiveserviceAdapter.MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ActiveserviceAdapter.MyViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

    }

}
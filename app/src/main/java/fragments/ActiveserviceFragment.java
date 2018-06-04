package fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.newproject.R;

import adapters.ActiveserviceAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sudip on 4/27/2018.
 */

public class ActiveserviceFragment extends Fragment implements View.OnClickListener{
    Activity act;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.addService)
    TextView addService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context != null){
            act = (Activity) context;
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activeservice_screen,container,false);
        ButterKnife.bind(this,view);
        recycler.setAdapter(new ActiveserviceAdapter());
        addService.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addService:
                break;
                default:
                    break;
        }
    }



}
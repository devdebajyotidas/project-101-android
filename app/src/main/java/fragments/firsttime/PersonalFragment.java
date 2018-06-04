package fragments.firsttime;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newproject.R;

/**
 * Created by Sudip on 4/27/2018.
 */

public class PersonalFragment extends Fragment {
    Activity act;
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
        View view = inflater.inflate(R.layout.own_profile,container,false);
        return view;
    }
}

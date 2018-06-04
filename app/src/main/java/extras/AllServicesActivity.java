package extras;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.newproject.R;

import adapters.ActiveserviceAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import fragments.ActiveserviceFragment;
import views.NavigationTabStrip;

/**
 * Created by Sudip on 4/27/2018.
 */

public class AllServicesActivity extends AppCompatActivity{

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allservices_screen);
        ButterKnife.bind(this);
        recycler.setAdapter(new ActiveserviceAdapter());
    }


}

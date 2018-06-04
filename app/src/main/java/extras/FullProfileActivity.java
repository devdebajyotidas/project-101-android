package extras;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.newproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.NavigationTabStrip;

/**
 * Created by Sudip on 5/7/2018.
 */

public class FullProfileActivity  extends AppCompatActivity {
    @BindView(R.id.profileTabs)
    NavigationTabStrip profileTabs;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.cl3)
    ConstraintLayout cl3;
    @BindView(R.id.cl)
    ConstraintLayout cl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        ButterKnife.bind(this);
        inflater = LayoutInflater.from(this);
        cl3.setVisibility(View.GONE);
        linear.setVisibility(View.VISIBLE);
        cl.setVisibility(View.VISIBLE);
        generateDoubleSticky();
      //  viewForFirstTime();
    }

    private void viewForFirstTime() {
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = null;
                LayoutInflater inflater = LayoutInflater.from(FullProfileActivity.this);
                switch (position){
                    case 1:
                        view = inflater.inflate(R.layout.aadhaar_screen,null);
                        break;
                    case 2:
                        view = inflater.inflate(R.layout.pan_screen,null);
                        break;
                    case 3:
                        view = inflater.inflate(R.layout.location_screen,null);
                        break;
                    default:
                        view = inflater.inflate(R.layout.own_profile,null);
                        break;
                }
                if (view != null)
                    container.addView(view);
                return view;
            }
        });
        profileTabs.setViewPager(pager, 0);
    }




    private LayoutInflater inflater;

    private void generateDoubleSticky() {
        for (int i = 0;i<2;i++){
            View header = inflater.inflate(R.layout.sticky_title,null);
            linear.addView(header);
            for (int j =0;j<3;j++){
                View sHeader = inflater.inflate(R.layout.sticky_date,null);
                linear.addView(sHeader);
                for (int k =0;k<2;k++){
                    View item = inflater.inflate(R.layout.sticky_item,null);
                    LinearLayout images = item.findViewById(R.id.images);
                    for (int m=0;m<3;m++){
                        View image = inflater.inflate(R.layout.sticky_imageitem,null);
                        images.addView(image);
                    }

                    linear.addView(item);


                }

            }
        }
    }

}


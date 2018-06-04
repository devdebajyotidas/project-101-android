package extras;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.newproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import views.NavigationTabStrip;

/**
 * Created by Sudip on 4/26/2018.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener,
        PopupMenu.OnMenuItemClickListener{
    @BindView(R.id.profileTabs)
    NavigationTabStrip profileTabs;
    @BindView(R.id.profileMenu)
    LinearLayout profileMenu;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.anchor)
    View anchor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        ButterKnife.bind(this);
        inflater = LayoutInflater.from(this);
        profileMenu.setOnClickListener(this);
       // generateDoubleSticky();
        viewForFirstTime();
    }

    private void viewForFirstTime() {
        pager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
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
                LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
                switch (position){
                    case 1:
                        LinearLayout linearLayout = new LinearLayout(ProfileActivity.this);
                        linearLayout.setOrientation(LinearLayout.VERTICAL);
                        generateDoubleSticky(linearLayout);
                        view = linearLayout;
                      //  view = inflater.inflate(R.layout.aadhaar_screen,null);
                        break;
                    case 2:
                        view = inflater.inflate(R.layout.pan_screen,null);
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

    private void generateDoubleSticky(LinearLayout linear) {
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profileMenu:
                createPopupMenu(anchor);
                break;
                default:
                    break;
        }
    }

    private void createPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(ProfileActivity.this, v);
        popupMenu.setOnMenuItemClickListener(ProfileActivity.this);
        popupMenu.getMenu().add(1,1, 1, "Option1");
        popupMenu.getMenu().add(1,2,2,"Option2");
        popupMenu.getMenu().add(1,3,3,"Option3");
        popupMenu.show();
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {
            case 1:
                Toast.makeText(this, "Option1 Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case 2:
                Toast.makeText(this, "Option2 Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case 3:
                Toast.makeText(this, "Option3 Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

}

package extras;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.newproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sudip on 4/29/2018.
 */

public class ServicesActivity extends AppCompatActivity {
    @BindView(R.id.servicesLinear)
    LinearLayout servicesLinear;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_screen);
        ButterKnife.bind(this);
        servicesLinear.removeAllViews();
        for (int i = 0;i<5;i++){
            View view = LayoutInflater.from(this).inflate(R.layout.list_services,null);
            servicesLinear.addView(view);
        }
    }
}

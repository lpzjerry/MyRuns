package edu.dartmouth.stressmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.util.Log;

import java.util.Objects;

public class BigImgActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_img);

        ImageView imageView = (ImageView) findViewById(R.id.bigImgView);
        Bundle bundle = getIntent().getExtras();
        int grid_id = 0;
        int position = 0;
        if (bundle != null) {
            grid_id = bundle.getInt("grid_id");
            position = bundle.getInt("position");
        }
        int img_id = PSM.getGridById(grid_id+1)[position];
        Drawable drawable = getResources().getDrawable(img_id, null);
        imageView.setImageDrawable(drawable);
    }

    public void onClickButtonCancel(View view) {
        BigImgActivity.this.finish();
    }

    public void onClickButtonSubmit(View view) {
        // TODO write to CSV
        BigImgActivity.this.finish();
    }
}

package edu.dartmouth.stressmeter;

import android.os.Bundle;
import android.util.Log;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.widget.GridView;
import android.os.Vibrator;
import android.os.VibrationEffect;

import edu.dartmouth.stressmeter.ui.home.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    Vibrator vibrator; // = (Vibrator) getSystemService(VIBRATOR_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Util.checkPermissions(this);
        // TODO vibrate
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Log.d("vib", "BEFORE VIBRATE");
        if (vibrator != null) {
            vibrator.vibrate(VibrationEffect.createOneShot(500000, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        Log.d("vib", "AFTER VIBRATE");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration =
                new AppBarConfiguration.Builder(R.id.nav_home, R.id.nav_tools).setDrawerLayout(drawer).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void onClickMoreImages(View view) {
        GridView gridView = findViewById(R.id.grid_view);
        HomeFragment.updateGridImgID();
        gridView.setAdapter(new GridViewAdapter(this, HomeFragment.getGridImgID()));
    }
}

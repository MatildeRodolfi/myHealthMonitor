package com.example.myhealthmonitor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class NewControlActivity extends AppCompatActivity {

    //fragment variable
    protected controlInfoFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        //set correct fragment
        setFragment();

        //display home button in actionBar
        Toolbar toolbar = findViewById(R.id.toolbarNew);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int saveID = R.id.save;

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case saveID:
                currentFragment.save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.sureBack))
                .setPositiveButton(R.string.yes, (dialog, which) -> finish())
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    //set Fragment
    private void setFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        currentFragment = new controlInfoFragment();
        transaction.replace(R.id.new_frame, currentFragment);
        transaction.commit();
    }
}
package com.example.myhealthmonitor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class ModifyAlarmActivity extends AppCompatActivity {

    //fragment variable
    protected alarmInfoFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);
        setFragment();

        //display home button in actionBar
        Toolbar toolbar = findViewById(R.id.toolbarModify);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.modify_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final int deleteID = R.id.delete;
        final int modifyID = R.id.modify;

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case deleteID:
                new AlertDialog.Builder(this)
                        .setMessage(getResources().getString(R.string.sureDelete))
                        .setPositiveButton(R.string.yes, (dialog, which) -> currentFragment.delete())
                        .setNegativeButton(R.string.cancel, null)
                        .show();
                return true;
            case modifyID:
                currentFragment.modify();
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
        currentFragment = new alarmInfoFragment();
        transaction.replace(R.id.modify_frame, currentFragment);
        transaction.commit();
    }
}
package com.example.ktu.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
//public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener,// PocketInterface,
        fragment_default.fragment_defaultInterface, fragment_dolg.fragment_dolgInterface, fragment_report.fragment_ReportInterface,
        fragment_report_buttons.fragment_report_buttonsInterface {

    private int selectedScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            authform();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_inout) {
            setScreen(0);
        } else if (id == R.id.nav_credit) {
            setScreen(1);
        } else if (id == R.id.nav_report) {
            setScreen(2);
        } else if (id == R.id.nav_settings) {
            authform();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_sync) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void authform() {
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);
    }

    private void setScreen(int position) {
        selectedScreen = position;
        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new fragment_default();
                break;
            case 1:
                fragment = new fragment_dolg();
                break;
            case 2:
                fragment = new fragment_report_buttons();
                break;
            case -1: //webview для вывода отчета кагбэ почти на весь экран
                fragment = new fragment_report();
                break;
            default:
                break;
        }

        FragmentManager FM = getFragmentManager();
        FragmentTransaction ft = FM.beginTransaction();
        // Insert the fragment by replacing any existing fragment
        if (fragment != null) {
            ft.replace(R.id.mainFrame, fragment);
        } else {
            ft.remove(fr);
        }
        //if (position == -1 || position == 2) ft.addToBackStack(null);
        ft.commit();

    }
    @Override
    public void atAttache() {
        FillFrame();
    }

    private void FillFrame() {
        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
//        switch (selectedScreen) {
//            case -1:
//                MyPockets.doReport(numberOfReport);
//                break;
//
//            case  2:
//                Button btn_report = (Button) fr.getView().findViewById(R.id.btnReport);
//                btn_report.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
//                        RadioGroup rgrp = (RadioGroup) fr.getView().findViewById(R.id.rgrp);
//                        switch (rgrp.getCheckedRadioButtonId()) {
//                            case R.id.rbtn1:
//                                numberOfReport = 1;
//                                break;
//                            case R.id.rbtn2:
//                                numberOfReport = 2;
//                                break;
//                            case R.id.rbtn3:
//                                numberOfReport = 3;
//                                break;
//                            case R.id.rbtn4:
//                                numberOfReport = 4;
//                                break;
//                        }
//                        setScreen(-1);
//                    }
//                });
//                WebView WVblnc = (WebView) fr.getView().findViewById(R.id.webBalance);
//                WVblnc.loadDataWithBaseURL("", MyPockets.HTMLBalances(), "text/html", "utf8", "");
//                break;
//            default:
//                Spinner sp_action = (Spinner) fr.getView().findViewById(R.id.spinner_action);
//                Spinner sp_purse = (Spinner) fr.getView().findViewById(R.id.spinner_purse);
//                Button btn_ok = (Button) fr.getView().findViewById(R.id.btn_ok);
//
//                sp_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view,
//                                               int position, long id) {
//                        fillDataByAction(selectedScreen * 4 + position);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> arg0) {
//                    }
//                });
//                sp_purse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view,
//                                               int position, long id) {
//                        ShowCash(parent.getSelectedItem().toString());
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> arg0) {
//                    }
//                });
//
//                sp_action.setSelection(MyPockets.getLastAction("Action") % 4);
//
//                btn_ok.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        TakeInToAccount();
//                    }
//                });
//                break;
//        }
    }

}

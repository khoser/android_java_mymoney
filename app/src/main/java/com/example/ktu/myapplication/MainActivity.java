package com.example.ktu.myapplication;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ktu.myapplication.PocketClass.PocketInterface;
import com.example.ktu.myapplication.fragment_default.fragment_defaultInterface;
import com.example.ktu.myapplication.fragment_dolg.fragment_dolgInterface;
import com.example.ktu.myapplication.fragment_report.fragment_ReportInterface;
import com.example.ktu.myapplication.fragment_report_buttons.fragment_report_buttonsInterface;

public class MainActivity extends AppCompatActivity
//public class MainActivity extends Activity
        implements NavigationView.OnNavigationItemSelectedListener, PocketInterface,
        fragment_defaultInterface, fragment_dolgInterface, fragment_ReportInterface,
        fragment_report_buttonsInterface {

    private int selectedScreen;

    private PocketClass MyPockets;

    private int numberOfReport;

    private boolean sincInProgress;

    //private DrawerLayout drawer;

    //private MenuItem syncMenuItem;

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
        });*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        MyPockets = new PocketClass(MainActivity.this, getString(R.string.DIR_SD));

        setScreen(0);

        CheckAuth();
        CheckForExchange();

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

    /*@Override
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
    }*/

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
            setScreen(3);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_sync) {
            if (sincInProgress) {
                return true;
            }
            if (!CheckAuth()) {
                return true;
            }
            sincInProgress = true;
            MyPockets.DoSynchronisation();
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
            case 3:
                fragment = new fragment_settings();
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
    ////////////////////////////////////////// implementations
    @Override
    public void atAttache() {
        FillFrame();
    }

    @Override
    public void ItIsErrorDuringSync(boolean byExeption) {
        if (byExeption) {
            Toast.makeText(getBaseContext(),getString(R.string.hello_world),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(),getString(R.string.SmthGoWrong),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void ItIsProgressTotal(int AsyncTotal) {
        ProgressBar prbr = (ProgressBar) findViewById(R.id.progressBar);
        prbr.setVisibility(View.VISIBLE);
        prbr.setMax(AsyncTotal);
        prbr.setProgress(0);
    }

    @Override
    public void ItIsNewData() {
        ShowCash("");
        sincInProgress = false;
        CheckForExchange();
    }

    @Override
    public void ItIsReport(String data) {
        if (selectedScreen == -1) {
            Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
            WebView WV_report = (WebView) fr.getView().findViewById(R.id.webView);
            LinearLayout pb = (LinearLayout) fr.getView().findViewById(R.id.pb);
            WV_report.loadDataWithBaseURL("",data, "text/html", "utf8","");
            //WV_report.loadUrl(data);
            pb.setVisibility(View.GONE);
            WV_report.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void ItIsProgressNow(int AsyncProgress) {
        ProgressBar prbr = (ProgressBar) findViewById(R.id.progressBar);
        prbr.setProgress(AsyncProgress);
        if (prbr.getProgress() == prbr.getMax()) {
            prbr.setVisibility(View.GONE);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////

    private void FillFrame() {
        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
        switch (selectedScreen) {
            case -1:
                MyPockets.doReport(numberOfReport);
                break;

            case  2:
                Button btn_report = (Button) fr.getView().findViewById(R.id.btnReport);
                btn_report.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
                        RadioGroup rgrp = (RadioGroup) fr.getView().findViewById(R.id.rgrp);
                        switch (rgrp.getCheckedRadioButtonId()) {
                            case R.id.rbtn1:
                                numberOfReport = 1;
                                break;
                            case R.id.rbtn2:
                                numberOfReport = 2;
                                break;
                            case R.id.rbtn3:
                                numberOfReport = 3;
                                break;
                            case R.id.rbtn4:
                                numberOfReport = 4;
                                break;
                        }
                        setScreen(-1);
                    }
                });
                WebView WVblnc = (WebView) fr.getView().findViewById(R.id.webBalance);
                WVblnc.loadDataWithBaseURL("", MyPockets.HTMLBalances(), "text/html", "utf8", "");
                break;
            case  3:
                Button btn_setconnection = (Button) fr.getView().findViewById(R.id.btnSetConnection);
                btn_setconnection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        authform();
                    }
                });
                Button btn_setcolors = (Button) fr.getView().findViewById(R.id.btnSetColors);
                btn_setcolors.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // TODO: 27.01.19: Запилить фрагмент ностроек цветов используемых валют.
//                        тут его открывать.
                    }
                });
                break;
            default:
                Spinner sp_action = (Spinner) fr.getView().findViewById(R.id.spinner_action);
                Spinner sp_purse = (Spinner) fr.getView().findViewById(R.id.spinner_purse);
                Button btn_ok = (Button) fr.getView().findViewById(R.id.btn_ok);

                sp_action.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        fillDataByAction(selectedScreen * 4 + position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });
                sp_purse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        ShowCash(parent.getSelectedItem().toString());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                    }
                });

                sp_action.setSelection(MyPockets.getLastAction("Action") % 4);

                btn_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        TakeInToAccount();
                    }
                });
                break;
        }
    }

    private void ShowCash(String PurseName) {
        if (selectedScreen == 0 || selectedScreen ==1) {
            Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
            TextView tv_cash = (TextView) fr.getView().findViewById(R.id.purse_data);
            if (PurseName.isEmpty()) {
                Spinner sp_purse = (Spinner) fr.getView().findViewById(R.id.spinner_purse);
                PurseName = sp_purse.getSelectedItem().toString();
            }
            tv_cash.setText(String.valueOf(MyPockets.GetBalance(PurseName)));
            if (selectedScreen ==1) {
                TextView tv_credit_data = (TextView) fr.getView().findViewById(R.id.credit_data);
                Spinner sp_Credit = (Spinner) fr.getView().findViewById(R.id.spinner_credit);
                PurseName = sp_Credit.getSelectedItem().toString();
                tv_credit_data.setText(String.valueOf(MyPockets.GetBalance(PurseName)));
            }
        }
    }

    private void CheckForExchange() {
        NavigationView n = (NavigationView) findViewById(R.id.nav_view);
        Menu m = n.getMenu();
        MenuItem syncMenuItem = m.findItem(R.id.nav_sync);
        switch (MyPockets.GetExchangeNeed()) {
            case -1:
//                mLeftDrawerList.setBackgroundColor(Color.CYAN);
//                mLeftDrawerList.setAdapter(DLA_notice);
                n.setBackgroundColor(Color.CYAN);
                break;
            case 0:
//                mLeftDrawerList.setBackgroundColor(Color.BLACK);
                //mLeftDrawerList.setAdapter(DLA_usual);
//                menu_sync.setIcon(R.drawable.ic_menu_send);
                syncMenuItem.setIcon(R.drawable.ic_menu_send);
                break;
            case 1:
//                mLeftDrawerList.setBackgroundColor(Color.BLACK);
                //mLeftDrawerList.setAdapter(DLA_notice);
//                menu_sync.setIcon(getResources().getDrawable(R.drawable.ic_menu_send_red));
                syncMenuItem.setIcon(R.drawable.ic_menu_send_red);
                break;
        }
    }

    public Boolean CheckAuth() {
        //проверить наличие файла
        boolean resul = true;
        if (MyPockets.Form_Header().isEmpty()) {
            resul = false;
            authform();//если файла нет, то выводить форму авторизации
        }
        //если файл есть, ничего не делать.
        return resul;
    }

    private void fillDataByAction(int tmpActionIndex) {
        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
        Spinner sp_purse = (Spinner) fr.getView().findViewById(R.id.spinner_purse);

        sp_purse.setAdapter(MyPockets.getAdapterPurse());

        if (selectedScreen == 0) {
            Spinner sp_item = (Spinner) fr.getView().findViewById(R.id.spinner_item);
            TextView tv_item = (TextView) fr.getView().findViewById(R.id.item_txt);
            TextView tv_num = (TextView) fr.getView().findViewById(R.id.num_txt);

            switch (tmpActionIndex){
                case 0:
                    sp_item.setAdapter(MyPockets.getAdapterItemsIn());
                    sp_purse.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "Purse"));
                    sp_item.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "ItemIn"));
                    tv_item.setText(getString(R.string.itemin));
                    tv_num.setText(getString(R.string.num));
                    break;
                case 1:
                    sp_item.setAdapter(MyPockets.getAdapterItemsOut());
                    sp_purse.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "Purse"));
                    sp_item.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "ItemOut"));
                    tv_item.setText(getString(R.string.itemout));
                    tv_num.setText(getString(R.string.num));
                    break;
                case 2:
                    sp_item.setAdapter(MyPockets.getAdapterPurse());
                    sp_purse.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "PurseOut"));
                    sp_item.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "PurseIn"));
                    tv_item.setText(getString(R.string.purseto));
                    tv_num.setText(getString(R.string.num));
                    break;
                case 3:
                    sp_item.setAdapter(MyPockets.getAdapterPurse());
                    sp_purse.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "PurseOut"));
                    sp_item.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "PurseIn"));
                    tv_item.setText(getString(R.string.purseto));
                    tv_num.setText(getString(R.string.sumin));
                    break;
            }
        } else {
            Spinner sp_credit = (Spinner) fr.getView().findViewById(R.id.spinner_credit);
            Spinner sp_contact = (Spinner) fr.getView().findViewById(R.id.spinner_contact);
            LinearLayout llNewCredit = (LinearLayout) fr.getView().findViewById(R.id.layoutNewCredit);
            LinearLayout llOldContact = (LinearLayout) fr.getView().findViewById(R.id.layoutOldContact);
            LinearLayout llSumPercent = (LinearLayout) fr.getView().findViewById(R.id.layoutSumPercent);
            LinearLayout llOtherSum = (LinearLayout) fr.getView().findViewById(R.id.layoutOtherSum);
            EditText et_Contact = (EditText) fr.getView().findViewById(R.id.contact);

            sp_credit.setAdapter(MyPockets.getAdapterCredits());
            sp_contact.setAdapter(MyPockets.getAdapterContacts());

            sp_credit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    onSelectCredit(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });
            sp_contact.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int position, long id) {
                    onSelectContact(parent.getSelectedItem().toString());
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

            llNewCredit.setVisibility(View.GONE);
            et_Contact.setEnabled(false);

            try {
                sp_purse.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "Purse"));
            } catch (Exception e) { sp_purse.setSelection(0); }
            try {
                sp_credit.setSelection(MyPockets.getLastAction(String.valueOf(tmpActionIndex), "Credit"));
            } catch (Exception e) { sp_credit.setSelection(0); }

            llOldContact.setVisibility(View.GONE);

            switch (tmpActionIndex) {
                case 4: // МыВернулиДолг
                    llSumPercent.setVisibility(View.VISIBLE);
                    llOtherSum.setVisibility(View.VISIBLE);
                    break;
                case 5: // МыВзялиВДолг
                    llSumPercent.setVisibility(View.GONE);
                    llOtherSum.setVisibility(View.VISIBLE);
                    break;
                case 6: // МыДалиВДолг
                    llSumPercent.setVisibility(View.GONE);
                    llOtherSum.setVisibility(View.VISIBLE);
                    break;
                case 7:// НамВернулиДолг
                    llSumPercent.setVisibility(View.VISIBLE);
                    llOtherSum.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private void TakeInToAccount() {
        if (selectedScreen == 0) { //главный экран
            Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
            Spinner sp_action = (Spinner) fr.getView().findViewById(R.id.spinner_action);
            Spinner sp_purse = (Spinner) fr.getView().findViewById(R.id.spinner_purse);
            Spinner sp_item = (Spinner) fr.getView().findViewById(R.id.spinner_item);
            EditText et_sum = (EditText) fr.getView().findViewById(R.id.sum);
            EditText et_num = (EditText) fr.getView().findViewById(R.id.num);
            EditText et_comment = (EditText) fr.getView().findViewById(R.id.comment);

            int ActionIndex = sp_action.getSelectedItemPosition();
            String sPurse = sp_purse.getSelectedItem().toString();
            String sItem = sp_item.getSelectedItem().toString();
            String ssum = et_sum.getText().toString();
            String snum = et_num.getText().toString();
            String coment = et_comment.getText().toString();

            boolean err = false;

            switch (ActionIndex) {
                case 0: // доход
                    err = sPurse.isEmpty() || sItem.isEmpty() || ssum.isEmpty();
                    break;
                case 1: // расход
                    err = sPurse.isEmpty() || sItem.isEmpty() || ssum.isEmpty();
                    break;
                case 2: // перемещение
                    err = sPurse.isEmpty() || sItem.isEmpty() || ssum.isEmpty();
                    break;
                case 3: // обмен валюты
                    err = sPurse.isEmpty() || sItem.isEmpty() || ssum.isEmpty() || snum.isEmpty();
                    break;
            }

            if (err) {
                Toast.makeText(getBaseContext(),getString(R.string.NotAllFieldsFilled),Toast.LENGTH_SHORT).show();
                return;
            }
            double sum = 0;
            if (!ssum.isEmpty()) sum = Double.parseDouble(ssum);
            double num = 0;
            if (!snum.isEmpty()) num = Double.parseDouble(snum);

            MyPockets.DoAction(ActionIndex,sPurse,sum,sItem,sItem,num,coment,0,"");

            et_sum.setText("");
            et_num.setText("");
            et_comment.setText("");

            Toast.makeText(getBaseContext(), getString(R.string.Taken)+ " " + ssum, Toast.LENGTH_SHORT).show();

            ShowCash("");
            CheckForExchange();
        }

        if (selectedScreen == 1) { // экран долги
            Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
            Spinner sp_action = (Spinner) fr.getView().findViewById(R.id.spinner_action);
            Spinner sp_purse = (Spinner) fr.getView().findViewById(R.id.spinner_purse);
            Spinner sp_credit = (Spinner) fr.getView().findViewById(R.id.spinner_credit);
            Spinner sp_oldcontact = (Spinner) fr.getView().findViewById(R.id.spinner_contact);
            EditText et_newcredit = (EditText) fr.getView().findViewById(R.id.newcredit);
            EditText et_contact = (EditText) fr.getView().findViewById(R.id.contact);
            EditText et_sum = (EditText) fr.getView().findViewById(R.id.sum);
            EditText et_sumPercent = (EditText) fr.getView().findViewById(R.id.sumpercent);
            EditText et_Othersum = (EditText) fr.getView().findViewById(R.id.othersum);
            EditText et_comment = (EditText) fr.getView().findViewById(R.id.comment);

            int ActionIndex = 4 + sp_action.getSelectedItemPosition();
            String sPurse = sp_purse.getSelectedItem().toString();
            String sCredit = sp_credit.getSelectedItem().toString();
            if (sCredit.equals(getString(R.string.newCredit))) {
                sCredit = et_newcredit.getText().toString();
            } else {}
//            String sContact = sp_oldcontact.getSelectedItem().toString();
//            if (sContact.equals(getString(R.string.newContact))) {
            String sContact = et_contact.getText().toString();
//            }

            String ssum = et_sum.getText().toString();
            String ssumpercent = et_sumPercent.getText().toString();
            String sothersum = et_Othersum.getText().toString();
            String coment = et_comment.getText().toString();

            boolean err = sPurse.isEmpty() || sCredit.isEmpty() || sContact.isEmpty() || ssum.isEmpty();

            if (err) {
                Toast.makeText(getBaseContext(),getString(R.string.NotAllFieldsFilled),Toast.LENGTH_SHORT).show();
                return;
            }
            double sum = 0;
            if (!ssum.isEmpty()) sum = Double.parseDouble(ssum);
            double sumpercent = 0;
            if (!ssumpercent.isEmpty()) sumpercent = Double.parseDouble(ssumpercent);
            double othersum = 0;
            if (!sothersum.isEmpty()) sumpercent = Double.parseDouble(sothersum);

            MyPockets.DoAction(ActionIndex,sPurse,sum,sCredit,"",sumpercent,coment,othersum,sContact);

            et_sum.setText("");
            et_sumPercent.setText("");
            et_Othersum.setText("");
            et_comment.setText("");
            et_contact.setText("");
            et_newcredit.setText("");

            Toast.makeText(getBaseContext(), getString(R.string.Taken)+ " " + ssum, Toast.LENGTH_SHORT).show();

            ShowCash("");
            CheckForExchange();
        }
    }

    private void onSelectCredit(String str) {
        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
        LinearLayout llNewCredit = (LinearLayout) fr.getView().findViewById(R.id.layoutNewCredit);
        LinearLayout llOldContact = (LinearLayout) fr.getView().findViewById(R.id.layoutOldContact);
        LinearLayout llnewContact = (LinearLayout) fr.getView().findViewById(R.id.layoutnewContact);
        llnewContact.setVisibility(View.VISIBLE);
        EditText et_Contact = (EditText) fr.getView().findViewById(R.id.contact);
        et_Contact.setEnabled(false);
        llNewCredit.setVisibility(View.GONE);
        llOldContact.setVisibility(View.GONE);
        if (str.equals(getString(R.string.newCredit))) {
            llNewCredit.setVisibility(View.VISIBLE);
            llOldContact.setVisibility(View.VISIBLE);
        }
        ShowCash("");
        et_Contact.setText(MyPockets.GetCreditContact(str));
    }

    private void onSelectContact(String str) {
        Fragment fr = getFragmentManager().findFragmentById(R.id.mainFrame);
        EditText et_Contact = (EditText) fr.getView().findViewById(R.id.contact);
        et_Contact.setEnabled(false);
        LinearLayout llnewContact = (LinearLayout) fr.getView().findViewById(R.id.layoutnewContact);
        llnewContact.setVisibility(View.GONE);
        if (str.equals(getString(R.string.newContact))) {
            llnewContact.setVisibility(View.VISIBLE);
            et_Contact.setEnabled(true);
        }
    }
}

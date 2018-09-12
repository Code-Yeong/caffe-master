package com.vector.caffe.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.vector.caffe.R;
import com.vector.caffe.util.DBOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemLongClickListener {

    private ListView listView, listviewForDialog;
    private ImageView pic, close;
    private ListAdapter adapter;
    private List<Map<String, String>> datas;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.content_home_list);
        pic = (ImageView) findViewById(R.id.content_home_pic);

        listView.setOnItemLongClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, OrderActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_menu) {
            startActivity(new Intent(this, MenuActivity.class));
        } else if (id == R.id.home_statistic) {
            startActivity(new Intent(this, StatisticActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        init();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public List<Map<String, String>> getDatas() {
//        DatabaseHelper dh = DatabaseHelper.getInstance(this);
        List<Map<String, String>> datas = new ArrayList<>();
//        Map<String, String> map;
//        for (int i = 0; i < 3; i++) {
//            map = new HashMap();
//            for (int j = 1; j < 2; j++) {
//                map.put("time", "20170612123200" + i);
//                map.put("name" + j, "卡布奇诺拿铁咖啡");
//                map.put("count" + j, "1");
//                map.put("note" + j, "加冰 打包");
//            }
//            datas.add(map);
//        }
        DBOrder dbOrder = new DBOrder(this);
        datas = dbOrder.queryAllDetails();
        return datas;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, String> map = datas.get(position);
        LayoutInflater inflater = getLayoutInflater();
        List<Map<String, String>> list = new ArrayList<>();
        list.add(map);
        View v = inflater.from(this).inflate(R.layout.self_defined_home_detail_dialog, null);
        close = (ImageView) v.findViewById(R.id.self_defined_home_detail_dialog_close);
        listviewForDialog = (ListView) v.findViewById(R.id.self_defined_home_detail_dialog_list);
        ListAdapter adapter = new SimpleAdapter(this, list, R.layout.self_defined_list_item_detail,
                new String[]{"name1", "count1", "note1", "name2", "count2", "note2", "name3", "count3", "note3", "name4", "count4", "note4", "name5", "count5", "note5"}, new int[]{
                R.id.self_defined_list_item_detail_name_1, R.id.self_defined_list_item_detail_count_1, R.id.self_defined_list_item_detail_note_1,
                R.id.self_defined_list_item_detail_name_2, R.id.self_defined_list_item_detail_count_2, R.id.self_defined_list_item_detail_note_2,
                R.id.self_defined_list_item_detail_name_3, R.id.self_defined_list_item_detail_count_3, R.id.self_defined_list_item_detail_note_3,
                R.id.self_defined_list_item_detail_name_4, R.id.self_defined_list_item_detail_count_4, R.id.self_defined_list_item_detail_note_4,
                R.id.self_defined_list_item_detail_name_5, R.id.self_defined_list_item_detail_count_5, R.id.self_defined_list_item_detail_note_5});
        listviewForDialog.setAdapter(adapter);
        dialog = new AlertDialog.Builder(this)
                .setView(v)
                .show();
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return false;
    }

    public void init() {
        datas = getDatas();
        adapter = new SimpleAdapter(this, datas, R.layout.self_defined_home_list_item,
                new String[]{"time", "name1", "count1", "note1", "name2", "count2", "note2",
                        "name3", "count3", "note3", "name4", "count4", "note4"},
                new int[]{R.id.self_defined_home_list_item_time,
                        R.id.self_defined_home_list_item_1_name, R.id.self_defined_home_list_item_1_count, R.id.self_defined_home_list_item_1_note,
                        R.id.self_defined_home_list_item_2_name, R.id.self_defined_home_list_item_2_count, R.id.self_defined_home_list_item_2_note,
                        R.id.self_defined_home_list_item_3_name, R.id.self_defined_home_list_item_3_count, R.id.self_defined_home_list_item_3_note,
                        R.id.self_defined_home_list_item_4_name, R.id.self_defined_home_list_item_4_count, R.id.self_defined_home_list_item_4_note});
        listView.setAdapter(adapter);
    }
}

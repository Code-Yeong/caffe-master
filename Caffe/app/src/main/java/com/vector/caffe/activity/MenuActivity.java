package com.vector.caffe.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.vector.caffe.R;
import com.vector.caffe.bean.MenuItems;
import com.vector.caffe.util.DBMenu;
import com.vector.caffe.util.FunUtils;

import java.util.List;
import java.util.Map;

public class MenuActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private ImageView img_milk, img_coffee;
    private ListView lv_milk, lv_coffee, lv;
    private ListAdapter adapter;
    private ArrayAdapter categoryAdapter;
    List<Map<String, String>> lists;
    String mCategoryId = "1001";
    int pos;
    private String[] category = {"咖啡", "牛奶"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.content_menu_list);

        lv.setOnItemLongClickListener(this);
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, category);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FunUtils.startVibrate(this);
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.menu_add:
                //开启新界面

                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.self_defined_menu_list_item_add, null);
                Button submit = (Button) view.findViewById(R.id.self_defined_menu_list_item_add_submit);
                ImageView close = (ImageView) view.findViewById(R.id.self_defined_menu_list_item_add_close);
                final Spinner spinner = (Spinner) view.findViewById(R.id.self_defined_menu_list_item_add_type);
                final EditText price = (EditText) view.findViewById(R.id.self_defined_menu_list_item_add_price);
                final EditText name = (EditText) view.findViewById(R.id.self_defined_menu_list_item_add_name);
                final Dialog dialog = new AlertDialog.Builder(this)
                        .setView(view)
                        .show();
                spinner.setAdapter(categoryAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        DBMenu dbMenu = new DBMenu(MenuActivity.this);
                        if (position < 10) {
                            mCategoryId = "0" + (position + 1);
                        } else {
                            mCategoryId = "10" + (position + 1);
                        }
                        mCategoryId += dbMenu.getCountId(mCategoryId);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String mPrice = price.getText().toString().trim();
                        String mName = name.getText().toString().trim();
                        String mCategoryName = spinner.getSelectedItem().toString();
                        if (TextUtils.isEmpty(mPrice) || TextUtils.isEmpty(mName)) {
                            FunUtils.showToast(getWindow().getDecorView(), "数据有误");
                            return;
                        }
                        MenuItems menuItems = new MenuItems(mName, mCategoryId, mCategoryName, mPrice);
                        DBMenu dbMenu = new DBMenu(MenuActivity.this);
                        long result = dbMenu.insert(menuItems);
                        if (result > 0) {
                            FunUtils.showToast(getWindow().getDecorView(), "添加成功");
                            dialog.dismiss();
                            init();
                        } else {
                            FunUtils.showToast(getWindow().getDecorView(), "添加失败");
                            dialog.dismiss();
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        FunUtils.startVibrate(this);
        this.pos = position;
        Log.i("info", "click");
        new AlertDialog.Builder(this)
                .setTitle("注意")
                .setMessage("是否删除?")
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBMenu dbMenu = new DBMenu(MenuActivity.this);
                        Map<String, String> map = lists.get(pos);
                        MenuItems menuItems = new MenuItems(map.get("id"));
                        long result = dbMenu.deleteById(menuItems);
                        dialog.dismiss();
                        if (result > 0) {
                            FunUtils.showToast(getWindow().getDecorView(), "删除成功");
                            init();
                        } else {
                            FunUtils.showToast(getWindow().getDecorView(), "删除失败");
                        }
                    }
                }).show();
        return false;
    }

    public void init() {
        DBMenu dbMenu = new DBMenu(this);
        lists = dbMenu.queryAll();
        adapter = new SimpleAdapter(this, lists, R.layout.self_defined_menu_list_item, new String[]{"mid", "mname"},
                new int[]{R.id.self_defined_menu_list_item_id, R.id.self_defined_menu_list_item_name});
        lv.setAdapter(adapter);
    }
}

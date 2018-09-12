package com.vector.caffe.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.vector.caffe.R;
import com.vector.caffe.bean.DetailItems;
import com.vector.caffe.bean.OrderItems;
import com.vector.caffe.util.DBDetail;
import com.vector.caffe.util.DBMenu;
import com.vector.caffe.util.DBOrder;
import com.vector.caffe.util.FunUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private List<Map<String, String>> listsForListview, datas;
    private List<DetailItems> listsForDb;
    private List<String> iNames;
    private List<String> goodsIds;
    private ListView listView;
    private ListAdapter adapter;
    private TextView realReceive, originReceive, orderId;
    private FloatingActionButton fab;
    private String iSize, iTemp, iNote, iName, iPrice;
    private String newOrderId, goodsId = "1001";
    private int iCount = 1;
    private float iSum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listsForListview = new ArrayList<>();
        listsForDb = new ArrayList<>();
        iNames = new ArrayList<>();

        listView = (ListView) findViewById(R.id.content_order_list);
        orderId = (TextView) findViewById(R.id.content_order_id);
        realReceive = (TextView) findViewById(R.id.content_order_money_2);
        originReceive = (TextView) findViewById(R.id.content_order_money_1);
        fab = (FloatingActionButton) findViewById(R.id.order_fab);

        fab.setOnClickListener(new MyFabClickListener());
        listView.setOnItemLongClickListener(new MyListViewListener());
        newOrderId = getOrderId();
        init();//初始化list列表内容
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.order, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (listsForDb.size() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("当前还有未结算的订单,是否退出?")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //退出当前界面，并释放资源
                            listsForDb = null;
                            listsForListview = null;
                            dialog.dismiss();
                            OrderActivity.this.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        } else {
            this.finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (listsForDb.size() > 0) {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("当前还有未结算的订单,是否退出?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //退出当前界面，并释放资源
                                listsForDb = null;
                                listsForListview = null;
                                dialog.dismiss();
                                OrderActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            } else {
                this.finish();
            }
        } else if (id == R.id.order_save) {
            if (listsForDb.size() == 0) {
                FunUtils.showToast(getWindow().getDecorView(), "结算失败:当前没有产生任何订单!");
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("请确保客户付款之后再点击【确定】")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBOrder dbOrder = new DBOrder(OrderActivity.this);
                                OrderItems orderItems = new OrderItems(newOrderId, iSum + "", null);
                                if (dbOrder.insert(orderItems) > 0) {
                                    DBDetail dbDetail = new DBDetail(OrderActivity.this);
                                    if (dbDetail.insertAll(listsForDb) > 0) {
                                        FunUtils.showToast(getWindow().getDecorView(), "订单保存成功!");
                                    } else {
                                        FunUtils.showToast(getWindow().getDecorView(), "订单保存失败!");
                                    }
                                } else {
                                    FunUtils.showToast(getWindow().getDecorView(), "订单保存失败!");
                                }
                                dialog.dismiss();
                                OrderActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        iSize = "中杯";
        iTemp = "常温";
        iCount = 1;
        listsForListview.clear();
        realReceive.setText(getiSum());
        orderId.setText(newOrderId);
        if (listView.getChildCount() > 0) {
            adapter = null;
            listView.setAdapter(adapter);
        }
        int flag = 0;
        for (DetailItems d : listsForDb) {
            Map<String, String> map = new HashMap<>();
            map.put("name", "[" + d.getMid() + "]" + iNames.get(flag));
            map.put("price", d.getDprice());
            map.put("count", "x " + d.getDcount() + "杯");
            map.put("size", d.getDsize());
            map.put("temp", d.getDtemp());
            map.put("note", d.getDnote());
            map.put("id", (listsForListview.size() + 1) + "");
            flag++;
            listsForListview.add(map);
        }

        adapter = new SimpleAdapter(this, listsForListview, R.layout.self_defined_order_list_items, new String[]{
                "id", "name", "size", "temp", "price", "note", "count"}, new int[]{
                R.id.self_defined_order_list_items_id, R.id.self_defined_order_list_items_name,
                R.id.self_defined_order_list_items_size, R.id.self_defined_order_list_items_temp,
                R.id.self_defined_order_list_items_price, R.id.self_defined_order_list_items_note,
                R.id.self_defined_order_list_items_count});
        listView.setAdapter(adapter);
    }

    private class MyFabClickListener implements View.OnClickListener {
        Dialog dialog;
        Button submit;
        TextView count;
        CheckBox size_l, size_m, size_s;
        CheckBox temp_h, temp_n, temp_c;
        ImageView btnClose;
        ImageView search;
        ImageView countDel, countAdd;
        EditText price, note, name;

        @Override
        public void onClick(View view) {
            if (listsForDb.size() >= 4) {
                FunUtils.showToast(getWindow().getDecorView(), "订单内容已达到上限");
                return;
            }
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.self_defined_order_add_item, null);

            btnClose = (ImageView) v.findViewById(R.id.self_defined_order_add_item_cancel);
            submit = (Button) v.findViewById(R.id.self_defined_order_add_item_submit);

            price = (EditText) v.findViewById(R.id.self_defined_order_add_item_price);
            note = (EditText) v.findViewById(R.id.self_defined_order_add_item_note);

            name = (EditText) v.findViewById(R.id.self_defined_order_add_item_name);

            count = (TextView) v.findViewById(R.id.self_defined_order_add_item_count);
            countAdd = (ImageView) v.findViewById(R.id.self_defined_order_add_item_count_add);
            countDel = (ImageView) v.findViewById(R.id.self_defined_order_add_item_count_del);
            search = (ImageView) v.findViewById(R.id.self_defined_order_add_item_search);

            size_l = (CheckBox) v.findViewById(R.id.self_defined_order_add_item_size_l);
            size_m = (CheckBox) v.findViewById(R.id.self_defined_order_add_item_size_m);
            temp_h = (CheckBox) v.findViewById(R.id.self_defined_order_add_item_temp_h);
            temp_c = (CheckBox) v.findViewById(R.id.self_defined_order_add_item_temp_c);
            temp_n = (CheckBox) v.findViewById(R.id.self_defined_order_add_item_temp_n);

            size_l.setOnClickListener(new MyClickListener(temp_h, temp_n, temp_c, size_l, size_m));
            size_m.setOnClickListener(new MyClickListener(temp_h, temp_n, temp_c, size_l, size_m));
            temp_h.setOnClickListener(new MyClickListener(temp_h, temp_n, temp_c, size_l, size_m));
            temp_c.setOnClickListener(new MyClickListener(temp_h, temp_n, temp_c, size_l, size_m));
            temp_n.setOnClickListener(new MyClickListener(temp_h, temp_n, temp_c, size_l, size_m));

            search.setOnClickListener(new MyClickListener(name));

            submit.setOnClickListener(new MyClickListener(submit));
            countAdd.setOnClickListener(new MyClickListener(count));
            countDel.setOnClickListener(new MyClickListener(count));

            dialog = new AlertDialog.Builder(OrderActivity.this)
                    .setView(v)
                    .show();
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    iPrice = price.getText().toString().trim();
                    iNote = note.getText().toString().trim();
                    if (TextUtils.isEmpty(iPrice) || TextUtils.isEmpty(iName)) {
                        FunUtils.showToast(getWindow().getDecorView(), "数据填写有误");
                        return;
                    }
                    iSum += (Float.parseFloat(iPrice) * iCount);

                    DetailItems detailItems = new DetailItems(newOrderId, goodsId, iPrice, iCount + "", iSize, iTemp, iNote);
                    listsForDb.add(detailItems);

                    init();
                    dialog.dismiss();
                }
            });
        }
    }

    private class MyClickListener implements View.OnClickListener {
        View view;
        CheckBox temp_h, temp_n, temp_c, size_l, size_m;

        MyClickListener(View view) {
            this.view = view;
        }

        MyClickListener(CheckBox v1, CheckBox v2, CheckBox v3, CheckBox v4, CheckBox v5) {
            this.temp_h = v1;
            this.temp_n = v2;
            this.temp_c = v3;
            this.size_l = v4;
            this.size_m = v5;
        }

        @Override
        public void onClick(final View v) {
            int id = v.getId();
            final Dialog dialog;
            final ListView list;
            switch (id) {
                case R.id.self_defined_order_add_item_search:
                    View myView = getLayoutInflater().inflate(R.layout.self_defined_order_search_dialog, null);
                    EditText et = (EditText) myView.findViewById(R.id.self_defined_order_search_dialog_search);
                    ImageView close = (ImageView) myView.findViewById(R.id.self_defined_order_search_dialog_close);
                    list = (ListView) myView.findViewById(R.id.self_defined_order_search_dialog_list);
                    DBMenu dbMenu = new DBMenu(OrderActivity.this);
                    datas = dbMenu.queryAll();
                    ListAdapter adapter = new SimpleAdapter(OrderActivity.this, datas, R.layout.self_defined_menu_list_item,
                            new String[]{"mid", "mname"}, new int[]{R.id.self_defined_menu_list_item_id, R.id.self_defined_menu_list_item_name});
                    list.setAdapter(adapter);

                    dialog = new AlertDialog.Builder(OrderActivity.this)
                            .setView(myView)
                            .show();

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            DBMenu dbMenu = new DBMenu(OrderActivity.this);
                            datas = dbMenu.queryByName(s.toString());
                            ListAdapter adapter = new SimpleAdapter(OrderActivity.this, datas, R.layout.self_defined_menu_list_item,
                                    new String[]{"mid", "mname"}, new int[]{R.id.self_defined_menu_list_item_id, R.id.self_defined_menu_list_item_name});
                            list.setAdapter(adapter);
                        }
                    });

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            goodsId = datas.get(position).get("mid");
                            iName = datas.get(position).get("mname");
                            iNames.add(iName);
                            ((EditText) view).setText(iName);
                            dialog.dismiss();
                        }
                    });
                    break;

                case R.id.self_defined_order_add_item_count_add:
                    iCount++;
                    ((TextView) view).setText(iCount + "");
                    break;
                case R.id.self_defined_order_add_item_count_del:
                    if (iCount > 1) {
                        iCount--;
                        ((TextView) view).setText(iCount + "");
                    }
                    break;
                case R.id.self_defined_order_add_item_size_l:
                    Log.i("info", "large");
                    size_l.setChecked(true);
                    size_m.setChecked(false);
                    iSize = "大杯";
                    break;
                case R.id.self_defined_order_add_item_size_m:
                    Log.i("info", "small");
                    size_l.setChecked(false);
                    size_m.setChecked(true);
                    iSize = "中杯";
                    break;
                case R.id.self_defined_order_add_item_temp_c:
                    temp_h.setChecked(false);
                    temp_n.setChecked(false);
                    temp_c.setChecked(true);
                    iTemp = "冰饮";
                    break;
                case R.id.self_defined_order_add_item_temp_n:
                    temp_h.setChecked(false);
                    temp_n.setChecked(true);
                    temp_c.setChecked(false);
                    iTemp = "常温";
                    break;
                case R.id.self_defined_order_add_item_temp_h:
                    temp_h.setChecked(true);
                    temp_n.setChecked(false);
                    temp_c.setChecked(false);
                    iTemp = "热饮";
                    break;
            }
        }

    }


    private class MyListViewListener implements AdapterView.OnItemLongClickListener {
        Map<String, String> map;
        DetailItems d;

        @Override
        public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
            d = listsForDb.get(position);
            map = listsForListview.get(position);
            new AlertDialog.Builder(OrderActivity.this)
                    .setTitle("提示")
                    .setMessage("确定删除该订单吗？\n商品名：" + map.get("name"))
                    .setNegativeButton("取消", null)
                    .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            iSum -= (Float.parseFloat(d.getDprice()) * Integer.parseInt(d.getDcount()));
                            listsForDb.remove(position);
                            iNames.remove(position);
                            init();
                        }
                    }).show();

            return false;
        }
    }


    public String getiSum() {
        return "￥" + iSum;
    }

    public String getOrderId() {
        DBOrder dbOrder = new DBOrder(this);
        String oriderid = dbOrder.getNewOrderId();
        Log.i("info", "订单号：" + oriderid);
        return oriderid;
    }

}

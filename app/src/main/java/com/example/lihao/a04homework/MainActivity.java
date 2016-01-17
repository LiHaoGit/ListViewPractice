package com.example.lihao.a04homework;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int index =1;
    private int pageSize = 10;
    ListView lv;
    Button bt_pre;
    Button bt_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.lv);
        lv.setAdapter(new MyAdapter(getPageData(index-1,pageSize)));

        bt_pre = (Button) findViewById(R.id.bt_pre);
        bt_next = (Button) findViewById(R.id.bt_next);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Person person = (Person) parent.getItemAtPosition(position);
                Log.d("hehe",person.getName());

                                                                    //如果使用getApplication()当作参数会报错
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(person.getName());
                builder.setMessage("您要执行的操作");
                builder.setPositiveButton("打电话", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:"+person.getPhone()));
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("发信息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SmsManager sms = SmsManager.getDefault();
                        ArrayList<String> msg = sms.divideMessage("nihao1111");
                        sms.sendMultipartTextMessage(person.getPhone(),null,msg,null,null);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private List<Person> getPageData(int pageIndex,int pageSize)
    {
        List<Person> datas = new ArrayList<Person>();
        SqliteHelper helper = new SqliteHelper(getApplication(),"person",null,1);
        SQLiteDatabase  db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from person order by _id  limit ? offset ?",new String[]{pageSize+"",((pageIndex-1)*pageSize)+""});
        while (cursor.moveToNext()){
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            int id = cursor.getInt(0);
            Person person = new Person(name,phone,id);
            datas.add(person);
        }
        Log.d("hehe",pageIndex+"");
        db.close();
        return  datas;
    }

    public void pre(View v) {
        index = index -1;
        bt_next.setEnabled(true);
        if (index == 1) {
            lv.setAdapter(new MyAdapter(getPageData(index, pageSize)));
            v.setEnabled(false);
        }
        else{
            lv.setAdapter(new MyAdapter(getPageData(index,pageSize)));
        }
    }

    public void next(View v) {
        index = index+1;
        bt_pre.setEnabled(true);
        List<Person> pageData = getPageData(index, pageSize);
        if (pageData.size()<10){
            v.setEnabled(false);
        }else{

            lv.setAdapter(new MyAdapter(pageData));
        }
    }

    class MyAdapter extends BaseAdapter {

        private int PageShowCount = 0;
        private List<Person> data;

        public MyAdapter(List<Person> data) {
            super();
            if (data != null)
                this.data = data;
            else
                this.data = new ArrayList<Person>();
        }

        public int getPageShowCount() {
            return PageShowCount;
        }

        /**
         * How many items are in the data set represented by this Adapter.
         *
         * @return Count of items.
         */
        @Override
        public int getCount() {
            return data.size();
        }

        /**
         * Get the data item associated with the specified position in the data set.
         *
         * @param position Position of the item whose data we want within the adapter's
         *                 data set.
         * @return The data at the specified position.
         */
        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        /**
         * Get the row id associated with the specified position in the list.
         *
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The id of the item at the specified position.
         */
        @Override
        public long getItemId(int position) {
            return data.get(position).getId();
        }

        /**
         * Get a View that displays the data at the specified position in the data set. You can either
         * create a View manually or inflate it from an XML layout file. When the View is inflated, the
         * parent View (GridView, ListView...) will apply default layout parameters unless you use
         * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
         * to specify a root view and to prevent attachment to the root.
         *
         * @param position    The position of the item within the adapter's data set of the item whose view
         *                    we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *                    is non-null and of an appropriate type before using. If it is not possible to convert
         *                    this view to display the correct data, this method can create a new view.
         *                    Heterogeneous lists can specify their number of view types, so that this View is
         *                    always of the right type (see {@link #getViewTypeCount()} and
         *                    {@link #getItemViewType(int)}).
         * @param parent      The parent that this view will eventually be attached to
         * @return A View corresponding to the data at the specified position.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;

            if (convertView != null) {
                v = convertView;
            } else {
                this.PageShowCount++;
                v = View.inflate(getApplication(), R.layout.item, null);
            }
            TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
            tv_name.setText(data.get(position).getName());
            TextView tv_phone = (TextView) v.findViewById(R.id.tv_phone);
            tv_phone.setText(data.get(position).getPhone());

            return v;
        }

    }
}

package com.example.crystaleyes.cip;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.SparseBooleanArray;

import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class unblock extends Activity implements AdapterView.OnItemClickListener {
    List<String> name1 = new ArrayList<String>();
    List<String> phno1 = new ArrayList<String>();
    MyAdapter ma ;
    Button select;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unblock);

        sharedpreferences = MyApplication.getAppContext().getSharedPreferences("BlockedContacts", Context.MODE_PRIVATE);
        editor=sharedpreferences.edit();
        getAllContacts(this.getContentResolver());
        ListView lv= (ListView) findViewById(R.id.lv1);
        ma = new MyAdapter();
        lv.setAdapter(ma);
        lv.setOnItemClickListener(this);
        lv.setItemsCanFocus(false);
        lv.setTextFilterEnabled(true);
        // adding
        select = (Button) findViewById(R.id.button1);
        select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StringBuilder checkedcontacts = new StringBuilder();
                System.out.println(".............." + ma.mCheckStates.size());
                for (int i = 0; i < name1.size(); i++)

                {
                    if (ma.mCheckStates.get(i) == true) {
                        checkedcontacts.append(name1.get(i).toString());
                        checkedcontacts.append("\n");
                        //editor.putString(phno1.get(i).toString(), name1.get(i).toString());
                        //editor.commit();
                        editor.remove(phno1.get(i).toString());
                        editor.commit();
                        //Toast.makeText(unblock.this,"Blocked"+ i + sharedpreferences.getString(phno1.get(i).toString(), ""), Toast.LENGTH_SHORT).show();

                    }


                }

                Toast.makeText(unblock.this,"Unblocked "+ checkedcontacts, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(getIntent());

            }
        });


    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        ma.toggle(arg2);
    }
    public  void getAllContacts(ContentResolver cr) {
        Map<String,?> map=sharedpreferences.getAll();
        Object[] ob=map.keySet().toArray();
        int i=0;

        if(!map.isEmpty()) {
            while (i < map.size()) {
                String name = map.get(ob[i]).toString(); //.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phoneNumber = ob[i++].toString();// phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                System.out.println(".................." + phoneNumber);
                if (!(name1.contains(phoneNumber))) {
                    name1.add(name);
                    phno1.add(phoneNumber);
                }
            }

        }

        map.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {

        }

        return super.onOptionsItemSelected(item);
    }
    class MyAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener
    {  private SparseBooleanArray mCheckStates;
        LayoutInflater mInflater;
        TextView tv1;
        CheckBox cb;
        MyAdapter()
        {
            mCheckStates = new SparseBooleanArray(name1.size());
            mInflater = (LayoutInflater)unblock.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return name1.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub

            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View vi=convertView;

            if (convertView == null)
                vi = mInflater.inflate(R.layout.row, null);
            TextView tv = (TextView) vi.findViewById(R.id.textView1);
            tv1 = (TextView) vi.findViewById(R.id.textView2);
            cb = (CheckBox) vi.findViewById(R.id.checkBox1);
            tv.setText("Name :" + name1.get(position));
            tv1.setText("Phone No :" + phno1.get(position));
            cb.setTag(position);
            cb.setChecked(mCheckStates.get(position, false));
            cb.setOnCheckedChangeListener(this);
            return vi;
        }
        public boolean isChecked(int position) {
            return mCheckStates.get(position, false);
        }

        public void setChecked(int position, boolean isChecked) {
            mCheckStates.put(position, isChecked);
            System.out.println("hello...........");
            notifyDataSetChanged();
        }
       /* public void filter(String charText) {
            charText = charText.toLowerCase(Locale.getDefault());
            name1.clear();
            if (charText.length() == 0) {
                name1.addAll(arraylist);
            } else {
                for (SelectUser wp : arraylist) {
                    if (wp.getName().toLowerCase(Locale.getDefault())
                            .contains(charText)) {
                        name1.add(wp);
                    }
                }
            }
            notifyDataSetChanged();
        }*/

        public void toggle(int position) {
            setChecked(position, !isChecked(position));
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub

            mCheckStates.put((Integer) buttonView.getTag(), isChecked);
        }
    }

}
    /*public ListView lv;
    public ArrayList<String> arr;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        arr=new ArrayList<String>(1000);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Cursor cursor = getContentResolver().query(   ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
        int i=0;

        while (cursor.moveToNext())
        {
            name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            arr.add(name);

        }
            lv = (ListView) findViewById(R.id.lv);


        lv=(ListView)findViewById(R.id.lv);
        lv.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1 , arr));
        }

}
*/
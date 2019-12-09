package com.tbright.sketchpad.listactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tbright.sketchpad.R;
import com.tbright.sketchpad.listactivity.bean.EinkHomeworkViewBean;
import com.tbright.sketchpad.view.view.ScaleEinkHomeworkView;
import com.tbright.sketchpad.view.view.TestView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView rv;
    private LinearLayoutManager linearLayoutManager;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        findViewById(R.id.move).setOnClickListener(this);
        findViewById(R.id.qianbi).setOnClickListener(this);
        findViewById(R.id.xiangpica).setOnClickListener(this);
        linearLayoutManager = new LinearLayoutManager(this);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(linearLayoutManager);
        myAdapter = new MyAdapter();
        rv.setAdapter(myAdapter);
        ListPresenter listPresenter = new ListPresenter();
        listPresenter.downloadTestPaper("712424997901045760", "712425681283190784", new ListPresenter.ResultListener() {
            @Override
            public void result(ArrayList<EinkHomeworkViewBean.PaperListBean> result) {
                myAdapter.setDatas(result);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.move:
                myAdapter.setMode(0);
                break;
            case R.id.qianbi:
                myAdapter.setMode(1);
                break;
            case R.id.xiangpica:
                myAdapter.setMode(2);
                break;
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private int mode;

        public void setMode(int mode) {
            this.mode = mode;
            notifyDataSetChanged();
        }
        private ArrayList<EinkHomeworkViewBean.PaperListBean> mResult = new ArrayList<>();

        public void setDatas(ArrayList<EinkHomeworkViewBean.PaperListBean> result) {
            mResult.clear();
            mResult.addAll(result);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(ListActivity.this).inflate(R.layout.item_list, parent, false);
            return new MyHolder(inflate);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof MyHolder) {
                MyHolder myHolder = (MyHolder) holder;
                EinkHomeworkViewBean.PaperListBean item = mResult.get(position);
                myHolder.einkHomeworkView.addBitmap(item);
                myHolder.einkHomeworkView.setCurrentMode(mode);
            }
        }

        @Override
        public int getItemCount() {
            return mResult.size();
        }


    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private TestView einkHomeworkView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            einkHomeworkView = (TestView) itemView.findViewById(R.id.einkHomeworkView);
        }
    }
}

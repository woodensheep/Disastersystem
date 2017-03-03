package com.nandity.disastersystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nandity.disastersystem.R;
import com.nandity.disastersystem.activity.MainActivity;
import com.nandity.disastersystem.adapter.CategoryViewProvider;
import com.nandity.disastersystem.bean.Category;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

/**
 * Created by lemon on 2017/2/22.
 */

public class SetupFragment extends Fragment {

    @BindView(R.id.ll_signout)
    LinearLayout signOut;
    private String[] stringsname = new String[]{"视频采集", "群组对讲", "录像地址",
            "存储路径", "摄像头", "帧数",
            "码流", "分辨率", "系统信息"
    };
    private String[] stringsid = new String[]{"video_capture", "group_intercom", "video_address",
            "storage_path", "camera", "frames_number",
            "stream", "resolving_power", "system_information"
    };

    private MultiTypeAdapter adapter;
    /* Items 等同于 ArrayList<Object> */
    private Items items;
    private RecyclerView rvSetup;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup, container, false);

        ButterKnife.bind(this, view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false) {

            @Override
            public boolean canScrollVertically() {

                return false;
            }

        };

        rvSetup = (RecyclerView) view.findViewById(R.id.rv_setup);
        items = new Items();
        adapter = new MultiTypeAdapter(items);
        /* 注册类型和 View 的对应关系 */
        adapter.register(Category.class, new CategoryViewProvider());

        /* 模拟加载数据，也可以稍后再加载，然后使用
         * adapter.notifyDataSetChanged() 刷新列表 */
        for (int i = 0; i < 9; i++) {
            items.add(new Category(stringsname[i],
                    getContext().getResources().getIdentifier(stringsid[i], "mipmap", getContext().getPackageName())
                    , i)
            );
            //items.add(new Song("小艾大人", R.drawable.avatar_dakeet));
            //items.add(new Song("许岑", R.drawable.avatar_cen));
        }
        rvSetup.setLayoutManager(linearLayoutManager);
        rvSetup.setAdapter(adapter);

        setListeners();
        return view;
    }

    private void setListeners() {
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).signOut();
            }
        });
    }


}

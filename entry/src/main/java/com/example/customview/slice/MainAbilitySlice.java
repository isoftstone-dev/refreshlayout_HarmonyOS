package com.example.customview.slice;

import com.example.customview.ResourceTable;
import com.example.refreshlayout_harmonyos.HeadDefaultComponent;
import com.example.refreshlayout_harmonyos.IRefresh;
import com.example.refreshlayout_harmonyos.RefreshComponent;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.agp.window.dialog.ToastDialog;
import ohos.eventhandler.EventHandler;
import ohos.eventhandler.EventRunner;
import ohos.multimodalinput.event.TouchEvent;

import java.util.ArrayList;

public class MainAbilitySlice extends AbilitySlice {

    ListContainer listContainer;

    ArrayList<Integer> list = new ArrayList<>();
    MyProvider myProvider;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        ListContainer listContainer = (ListContainer) findComponentById(ResourceTable.Id_list);
        setData();
        myProvider = new MyProvider();
        listContainer.setItemProvider(myProvider);

        //获取到刷新组件
        RefreshComponent myView = (RefreshComponent) findComponentById(ResourceTable.Id_myView);
        //设置头部刷新样式,可自定义样式
        HeadDefaultComponent headDefaultComponent = new HeadDefaultComponent(this);
        //添加样式到头部
        myView.setHeadComponent(headDefaultComponent);
        //设置刷新回调
        myView.setRefreshListener(new IRefresh.RefreshListener() {
            @Override
            public void onRefresh() {

                new EventHandler(EventRunner.getMainEventRunner()).postTask(new Runnable() {
                    @Override
                    public void run() {
                        // 数据更新完  结束掉刷新
                        myView.refreshFinish();
                    }
                }, 2000);
            }

            @Override
            public boolean enableRefresh() {
                return false;
            }
        });
    }

    private void setData() {
        list.clear();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    class MyProvider extends RecycleItemProvider {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public Component getComponent(int i, Component component, ComponentContainer componentContainer) {
//            ViewHolder holder = null;
//            if (component == null) {
//                component = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_item_layout, null, false);
//                holder = new ViewHolder();
//                holder.text = (Text) component.findComponentById(ResourceTable.Id_text);
//                component.setTag(holder);
//            } else {
//                holder = (ViewHolder) component.getTag();
//            }
////            System.out.println("++++++i+++++" + i);
//            holder.text.setText(String.valueOf(i));
//            return component;
            Component cpt = component;
            if (cpt == null) {
                cpt = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_item_layout, null, false);
            }
            Text text = (Text) cpt.findComponentById(ResourceTable.Id_text);
            text.setText(String.valueOf(i));
            return cpt;
        }

        class ViewHolder {
            Text text;
        }
    }

}

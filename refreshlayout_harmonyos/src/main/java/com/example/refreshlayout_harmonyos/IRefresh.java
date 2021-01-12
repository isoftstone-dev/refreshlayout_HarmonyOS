package com.example.refreshlayout_harmonyos;

public interface IRefresh {
    void refreshFinish();

    void setRefreshListener(RefreshListener listener);

    void setHeadComponent(HeadBaseComponent headComponent);

    interface RefreshListener {

        /**
         * 告诉调用者，此时正在刷新
         */
        void onRefresh();

        /**
         * 是否允许刷新
         *
         * @return
         */
        boolean enableRefresh();
    }
}

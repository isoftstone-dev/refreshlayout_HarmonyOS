package com.example.refreshlayout_harmonyos;

import ohos.agp.components.*;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

public class RefreshComponent extends ComponentContainer implements Component.LayoutRefreshedListener, Component.TouchEventListener, IRefresh {

    public RefreshComponent(Context context) {
        super(context);
    }

    public RefreshComponent(Context context, AttrSet attrSet) {
        super(context, attrSet);
        setTouchEventListener(this);
        setLayoutRefreshedListener(this);

//        Display display = DisplayManager.getInstance().getDefaultDisplay(context).get();
//        DisplayAttributes displayAttributes = display.getAttributes();
//        int mHeight = displayAttributes.height;
    }

    @Override
    public void onRefreshed(Component component) {

        Component head = getComponentAt(0);
        headBaseComponent.mPullRefreshHeight = head.getHeight();
        Component content = getComponentAt(1);

        head.setComponentPosition(0, content.getTop() - head.getHeight(), head.getRight(), content.getTop());
        content.setComponentPosition(0, content.getTop(), content.getRight(), content.getBottom());
    }

    private float startDownY;
    private int mLastY;
    RefreshListener refreshListener;
    protected HeadBaseComponent headBaseComponent;
    private HeadBaseComponent.RefreshState mState;

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        Component head = getComponentAt(0);
        Component content = getComponentAt(1);

        final boolean pointerUp =
                touchEvent.getAction() == TouchEvent.OTHER_POINT_UP;
        final int skipIndex = pointerUp ? touchEvent.getIndex() : -1;

        float sumX = 0, sumY = 0;
        final int count = touchEvent.getPointerCount();
        // 把所有还在触摸的手指的位置x，y加起来，后面求平均数，算出中心焦点
        for (int i = 0; i < count; i++) {
            if (skipIndex == i) {
                // 跳过非主要指针的抬起动作
                continue;
            }
            sumY += touchEvent.getPointerPosition(i).getY();
        }
        final int div = pointerUp ? count - 1 : count;
        // 求平均值，算出中心焦点
        final float endUpY = sumY / div;

        switch (touchEvent.getAction()) {
            case TouchEvent.CANCEL:
                System.out.println("-------指示事件被中断或取消。。-------");
                break;
            case TouchEvent.PRIMARY_POINT_UP:
//                System.out.println("-------表示最后一根手指从屏幕上抬起。。-------");
//                //手指抬起之后, //如果状态不是刷新,判断下拉的高度是否大于头部高度  如果是则回弹,开始刷新,如果没有则回到初始状态,隐藏头部
                if (mState != HeadBaseComponent.RefreshState.STATE_REFRESH) {
                    //如果状态不是刷新,判断下拉的高度是否大于头部高度  如果是则回弹, 开始刷新,如果没有则回到初始状态,隐藏头部
                    if (refreshListener != null && head.getBottom() > headBaseComponent.mPullRefreshHeight) {
                        System.out.println("-------手指抬起之后。。-------");
                        head.setComponentPosition(head.getLeft(), 0, head.getRight(), head.getHeight());
                        content.setComponentPosition(content.getLeft(), head.getHeight(), content.getRight(), content.getBottom());
                        headBaseComponent.onRefresh();
                        refreshListener.onRefresh();
                        mState = HeadBaseComponent.RefreshState.STATE_REFRESH;
                    } else {
                        head.setComponentPosition(head.getLeft(), -head.getHeight(), head.getRight(), content.getTop());
                        content.setComponentPosition(content.getLeft(), 0, content.getRight(), content.getBottom());
                        mState = HeadBaseComponent.RefreshState.STATE_INIT;
                        System.out.println("-------走了这个时间了啊。。-------");
                        content.setEnabled(true);
//                        getchild(content, true);

                    }
                }
                break;
            case TouchEvent.OTHER_POINT_DOWN:
                //表示当一个或多个手指已经触摸屏幕时，另一根手指触摸了屏幕。
                startDownY = endUpY;

                break;
            case TouchEvent.OTHER_POINT_UP:
                //表示有些手指从屏幕上抬起，而另一些手指则留在屏幕上。
                startDownY = endUpY;
                break;
            case TouchEvent.PRIMARY_POINT_DOWN:
                //表示第一根手指触摸屏幕。
                startDownY = endUpY;
                break;
            case TouchEvent.POINT_MOVE:
                //指示手指在屏幕上移动。
                //1.正在刷新 2.未刷新
                final float scrollY = endUpY - startDownY;
                if (scrollY > 0) {
                    if (content.getScrollValue(AXIS_Y) > 0) {
                        content.setEnabled(true);
                        return false;
                    }
                    content.setEnabled(false);
                    float offsetY;

                    if (head.getTop() < headBaseComponent.mPullRefreshHeight) {
                        // 没到可刷新的距离，减少阻尼
                        offsetY = (int) (mLastY / 2f);
                    } else {
                        // 达到可刷新的距离，加大阻尼
                        offsetY = (int) (mLastY / 3.2f);
                    }

                    startDownY = endUpY;
                    mLastY = (int) scrollY;

                    if (mState != HeadBaseComponent.RefreshState.STATE_REFRESH) {
                        // 未刷新时   1.头部正在下拉,并未完全显示,  2.头部已经下拉并且超出自身高度
                        if (head.getBottom() > 0 && head.getBottom() < headBaseComponent.mPullRefreshHeight) {
//                        1.头部正在下拉,并未完全显示,
                            headBaseComponent.onHeadVisible();
                        } else if (head.getBottom() > headBaseComponent.mPullRefreshHeight) {
//                        2.头部已经下拉并且超出自身高度
                            headBaseComponent.onHeadOver();
//                        System.out.println("----头部已经下拉并且超出自身高度--=");
                        }
                    }
                    content.setTop((int) (content.getTop() + offsetY));
                    head.setComponentPosition(0, content.getTop() - head.getHeight(), head.getRight(), content.getTop());
                    content.setComponentPosition(0, content.getTop(), content.getRight(), content.getBottom());
                    return true;
                } else if (scrollY < 0) {
                    if (content.getTop() > 0 && content.getScrollValue(AXIS_Y) == 0) {
                        content.setEnabled(false);
                        float offsetY;
                        if (head.getTop() < headBaseComponent.mPullRefreshHeight) {
                            // 没到可刷新的距离，减少阻尼
                            offsetY = (int) (mLastY / 2f);
                        } else {
                            // 达到可刷新的距离，加大阻尼
                            offsetY = (int) (mLastY / 3.2f);
                        }
                        startDownY = endUpY;
                        mLastY = (int) scrollY;
                        content.setTop((int) (content.getTop() + offsetY));
                        content.setComponentPosition(content.getLeft(), content.getTop(), content.getRight(), content.getBottom());
                        return true;
                    } else {
                        head.setComponentPosition(head.getLeft(), -head.getHeight(), head.getRight(), 0);
                        content.setComponentPosition(content.getLeft(), 0, content.getRight(), content.getBottom());
                        content.setEnabled(true);
                        return false;
                    }

                }
                break;
        }
        return false;
    }

    @Override
    public void refreshFinish() {
        headBaseComponent.onFinish();
        headBaseComponent.setState(HeadBaseComponent.RefreshState.STATE_INIT);
        Component head = getComponentAt(0);
        Component content = getComponentAt(1);
        content.setEnabled(true);
        if (head.getBottom() > 0) {
            head.setComponentPosition(head.getLeft(), -head.getHeight(), head.getRight(), 0);
            content.setComponentPosition(content.getLeft(), 0, content.getRight(), content.getHeight());
        }
        mState = HeadBaseComponent.RefreshState.STATE_INIT;

    }

    @Override
    public void setRefreshListener(RefreshListener listener) {
        refreshListener = listener;
    }

    @Override
    public void setHeadComponent(HeadBaseComponent headComponent) {
        if (headBaseComponent != null) {
            removeComponent(headBaseComponent);
        }
        headBaseComponent = headComponent;
        LayoutConfig config = new LayoutConfig(LayoutConfig.MATCH_PARENT, LayoutConfig.MATCH_CONTENT);
        addComponent(headBaseComponent, 0, config);
    }
}

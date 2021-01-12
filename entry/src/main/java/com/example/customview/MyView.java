package com.example.customview;

import ohos.agp.components.AttrSet;
import ohos.agp.components.Component;
import ohos.agp.components.ComponentContainer;
import ohos.app.Context;
import ohos.multimodalinput.event.TouchEvent;

public class MyView extends ComponentContainer implements Component.TouchEventListener, Component.LayoutRefreshedListener, Component.ScrolledListener {

    boolean aBoolean = false;

    public MyView(Context context) {
        super(context);
    }

    public MyView(Context context, AttrSet attrSet) {
        super(context, attrSet);
        setTouchEventListener(this);
        setLayoutRefreshedListener(this);
        setScrolledListener(this);
    }

    @Override
    public void onRefreshed(Component component) {
        Component head = getComponentAt(0);
        component.setComponentPosition(component.getLeft(), 50, component.getRight(), component.getBottom());
    }

    @Override
    public boolean onTouchEvent(Component component, TouchEvent touchEvent) {
        System.out.println(component);
        Component head = getComponentAt(0);
        if (touchEvent.getAction() == touchEvent.POINT_MOVE) {


        }
        return true;
    }

    @Override
    public void onContentScrolled(Component component, int i, int i1, int i2, int i3) {
        System.out.println("12222222222222222222222222222222222222222222");
    }
}



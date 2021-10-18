package com.xxc.dev.adapter;

public interface MyControllerFactory extends ControllerFactory {

    @ViewType(1)
    AnimalController testCreateController();

    @ViewType(2)
    String testCreate();

    @ViewType(3)
    String myController();

    @ViewType(4)
    String onCreate();

    @ViewType(5)
    String createController();

    @ViewType(6)
    String onCreateController();

    @ViewType(7)
    String createEasyController();

    @ViewType(8)
    String createHolderController();

    @ViewType(9)
    String onCreateHolderController();

    @ViewType(10)
    String testHolderController();

}

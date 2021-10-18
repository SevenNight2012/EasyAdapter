package com.xxc.dev.adapter;


import androidx.annotation.NonNull;

/**
 * create the controller of viewType
 */
interface ControllerInfoFactory {

    @NonNull
    ControllerInfo createControllerInfo(int viewType);

}

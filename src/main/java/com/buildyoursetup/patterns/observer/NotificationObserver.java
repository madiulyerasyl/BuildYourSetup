package com.buildyoursetup.patterns.observer;

import com.buildyoursetup.model.PCBuild;

public class NotificationObserver implements BuildObserver {

    @Override
    public void onBuildChanged(PCBuild build) {

        System.out.println(
                "[NOTIFICATION] PC configuration updated: "
                        + build.getName()
        );
    }
}

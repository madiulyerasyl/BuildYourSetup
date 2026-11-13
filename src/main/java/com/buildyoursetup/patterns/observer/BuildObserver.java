package com.buildyoursetup.patterns.observer;

import com.buildyoursetup.model.PCBuild;

public interface BuildObserver {

    void onBuildChanged(PCBuild build);
}

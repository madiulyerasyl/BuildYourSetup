package com.buildyoursetup.service.validation;

import com.buildyoursetup.model.PCBuild;

public class CompletenessChecker {

    public boolean isComplete(PCBuild build) {
        return build != null && build.isComplete();
    }
}

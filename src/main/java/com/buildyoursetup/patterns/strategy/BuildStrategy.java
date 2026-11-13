package com.buildyoursetup.patterns.strategy;

import com.buildyoursetup.model.*;
import java.util.List;

public interface BuildStrategy {

    PCBuild recommend(
            String buildName,
            List<CPU> cpus,
            List<GPU> gpus,
            List<RAM> rams
    );

    String getStrategyName();
}

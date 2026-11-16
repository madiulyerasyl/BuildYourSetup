package com.buildyoursetup.persistence;

public class SavedBuild {

    private String id;
    private String name;

    private String cpuId;
    private String gpuId;
    private String ramId;

    private boolean rgb;
    private boolean warranty;

    public SavedBuild() {
        // Required by Jackson
    }

    public SavedBuild(
            String id,
            String name,
            String cpuId,
            String gpuId,
            String ramId,
            boolean rgb,
            boolean warranty
    ) {
        this.id = id;
        this.name = name;
        this.cpuId = cpuId;
        this.gpuId = gpuId;
        this.ramId = ramId;
        this.rgb = rgb;
        this.warranty = warranty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpuId() {
        return cpuId;
    }

    public void setCpuId(String cpuId) {
        this.cpuId = cpuId;
    }

    public String getGpuId() {
        return gpuId;
    }

    public void setGpuId(String gpuId) {
        this.gpuId = gpuId;
    }

    public String getRamId() {
        return ramId;
    }

    public void setRamId(String ramId) {
        this.ramId = ramId;
    }

    public boolean isRgb() {
        return rgb;
    }

    public void setRgb(boolean rgb) {
        this.rgb = rgb;
    }

    public boolean isWarranty() {
        return warranty;
    }

    public void setWarranty(boolean warranty) {
        this.warranty = warranty;
    }

    @Override
    public String toString() {
        return name;
    }
}

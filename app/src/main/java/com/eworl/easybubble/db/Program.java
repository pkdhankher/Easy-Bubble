package com.eworl.easybubble.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "PROGRAM".
 */
public class Program {

    private Long id;
    private String appName;
    private String appIcon;
    private String packageName;
    private Boolean isSelected;

    public Program() {
    }

    public Program(Long id) {
        this.id = id;
    }

    public Program(Long id, String appName, String appIcon, String packageName, Boolean isSelected) {
        this.id = id;
        this.appName = appName;
        this.appIcon = appIcon;
        this.packageName = packageName;
        this.isSelected = isSelected;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

}
package org.dragonsbaneproject.android.burstiq.dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Node implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("refName")
    @Expose
    private String refName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefName() {
        return refName;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

}
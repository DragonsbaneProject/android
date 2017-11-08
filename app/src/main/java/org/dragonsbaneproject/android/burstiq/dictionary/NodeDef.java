package org.dragonsbaneproject.android.burstiq.dictionary;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NodeDef implements Serializable {

    @SerializedName("arrays")
    @Expose
    private List<Array> arrays = null;
    @SerializedName("attributes")
    @Expose
    private List<Attribute> attributes = null;
    @SerializedName("defName")
    @Expose
    private String defName;
    @SerializedName("nodes")
    @Expose
    private List<Node> nodes = null;

    public List<Array> getArrays() {
        return arrays;
    }

    public void setArrays(List<Array> arrays) {
        this.arrays = arrays;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public String getDefName() {
        return defName;
    }

    public void setDefName(String defName) {
        this.defName = defName;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

}
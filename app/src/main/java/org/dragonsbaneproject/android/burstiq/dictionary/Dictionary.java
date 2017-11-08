package org.dragonsbaneproject.android.burstiq.dictionary;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dictionary implements Serializable {

    @SerializedName("client")
    @Expose
    private String client;
    @SerializedName("collection")
    @Expose
    private String collection;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("indexes")
    @Expose
    private List<Index> indexes = null;
    @SerializedName("nodeDefs")
    @Expose
    private List<NodeDef> nodeDefs = null;
    @SerializedName("rootnode")
    @Expose
    private Rootnode rootnode;
    @SerializedName("undefinedAttributesAction")
    @Expose
    private String undefinedAttributesAction;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Index> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<Index> indexes) {
        this.indexes = indexes;
    }

    public List<NodeDef> getNodeDefs() {
        return nodeDefs;
    }

    public void setNodeDefs(List<NodeDef> nodeDefs) {
        this.nodeDefs = nodeDefs;
    }

    public Rootnode getRootnode() {
        return rootnode;
    }

    public void setRootnode(Rootnode rootnode) {
        this.rootnode = rootnode;
    }

    public String getUndefinedAttributesAction() {
        return undefinedAttributesAction;
    }

    public void setUndefinedAttributesAction(String undefinedAttributesAction) {
        this.undefinedAttributesAction = undefinedAttributesAction;
    }

}
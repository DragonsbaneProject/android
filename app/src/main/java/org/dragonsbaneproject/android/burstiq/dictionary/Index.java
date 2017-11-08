package org.dragonsbaneproject.android.burstiq.dictionary;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Index implements Serializable {

    @SerializedName("attributes")
    @Expose
    private List<String> attributes = null;
    @SerializedName("sparse")
    @Expose
    private Boolean sparse;
    @SerializedName("unique")
    @Expose
    private Boolean unique;

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public Boolean getSparse() {
        return sparse;
    }

    public void setSparse(Boolean sparse) {
        this.sparse = sparse;
    }

    public Boolean getUnique() {
        return unique;
    }

    public void setUnique(Boolean unique) {
        this.unique = unique;
    }

}
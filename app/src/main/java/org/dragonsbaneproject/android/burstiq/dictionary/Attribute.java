package org.dragonsbaneproject.android.burstiq.dictionary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Attribute implements Serializable {

    @SerializedName("anonymizeOutput")
    @Expose
    private Boolean anonymizeOutput;
    @SerializedName("datatype")
    @Expose
    private String datatype;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("floatPrecision")
    @Expose
    private Integer floatPrecision;
    @SerializedName("hideOutput")
    @Expose
    private Boolean hideOutput;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("required")
    @Expose
    private Boolean required;

    public Boolean getAnonymizeOutput() {
        return anonymizeOutput;
    }

    public void setAnonymizeOutput(Boolean anonymizeOutput) {
        this.anonymizeOutput = anonymizeOutput;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getFloatPrecision() {
        return floatPrecision;
    }

    public void setFloatPrecision(Integer floatPrecision) {
        this.floatPrecision = floatPrecision;
    }

    public Boolean getHideOutput() {
        return hideOutput;
    }

    public void setHideOutput(Boolean hideOutput) {
        this.hideOutput = hideOutput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

}
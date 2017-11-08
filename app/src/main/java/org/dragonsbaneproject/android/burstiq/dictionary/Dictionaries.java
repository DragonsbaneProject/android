package org.dragonsbaneproject.android.burstiq.dictionary;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dictionaries implements Serializable {

    @SerializedName("dictionaries")
    @Expose
    private List<Dictionary> dictionaries = null;

    public List<Dictionary> getDictionaries() {
        return dictionaries;
    }

    public void setDictionaries(List<Dictionary> dictionaries) {
        this.dictionaries = dictionaries;
    }

}
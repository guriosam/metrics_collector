package br.ufal.ic.model;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danie
 */
public class Element implements IType {

    private String name;

    private ArrayList<Metric> metric;

    @Override
    public Type getType() {
        Type typeList = new TypeToken<List<Element>>() {
        }.getType();

        return typeList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Metric> getMetric() {
        return metric;
    }

    public void setMetric(ArrayList<Metric> metric) {
        this.metric = metric;
    }

    
}

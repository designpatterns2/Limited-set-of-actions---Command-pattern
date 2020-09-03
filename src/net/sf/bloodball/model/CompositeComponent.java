package net.sf.bloodball.model;


import java.util.HashMap;

public class CompositeComponent implements Component{
    private final HashMap<String, Component> childComponents = new HashMap();

    public void add(String key, Component component) {
        childComponents.put(key, component);
    }

    public Component getChild(String key) {
        return childComponents.get(key);
    }

    public void setChild(String key, Component component) {
        childComponents.replace(key, component);
    }
}

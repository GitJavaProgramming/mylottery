package org.pp.component.table.cell.cellCombo;

public class NameValue {

    private String name;
    private Object value;

    public NameValue() {
    }

    public NameValue(String name, Object val) {
        this.setName(name);
        this.setValue(val);
    }

    public String getValueString() {
        if (value == null){
            return "";
        }
        return value.toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String toString() {
        if (this.name == null){
            return "";
        }
        return this.name;
    }

}

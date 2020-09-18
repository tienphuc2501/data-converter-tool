package com.lexisnexis.mvr.config;

public class CsvColumn {
    private String name;
    //private int index;

    private String columnValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public int getIndex() {
        return index;
    }

    //public void setIndex(int index) {
        this.index = index;
    }*/

    public String getColumnValue() {
        return columnValue;
    }

    public void setColumnValue(String columnValue) {
        this.columnValue = columnValue;
    }

    @Override
    public String toString() {
        return "CsvColumn[" +
                "name='" + name + '\'' +
                //", index=" + index +
                ", columnValue=" + columnValue +
                ']';
    }
}

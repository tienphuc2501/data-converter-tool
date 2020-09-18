package com.lexisnexis.mvr.config;
public class RecordField {
    private String name;
    private int offset;
    private int length;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "RecordField[" +
                "fieldName='" + name + '\'' +
                ", offset=" + offset +
                ", length=" + length +
                ']';
    }
}

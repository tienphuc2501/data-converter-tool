package com.lexisnexis.mvr.config;

import java.util.List;

public class RecordType {

    private String recordCode;
    private int offset;
    private int length;
    private List<RecordField> fields;

    public String getRecordCode() {
        return recordCode;
    }

    public void setRecordCode(String recordCode) {
        this.recordCode = recordCode;
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

    public List<RecordField> getRecordFields() {
        return fields;
    }

    public void setRecordFields(List<RecordField> recordFields) {
        this.fields = recordFields;
    }

    @Override
    public String toString() {
        return "RecordType[" +
                "recordCode='" + recordCode + '\'' +
                ", offset=" + offset +
                ", length=" + length +
                ", fields=" + fields +
                ']';
    }
}

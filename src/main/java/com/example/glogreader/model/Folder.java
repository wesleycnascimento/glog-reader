package com.example.glogreader.model;

import java.util.ArrayList;
import java.util.List;

public class Folder {
    private String label;
    private int length;
    private int occurs;
    private List<Folder> children;

    // Constructor with label only
    public Folder(String label) {
        this(label, 0, 0);
    }

    // Constructor with label, length, and occurs
    public Folder(String label, int length, int occurs) {
        this.label = label;
        this.length = length;
        this.occurs = occurs;
        this.children = new ArrayList<>();
    }

    public String getLabel() {
        return label;
    }

    public int getLength() {
        return length;
    }

    public int getOccurs() {
        return occurs;
    }

    public List<Folder> getChildren() {
        return children;
    }

    public void addChild(Folder child) {
        children.add(child);
    }

    @Override
    public String toString() {
        return "Folder{" +
               "label='" + label + '\'' +
               ", length=" + length +
               ", occurs=" + occurs +
               ", children=" + children.size() +
               '}';
    }
}

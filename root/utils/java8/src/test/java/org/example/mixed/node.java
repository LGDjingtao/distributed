package org.example.mixed;

import lombok.Data;

import java.util.ArrayList;

@Data
public class node {
    Integer objId;
    String label;
    ArrayList<Integer> linkIds;
}


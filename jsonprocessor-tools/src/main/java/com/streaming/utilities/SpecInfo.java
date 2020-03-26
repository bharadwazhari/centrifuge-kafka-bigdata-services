package com.streaming.utilities;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpecInfo {
    private List<SpecNode> dlist = new ArrayList<>();
    private List<SpecNode> rlist = new ArrayList<>();
    private List<SpecNode> mlist = new ArrayList<>();
}

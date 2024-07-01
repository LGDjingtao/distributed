package org.example.mixed;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 测试线程池
 */
@Slf4j
public class NodeTest {


    static HashMap<Integer, Set<Integer>> map = new HashMap();

    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // 假设你有一个名为"example.json"的JSON文件
            List<LinkedHashMap> obj = mapper.readValue(new File("D:\\WORK\\distributed\\root\\utils\\java8\\src\\test\\java\\org\\example\\query_nodes.json"), List.class);

            obj.stream().map(v -> {
                node no = new node();
                new ArrayList<Integer>();
                no.setObjId((int) v.get("objId"));
                ArrayList<Integer> ls = (ArrayList<Integer>) v.get("linkIds");
                if (ls!=null) no.setLinkIds(ls);
                return no;
            }).forEach(v->{
                ArrayList<Integer> linkIds = v.getLinkIds();
                if (null != linkIds) {
                    v.getLinkIds().forEach(v2->{
                        Set<Integer> set = map.get(v2);
                        if (null == set) {
                            set = new HashSet<>();
                            map.put(v2,set);
                        }
                        set.add(v.getObjId());
                    });

                }

            });

            List<nodeXY> collect3 = obj.stream().map(v -> {
                nodeXY no = new nodeXY();
                new ArrayList<Integer>();
                no.setName( v.get("objId").toString());
                no.setLabel( v.get("label").toString());
                no.setX((int)v.get("posX"));
                no.setY((int)v.get("posY"));
                return no;
            }).collect(Collectors.toList());

            Collection<Set<Integer>> values = map.values();
            List<nodeLink> collect = values.stream().map(v -> {
                List<nodeLink> nolks = new ArrayList<>();
                List<Integer> stap = v.stream().collect(Collectors.toList());
                for (int i = 0; i < stap.size(); i++) {
                    for (int j = i + 1; j < stap.size(); j++) {
                        nodeLink nolk = new nodeLink();
                        nolks.add(nolk);
                        nolk.setSource(stap.get(i).toString());
                        nolk.setTarget(stap.get(j).toString());
                    }
                }
                return nolks;
            }).flatMap(v -> v.stream()).collect(Collectors.toList());
            System.out.println(JSON.toJSONString(collect3));
            System.out.println(JSON.toJSONString(collect));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

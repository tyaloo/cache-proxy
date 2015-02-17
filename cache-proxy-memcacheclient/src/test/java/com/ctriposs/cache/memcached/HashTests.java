package com.ctriposs.cache.memcached;

import com.ctriposs.cache.foroldlogic.Entities.MCluster;
import com.ctriposs.cache.foroldlogic.Entities.MGroup;
import com.ctriposs.cache.foroldlogic.Logic.KetamaHash;
import org.junit.*;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by tyaloo on 2015/2/12.
 */
public class HashTests {
    @Test
    public void TestHash(){
        MCluster cluster = new MCluster();
        try {
            ArrayList<MGroup> groups = new ArrayList<>();
            Path guidPath = Paths.get("d:\\temp\\guid.txt");
            Path keyPath = Paths.get("d:\\temp\\keys.txt");
            for(String guid:Files.readAllLines(guidPath, StandardCharsets.UTF_8)){

                MGroup group = new MGroup();
                group.setGroupId(UUID.fromString(guid));
                groups.add(group);
            }
            cluster.setGroups(groups);
            KetamaHash hashTool = new KetamaHash(cluster);

            for(String key:Files.readAllLines(keyPath, StandardCharsets.UTF_8)){

                System.out.println(key);
                System.out.println(hashTool.GetGroup(key).getGroupId().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

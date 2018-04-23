package org.tafia.tools;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Dason on 2018/4/22.
 */
public class YamlTest {

    @Test
    public void testDumpList() {
        Yaml yaml = new Yaml();
        
        System.out.println(yaml.dump(Collections.singletonMap("keys", Arrays.asList(Collections.singletonMap("name", "1")))));
    }
}

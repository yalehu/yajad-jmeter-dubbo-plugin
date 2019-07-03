package com.yajad.jmeter.parse;

import com.yajad.jmeter.dto.DubboParamDto;
import org.snakeyaml.engine.v1.api.Load;
import org.snakeyaml.engine.v1.api.LoadSettings;
import org.snakeyaml.engine.v1.api.LoadSettingsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class YamlParamParser {

    @SuppressWarnings("unchecked")
    public static DubboParamDto parseParameter(String yaml) {
        LoadSettings settings = new LoadSettingsBuilder().build();
        Load load = new Load(settings);
        Object params = load.loadFromString(yaml);

        List<String> dubboParamTypes = new ArrayList<>();
        List<Object> dubboParamValues = new ArrayList<>();

        if (params instanceof Map) {
            //        com.xx.param1:
            //            a: 1
            //            b: 2
            //        com.xxx.param2:
            //            c: 1
            //            d: 2
            for (Map.Entry<String, Object> entry : ((Map<String, Object>) params).entrySet()) {
                dubboParamTypes.add(entry.getKey());
                dubboParamValues.add(entry.getValue());
            }
        } else if (params instanceof List) {
            //        - com.xx.param1:
            //            a: 1
            //            b: 2
            //        - com.xxx.param2:
            //            c: 1
            //            d: 2
            //        - 1
            //        - 2
            //        - abc
            for (Object object : (List<Object>) params) {
                Object value = object;
                if (object instanceof Map) {
                    for (Map.Entry<String, Object> entry : ((Map<String, Object>) object).entrySet()) {
                        value = entry.getValue();
                        dubboParamTypes.add(entry.getKey());
                        break;
                    }
                } else {
                    dubboParamTypes.add(value.getClass().getName());
                }
                dubboParamValues.add(value);
            }
        }

        return new DubboParamDto(dubboParamTypes, dubboParamValues);
    }
}
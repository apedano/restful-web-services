/*
 * COPYRIGHT NOTICE
 * © 2020  Transsmart Holding B.V.
 * All Rights Reserved.
 * All information contained herein is, and remains the
 * property of Transsmart Holding B.V. and its suppliers,
 * if any.
 * The intellectual and technical concepts contained herein
 * are proprietary to Transsmart Holding B.V. and its
 * suppliers and may be covered by European and Foreign
 * Patents, patents in process, and are protected by trade
 * secret or copyright law.
 * Dissemination of this information or reproduction of this
 * material is strictly forbidden unless prior written
 * permission is obtained from Transsmart Holding B.V.
 */
package it.apedano.webservices.restfulwebservices.filtering;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import java.util.Arrays;
import java.util.List;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Alessandro Pedano <alessandro.pedano@transsmart.com>
 */
@RestController
public class FilteringController {

    @GetMapping("/static-filtering")
    public StaticFilteredBean retrieveStaticFilteredBean() {
        return new StaticFilteredBean("value1", "value2", "value3");
    }

    @GetMapping("/static-filtering-list")
    public List<StaticFilteredBean> retrieveStaticFilteredBeanList() {
        return Arrays.asList(
                new StaticFilteredBean("value11", "value12", "value13"),
                new StaticFilteredBean("value21", "value22", "value23")
        );
    }

    @GetMapping("/dynamic-filtering")
    public MappingJacksonValue retrieveSomeBean() {
        DynamicallyFilteredBean unfilteredBean = new DynamicallyFilteredBean("value1", "value2", "value3");
        MappingJacksonValue mapping = new MappingJacksonValue(unfilteredBean);
        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field2"); //only field3 remains
        FilterProvider filters = new SimpleFilterProvider().addFilter("DynamicFilter", filter);
        mapping.setFilters(filters);
        return mapping;

    }

    @GetMapping("/dynamic-filtering-list")
    public MappingJacksonValue retrieveDynamicFilteredBeanList() {
        List<DynamicallyFilteredBean> list = Arrays.asList(
                new DynamicallyFilteredBean("value11", "value12", "value13"),
                new DynamicallyFilteredBean("value21", "value22", "value23"));
        MappingJacksonValue mapping = new MappingJacksonValue(list);
        SimpleBeanPropertyFilter dynamicFilter = SimpleBeanPropertyFilter.filterOutAllExcept("field1", "field2"); //only field3 remains
        FilterProvider filters = new SimpleFilterProvider().addFilter("DynamicFilter", dynamicFilter);
        mapping.setFilters(filters);
        return mapping;

    }

}

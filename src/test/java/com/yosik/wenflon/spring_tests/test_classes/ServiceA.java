package com.yosik.wenflon.spring_tests.test_classes;

import com.yosik.wenflon.WenflonList;


@WenflonList(conditions = "${non-wenflon-properties.not-cute}")
public class ServiceA implements Testable {
}

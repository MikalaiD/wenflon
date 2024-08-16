<<<<<<<< HEAD:examples/basic/src/main/java/com/yosik/examples/basic/App.java
package com.yosik.examples.basic;
========
package com.github.mikalaid.examples.basic;
>>>>>>>> develop:examples/basic/src/main/java/com/github/mikalaid/examples/basic/App.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
![logo.png](logo.png)
# Wenflon

## Intro
This is a simple feature switch tool to solve the following problem:

Having Spring, hexagonal architecture and living production code 
several versions of the same adapter could arise at a given point of time - e.g. when the newer version 
developed but is rolled out for a limited group of users.
Often it results in temp code a-la ```codition ? adapterA : adapterB``` - every time in different places, 
every time for removal in many places later.
Wenflon solves this problem by introducing a @Wenflon annotation for a frequently updated port. 
All the implementations of the port will then be hidden behind the proxy which will become the primary bean.
Finally, based on the combination of application properties (**conditions**) 
and some decision critical runtime value (**pivot**) wenflon will decide in runtime which concrete implementation to use.

## Basic example
Assuming there is a simple web-app which calculates customer risk level based on some data. 
The app has _DecisionEngine_ interface to return rank and _StrInsuranceRiskEngine_ to implement this functionality.
Naturally, with time a better implementation of DecisionEngine appears - _NewInsuranceRiskEngine_. 
To replace one implementation with another we want a mechanism with certain requirements:
- gradual rollout over simple switch - to limit negative consequences if works not as expected;
- rollout to be controlled via properties, no changes in codebase;
- finishing rollout requires minimum or none cleanup in codebase;
- any future similar switch requires minimum change in codebase.

For this example we decide to make a gradual rollout in the following way - for users from EU and SOUTH_KOREA we want to
keep using _StrInsuranceRiskEngine_ and users from US will be facing new functionality provided by _NewInsuranceRiskEngine_.

We add dependency to the project:
```xml
        <dependency>
            <groupId>com.github.mikalaid</groupId>
            <artifactId>wenflon-core</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </dependency>
```
Then create 2 beans - WenflonBeanPostprocessor and PivotProvider<String>. PivoteProvider is a bean that will return **pivot** at runtime 
The **pivot** is the value based on which the decision on which implementation to be used is made. In this example - pivot is user's assigned market value, 
which we obtain from _SecurityContext_. 
```java
    @Bean
    WenflonBeanPostprocessor wenflonBeanPostprocessor() {
        return new WenflonBeanPostprocessor();
    }

    @Bean
    PivotProvider<String> defaultPivotProvider(){
        return ()-> Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(CustomUserDetails.class::cast)
                .map(CustomUserDetails::getMarket)
                .map(Market::name)
                .orElseThrow();
    }
```
Then annotate _DecisionEngine_ with _@Wenflon_
```java
@Wenflon
public interface DecisionEngine {
    String rank();
}
```
Final step - in the application properties add conditions under which old or new implementation should be used.
```yaml
wenflon:
  conditions:
    stdInsuranceRiskEngine: EU, REPUBLIC_OF_KOREA
    newInsuranceRiskEngine: US
```

Run the app and call the endpoint with different users to see results 
```shell
curl -u euUser:password1 http://localhost:8080/rank
```
```shell
curl -u usUser:password2 http://localhost:8080/rank
```

### Behaviour if single implementation is found

#### default behaviour 
//todo #15

#### strict behaviour TODO
If property ```wenflon.strict``` is set to true (default is false) then
proxy will require presence of an appropriate condition. In case pivot value provided by pivot provider does not
meet this sole condition, the application will throw a SomeRuntimeException (TODO)
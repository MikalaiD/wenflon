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
            <groupId>io.github.mikalaid</groupId>
            <artifactId>wenflon-core</artifactId>
            <version>0.0.1</version>
        </dependency>
```
Then create PivotProvider<String> bean - it will return **pivot** at runtime.
The **pivot** is the value based on which the decision on which implementation to be used is made. In this example - pivot is user's assigned market value, 
which we obtain from _SecurityContext_. 
```java
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
    newInsuranceRiskEngine: US, default
```

Run the app and call the endpoint with different users to see results 
```shell
curl -u euUser:password1 http://localhost:8080/rank
```
```shell
curl -u usUser:password2 http://localhost:8080/rank
```

## MULTIPLE PROVIDERS EXAMPLE
Scenario when one cannot rely just on single pivot provider is most likely.
Let's then extend the previous example by adding another interface with @Wenflon.

First, we add another interface annotated with @Wenflon as well as simple implementation and controller:
```java
@Wenflon
public interface KYCScanner {
    // Know Your Customer (KYC) - guidelines and regulations in financial services require professionals to verify
    // the identity, suitability, and risks involved with maintaining a business relationship with a customer (Wiki)
    String checkCustomer();
}
```
```java
@Service
public class ShallowKYCScanner implements KYCScanner {
    @Override
    public String checkCustomer() {
        return "Looks legit to me... Approved";
    }
}
```

```java
@RestController
@AllArgsConstructor
public class KYCController {

    private final KYCScanner kycScanner;

    @GetMapping(path = "/scan")
    public ResponseEntity<String> scan(){
        return ResponseEntity.ok(kycScanner.checkCustomer());
    }
}
```
If we run the application at this stage and call the _/scan_ endpoint it will just work fine, even though neither wenflon conditions nor new pivot provider has been specified.
```shell
curl -u euUser:password1 http://localhost:8080/scan
```
The reason is implicit default implementation - if there is only one implementation @Wenflon jus uses it (see more in [Default Implementation](#default-implementation))

Let's assume there is a business need to add a newer, more thorough scanner:
```java
@Slf4j
@Service
public class ThoroughKYCScanner  implements KYCScanner {
    @Override
    @SneakyThrows
    public String checkCustomer() {
        log.info("Verifying client's genealogy tree...");
        Thread.sleep(5000);
        return "We can trust this gentleman";
    }
}
```
If we try running the application now, it will run as well. Calling _**/rank**_ endpoint will work as well. However, the _**/scan**_
endpoint will return 500 and the _WenflonException_ under the hood.
```shell
curl -u euUser:password1 http://localhost:8080/rank
```
```shell
curl -u euUser:password1 http://localhost:8080/scan
```
The reason is simple - lack of conditions. Having more than one implementation @Wenflon cannot decide which to use.
This issue could be resolved simply by specifying in properties that _shallowKYCScanner_ to be used by default while for users from EU 
the _thoroughKYCScanner_ must be used.

```yaml
wenflon:
  conditions:
    stdInsuranceRiskEngine: EU, REPUBLIC_OF_KOREA
    newInsuranceRiskEngine: US, default
    shallowKYCScanner: default
    thoroughKYCScanner: EU
```
Quick check presents expected result:
```shell
curl -u euUser:password1 http://localhost:8080/scan
```
```shell
curl -u usUser:password2 http://localhost:8080/scan
```
Now, what if business requirement here is the following - since throw KYC scanner is too thorough and slow we want to apply it 
to everyone in all countries except VIP clients, cause those are too important to wait.

We start by adding a new pivot provider:
```java
    @Bean
    PivotProvider<String> rolloutGroupPivotProvider(){
        return ()-> Optional.of(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .map(CustomUserDetails.class::cast)
                .map(CustomUserDetails::getRolloutGroup)
                .map(FeaturesRolloutGroup::name)
                .orElseThrow();
    }
```

If we try and run the application now the _BeanCreationException_ will be thrown since at this point it is not clear for @Wenflon
which pivot provider to use for which case. This can be resolved by simply providing pivot bean name inside @Wenflon annotation:
```java
@Wenflon(pivotProviderBeanName = "rolloutGroupPivotProvider")
public interface KYCScanner {}
```
```java
@Wenflon(pivotProviderBeanName = "defaultPivotProvider")
public interface DecisionEngine {}
```
Also need to change conditions values for KYC since there is a new bean:
```yaml
wenflon:
  conditions:
    stdInsuranceRiskEngine: EU, REPUBLIC_OF_KOREA
    newInsuranceRiskEngine: US, default
    shallowKYCScanner: VIP
    thoroughKYCScanner: default
```

Now the app runs without problem and you can check the outcomes:
... for _**/rank**_ endpoint:
```shell
curl -u euUser:password1 http://localhost:8080/rank
```
```shell
curl -u usUser:password2 http://localhost:8080/rank
```
```shell
curl -u usUserVIP:password3 http://localhost:8080/rank
```
... and for _**/scan**_ endpoint:
```shell
curl -u euUser:password1 http://localhost:8080/scan
```
```shell
curl -u usUser:password2 http://localhost:8080/scan
```
```shell
curl -u usUserVIP:password3 http://localhost:8080/scan
```

## DETAILS
### Default Implementation
Conditions recognize ```default``` keyword which can be used together with other conditions. The ```default```
will instruct wenflon to use marked implementation in case pivot value is not 
found in any of given conditions. 

In the adjusted example below properties would mean that for any user not from US market 
the _stdInsuranceRiskEngine_ will be used.
```yaml
wenflon:
  conditions:
    stdInsuranceRiskEngine: default
    newInsuranceRiskEngine: US
```
For more readability, ```default``` can be used with other conditions. The example below will produce the same result 
as the one above:

```yaml
wenflon:
  conditions:
    stdInsuranceRiskEngine: EU, default
    newInsuranceRiskEngine: US
```
At the same time only ```1``` default implementations are allowed per wenflon. The example below will
throw _BeanCreationException_:

```yaml
wenflon:
  conditions:
    stdInsuranceRiskEngine: EU, default
    newInsuranceRiskEngine: US, default
```

#### Implicit default implementation
If there is only **one implementation** of an interface annotated with _@Wenflon_ and **no conditions** is provided for this implementation,
then this implementation is considered as default implementation.

If there is only **one implementation** of an interface annotated with _@Wenflon_ and **condition is present** for it in properties,
then this implementation is considered as default implementation depending on ```soleConditionalImplAsImplicitDefault``` @Wenflon property value:
- if ```true``` (_by default_) then this implementation is considered as default implementation, and the condition won't be tested;
- if ```false``` then this implementation is considered as conditional implementation, and will be used only if the condition is met; otherwise exception will be thrown.

### Multiple Pivot Providers
If 1 pivot provider is specified, @Wenflon annotated interfaces may not specify the pivot provider bean name.
Despite the number of @Wenflon annotated interfaces declared if pivot provider bean name is specified but not found among pivot provider beans - the _BeanCreationException_ will be thrown. At the same time there can be more pivot providers in context than declared wenflons. 
If there is one @Wenflon annotated interface without specifying pivot provider bean name and there are 2+ pivot provider beans then, naturally, 
the _BeanCreationException_ will be thrown since it is impossible to define which provider to match with. 
If there is only one pivot provider bean in context and there are only @Wenflon annotations without specifying the provider to use - this pivot provider will be used by default.
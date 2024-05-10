# Wenflon

![img_1.png](img_1.png)

## Intro
This is simple feature switch tool. Nothing fancy, no ui. Designed to solve the following trivial problem:
As an average Java programmer, who has Spring, hexagonal architecture and living production code I often have
several versions of the same adapter at a given point of time - when the newer version is rolled out for a limited range of cases.
Usually it results in code a-la ```codition ? adapterA : adapterB``` - every time in different places, every time for removal in many places later.
What I wanted is to annotate a frequently updated port once with a special annotation and force Spring to discover all 
implementation and then use different implementation depending on conditions set via application properties.

### Behaviour if single implementation is found

#### default behaviour 
If there is only one bean of the implemented interface then pivot provider
and conditions from properties are ignored 
and proxy returns passes all the calls directly to the bean.

#### strict behaviour TODO
If property ```wenflon.strict``` is set to true (default is false) then
proxy will require presence of an appropriate condition. In case pivot value provided by pivot provider does not
meet this sole condition, the application will naturally throw a SomeRuntimeException (TODO)
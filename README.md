# wenflonDynamicProxy

### Behaviour if single implementation is found

#### default behaviour 
If there is only one bean of the implemented interface then pivot provider
and conditions from properties are ignored 
and proxy returns passes all the calls directly to the bean.

#### strict behaviour TODO
If property ```wenflon.strict``` is set to true (default is false) then
proxy will require presence of an appropriate condition. In case pivot value provided by pivot provider does not
meet this sole condition, the application will naturally throw a SomeRuntimeException (TODO)
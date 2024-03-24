## Pivot - the variable against which wenflon condition should be tested - e.g. country
1. Specify the "pivot" -> the context variable which should be taken into account for wenflon decision
2. By default it should be just name of the property directly in context.
3. Later should be added an option of specifying the factory bean of extracting pivot variable 


## Condition - values specified per wenflon and tested against pivot
1. Should be 2 types of conditions - ab (boolean) and list.
2. Conditions type (i.e. wenflon type) should be specified in the annotation (ab or list)
3. AB wenflon should track & control that no more than 2 instances (A & B) are available
4. List wenflon should read from properties or specify directly the list of conditions
5. An error must be thrown if there is mismatch between annotated wenflon concrete implementations
6. If list - implementations should be checked if conditions are not repeated
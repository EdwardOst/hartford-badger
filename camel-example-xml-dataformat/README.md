Camel `jacksonxml` dataformat will convert xml into a potentially nested Hashmap.

With Camel Simple's support Map interfaces, this is all you need to extract fields for processing.

Of course, you could extract fields via xpath as well without the overhead of unmarshalling.

So a more typical use case would be to do more complex processing against the Hashmap object, e.g. applying SpringExpression language.

Unmarshalling to a specific class is supported by passing a class to the formatter.
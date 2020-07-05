### Camel Avro Data Format

Resources in `src/main/resources`

* `person.json` : sample data in json format.
* `person.avsc` : Avro schema derived from `sample-person-message.json` using [Avro-Schema-from-Json](https://toolslick.com/generation/metadata/avro-schema-from-json) online tool
* `person.avro` : Avro file derived from `person.json` and `person.avsc` using Avro command line tool
* `person.sz` : Avro file with snappy compression derived from `person.json` and `person.avsc` using Avro command line tool
* `person.avro.json` : json file extracted from `person.avro` using Avro command line tool
* `person.avro.avsc` : Avro schema file extracted from `person.avro` using Avro command line tool
* `person.sz.json` : json file extracted from `person.sz` using Avro command line tool
* `person.sz.avsc` : Avro schema file extracted from `person.sz` using Avro command line tool

````
# person.json is provided
# run Avro-Schema-from-Json to create person.avsc

# create person.avro from schema and json files
java -jar avro-tools-1.10.0.jar fromjson --schema-file person.avsc person.json > person.avro

# extract json from person.avro
java -jar avro-tools-1.10.0.jar tojson person.avro > person.avro.json

# extract schema from person.avro
java -jar avro-tools-1.10.0.jar getschema person.avro > person.avro.avsc

# create compressed person.sz from schema and json files
java -jar avro-tools-1.10.0.jar fromjson --codec snappy --schema-file person.avsc person.json > person.sz

# extract json from person.sz
java -jar avro-tools-1.10.0.jar tojson person.sz > person.sz.json

# extract schema from person.sz
java -jar avro-tools-1.10.0.jar getschema person.sz > person.sz.avsc

````
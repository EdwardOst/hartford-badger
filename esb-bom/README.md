apache-esb POM
--------------

This is a platform pom that uses dependencyManagement imports of the parent poms
from the component Apache projects to establish a consistent set of dependency
versions for use as a baseline of comparison with talend-esb and apps built
on the tesb platform pom.

This encapsulates versions for dependencies and provides a
single point of control for Talend dependencies.  Note that the "apache
platform" just refers to the fact that the project is intended to compose
multiple apache projects into a logical "platform".  It is not an Apache
community project although it is apache v2 licensed.
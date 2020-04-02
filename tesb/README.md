Talend ESB POM
--------------

It is recommended that explicit dependencyManagement for the Talend ESB be
centralized.

This platform bom pom uses dependencyManagement imports 
for component Apache projects and Talend ESB to establish a consistent
set of dependency versions for use by apps built on Talend ESB.

It also provides a single point of control to tweak dependencies if needed, 
e.g. the spring framework to exclude commons logging.

In general, properties defined here should match
those found in the tesb pom for org.talend.esb:esb-parent

It adds the Spring bom pom and used by Talend ESB. 

There are some problems which can arise with this approach on occasions.  For
example, Talend ESB 5.2 uses Karaf 2.2.9 but substitutes Jetty 7.6.7 for the
default Jetty 7.5.4 used in Karaf.  The resulting dependencyManagement imports
provide the Jetty 7.5.4 via the Karaf parent pom.  This may be sufficient
fidelity in development in most cases since unit testing can be done without
using Jetty using camel-maven:run.  But if the jetty-maven-plugin is used or
other details of jetty configuration are impacted then these differences could
have an impact.

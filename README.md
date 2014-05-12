restapi-doc
===========

Maven Plugin for generating documentation for spring mvc RESTFul apis

The plugin consist of 2 components:

* A Doclet for collecting Javadoc Comments of Class, Field, Methods and Parameters storing them unter META-INF
* A Maven Plugin for generating the actual documentation


## Doclet usage inside maven

Generates the comments file during maven cmpile phase

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>2.8</version>
    <extensions>true</extensions>
    <executions>
        <execution>
            <phase>compile</phase>
            <goals>
                <goal>javadoc</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <doclet>de.mvbonline.tools.restapidoc.doclet.JavadocToXMLDoclet</doclet>
        <docletArtifact>
            <groupId>de.mvbonline</groupId>
            <artifactId>restapi-doc</artifactId>
            <version>0.0.1-SNAPSHOT</version>
        </docletArtifact>
        <useStandardDocletOptions>false</useStandardDocletOptions>
        <reportOutputDirectory>${project.build.outputDirectory}/META-INF</reportOutputDirectory>
    </configuration>
</plugin>
```

## Maven Plugin

The Plugin checks for spring mvc @Controller annotated Classes and Methods annotated with @ResponseBody and build a documentation for them. It outputs sphinx reStructuredText using the sphinxcontrib.httpdomain.

Domain models in Parameters or return value are parsed with Jackson and the resulting model info will also be documented.

### Parameters

* packageName : Package name for checking for Controllers
* sourceDocFolder : Folder to store the generated doc
* jacksonObjectMapper : full qulified Classname of an Jackson2 ObjectMapper

### Example bind pre-site generation
```xml
<plugin>
    <groupId>de.mvbonline</groupId>
    <artifactId>restapi-doc</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <executions>
        <execution>
            <phase>pre-site</phase>
            <goals>
                <goal>generatedocs</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <!-- Package to scan -->
        <packageName>${rest.api.package.name}</packageName>
    </configuration>
</plugin>
```

## Todo

- [ ] Add Exeptions
- [ ] Add HTTPStatuscodes
- [x] Support nested domain models
- [x] Add annotations of fields
- [x] Add Blacklist for Annotations, Classes, Methods and Fields

Nested Domain models are now retrieved via recursion, this means, that the Blacklists are very big, because we needed
to blacklist all standard java objects. This should be avoided by probably using the @Entity annotation and/or using
package names on Classes as well.
# Ewon Flexy Extensions Library (sc-ewon-flexy-extensions-lib)

THE PROJECT IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND. HMS DOES NOT WARRANT THAT THE FUNCTIONS OF THE PROJECT WILL MEET YOUR REQUIREMENTS, OR THAT THE OPERATION OF THE PROJECT WILL BE UNINTERRUPTED OR ERROR-FREE, OR THAT DEFECTS IN IT CAN BE CORRECTED.
---

## [Table of Contents](#table-of-contents)

1. [Description](#description)
   1. [Required Firmware Version](#required-firmware-version)
   2. [Libraries and Dependencies](#libraries-and-dependencies)
      1. [Solution Center Repository](#solution-center-repository)
2. [Development Environment](#development-environment)
3. [Contributing](#contributing)

---

## [Description](#table-of-contents)

A library of extensions to the functionality of the Ewon Flexy Java environment (Ewon ETK), created by the HMS Networks, MU Americas Solution Center.

### [Required Firmware Version](#table-of-contents)

This project requires a minimum Ewon firmware version of 14.0 or higher. Older firmware versions may be incompatible and are not supported.

### [Libraries and Dependencies](#table-of-contents)

1. Ewon ETK
   ```xml
   <dependencies>
      ...
      <dependency>
         <groupId>com.hms_networks.americas.sc.mvnlibs</groupId>
         <artifactId>ewon-etk</artifactId>
         <version>1.4.4</version>
         <scope>provided</scope>
      </dependency>
      ...
   </dependencies>
   ```
   _Note: The scope must be set to 'provided' for the Ewon ETK dependency. This indicates that the library is provided by the system and does not need to be included in the packaged JAR file._
2. JUnit
   ```xml
   <dependencies>
      ...
      <dependency>
         <groupId>junit</groupId>
         <artifactId>junit</artifactId>
         <version>3.8.1</version>
         <scope>test</scope>
      </dependency>
      ...
   </dependencies>
   ```
   _Note: The scope must be set to 'test' for the JUnit dependency. This indicates that the library is required for code testing and does not need to be included in the packaged JAR file._


As required, you can include additional libraries or dependencies using the Maven build system. To add a new library or dependency, add a new `<dependency></dependency>` block in the `<dependencies></dependencies>` section of your `pom.xml`. For example, to use this library, you would add the following to your pom.xml:

```xml
<dependencies>
   ...
   <dependency>
      <groupId>com.hms_networks.americas.sc</groupId>
      <artifactId>extensions</artifactId>
      <version>1.5.0-pre1</version>
   </dependency>
   ...
</dependencies>
```

#### [Solution Center Repository](#table-of-contents)

This project and the Ewon ETK are available via the Solution Center Maven repository.

If the Solution Center Maven repository is not already included in your project, you can include it by adding the following `<repository></repository>` block in the `<repositories></repositories>` section of your `pom.xml` as follows:
```xml
<repositories>
   ...
   <!-- HMS Networks, MU Americas Solution Center Maven Repo -->
   <repository>
      <id>sc-java-maven-repo</id>
      <name>HMS Networks, MU Americas Solution Center Maven Repo</name>
      <url>https://github.com/hms-networks/sc-java-maven-repo/raw/main/</url>
   </repository>
   ...
</repositories>
```

## [Development Environment](#table-of-contents)

This project is based on the [Solution Center Maven Starter Project](https://github.com/hms-networks/sc-java-maven-starter-project), and uses the Maven build system for compilation, testing, and packaging.

Maven lifecycle information and other details about the development environment provided by the [Solution Center Maven Starter Project](https://github.com/hms-networks/sc-java-maven-starter-project) can be found in its README.md at [https://github.com/hms-networks/sc-java-maven-starter-project/blob/main/README.md](https://github.com/hms-networks/sc-java-maven-starter-project/blob/main/README.md).

## [Contributing](#table-of-contents)

Detailed information about contributing to this project can be found in [CONTRIBUTING.md](CONTRIBUTING.md).
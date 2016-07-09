# Description

Program to analyze mails stored into a Microsoft Outlook PST file and find one based on search keywords.
There no need of Microsoft Outlook on the target computer.

This project is implemented using Maven (v3.x) structure.

# Build the program

The last version (**0.9.1**) of the artifact **java-libpst** is not yet published on Maven public repository then you must install the library prior to program build, this using the following command line:

`mvn install:install-file -Dfile=ext/java-libpst-0.9.1.jar -DgroupId=com.pff -DartifactId=java-libpst -Dversion=0.9.1 -Dpackaging=jar`

And then use the following Maven command line to build the program jar file:

`mvn clean package`

The program will be build as the file **target/pst-digger.jar**

# CloudBees build status

[![Build Status](https://righettod.ci.cloudbees.com/buildStatus/icon?job=PSTDigger)](https://righettod.ci.cloudbees.com/job/PSTDigger/)

[Download](https://righettod.ci.cloudbees.com/job/PSTDigger/lastSuccessfulBuild/artifact/target/pst-digger.jar) last build.

# Running requirement

The program need a JRE <= 1.8

# Usage

Use the following command to display the available parameters:

`java -jar pst-digger.jar`

```
Usage:
 -a      : Dump linked attachments for interesting mails found (default: false)
 -f FILE : PST file to analyze (absolute or relative path)
 -i      : Perform search of terms in case sensitive way (default: false)
 -k VAL  : Keyword to search in mail (subject/body), separated by a pipe '|'
 -o FILE : Folder in which store copy of interesting mails containing the keywords specified (default: out)

Example using required parameters:
java -jar pst-digger.jar -f FILE -k VAL

Example using all parameters:
java -jar pst-digger.jar -a -f FILE -i -k VAL -o FILE
```

# Thanks

Thanks you to **rjohnsondev** for the Java version of the [LibPST](https://github.com/rjohnsondev/java-libpst) library.





 

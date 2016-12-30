# Description

Program to analyze mails stored into a Microsoft Outlook PST file and find one based on search keywords.
There no need of Microsoft Outlook on the target computer.

This project is implemented using Maven (v3.x) structure.

# Build the program

Use the following Maven command line to build the program jar file:

`mvn clean package`

The program will be build as the file **target/pst-digger.jar**

# Travis-CI build status

[![Build Status](https://travis-ci.org/righettod/pst-digger.svg?branch=master)](https://travis-ci.org/righettod/pst-digger)

# Running requirement

The program need a JRE 1.8

# Usage

Use the following command to display the call parameters:

`java -jar pst-digger.jar`

```
Usage:
 -a      : Dump linked attachments for interesting mails found (default: false)
 -f FILE : PST file to analyze (absolute or relative path)
 -i      : Perform search of terms in case sensitive way (default: false)
 -k VAL  : Keywords to search in mail (subject/body), separated by a pipe '|'
 -o FILE : Folder in which store copy of interesting mails containing the keywords specified (default: out)

Example using required parameters:
java -jar pst-digger.jar -f FILE -k VAL

Example using all parameters:
java -jar pst-digger.jar -a -f FILE -i -k VAL -o FILE
```

Real call example:

`java -jar pst-digger.jar -a -f "C:\Users\xxx\my.pst" -i -k "login|password" -o TEST01`

# Thanks

Thanks you to **rjohnsondev** for the Java version of the [LibPST](https://github.com/rjohnsondev/java-libpst) library.

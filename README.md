# Description

Program to analyze mails stored into a Microsoft Outlook PST file and find one based on search keywords.
There no need of Microsoft Outlook on the target computer.

# Build the program

Use the following Maven command line:

`mvn clean package`

The program will be build in the folder **target/pst-digger.jar**

# CloudBees build status

[![Build Status](https://righettod.ci.cloudbees.com/buildStatus/icon?job=PSTDigger)](https://righettod.ci.cloudbees.com/job/PSTDigger/)

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





 

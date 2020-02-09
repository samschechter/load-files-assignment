# logikcull-loadfiles

## Overview

This assignment is intended to model a typical request to add new features to our document processing pipeline. As part of our team, you will be asked to work independently on such features, select a solution that can be completed in 4-6 hours but not more than 8, and submit that feature for collaborative review by one or more colleagues.

In this assignment, you develop the code required for the solution, and importantly provide a sample pull-request description that addresses all of the considerations in the Deliverables section below. We are looking for colleagues that can not only complete the assignment, but also communicate the pros and cons of their approach clearly and succinctly to thereby contribute to overall team velocity. Place your pull request description in the same directory (i.e. PR.md).

## Context

Logikcull customers can import pre-processed documents into Logikcull by providing an archive in either ZIP or TAR format. The archive must contain a "loadfile" as well as the actual documents to be imported. 

A loadfile contains case-specific information about documents that we need to track, including:
  - Control numbers used to uniquely identify documents before a court
  - Volume names that group documents into collections
  - A relative path to the actual document provided in the corresponding archive.

## Tasks

1. Logikcull currently only supports the OPT loadfile format. Many of our customers want to use other common load file formats. Please modify our existing code to support additional loadfile formats while still supporting OPT. Examples of these additional formats can be found below.

2. Logikcull needs to validate the correctness of the loadfile. Implement a validation process that ensures, at minimum, that the paths in the loadfile all have corresponding documents in the archive. Also, to demonstrate that your approach is easily extensible by other developers on the team, implement at least one other validation on the metadata for each loadfile entry such as the presence of all required fields, valid file extensions, or control numbers matching a particular pattern.

## Load File Formats

For the first task we need to add support for the following load file formats:

### LFP

LFP is a special case of the common CSV file format. An example of LFP data follows:
``` csv
IM,test-000001,S,0,@Import Test 01;IMAGES/001/;test-000001.tif;2,0
IM,test-000002,C,0,@Import Test 01;IMAGES/001/;test-000002.tif;2,0
IM,test-000003,D,0,@Import Test 01;IMAGES/001/;test-000003.tif;2,0
```

The columns in this example indicate, in order:
1. The type of document being imported. In this case "IM" means the document is an image. For the purposes of this exercise we can ignore this column.
2. The control number, e.g. "test-000001". Note that a "control number" is not strictly a numeric value!
3. Ignore.
4. Ignore.
5. Four fields prefixed by `@` and separated by `;`: the name of the volume to which the document belongs; the dirname of the document in the archive; the basename; and the page count of the document. The page count we can ignore for the purposes of this exercise.
6. Ignore.

### XLF

XLF files contain document metadata represented in XML. An example:

``` xml
<loadfile>
  <entries>
    <entry control-number="test-000001">
        <volume>Import Test 01</volume>
        <image-path>IMAGES/001/</image-path>
        <image-name>test-000001.tif</image-name>
    </entry>
    <entry control-number="test-000002">
        <volume>Import Test 01</volume>
        <image-path>IMAGES/001/</image-path>
        <image-name>test-000002.tif</image-name>
    </entry>
    <entry control-number="test-000003">
        <volume>Import Test 01</volume>
        <image-path>IMAGES/001/</image-path>
        <image-name>test-000003.tif</image-name>
    </entry>
  </entries>
</loadfile>
```

## Loadfile schematization

All load file formats resolve to the following data:

| Control Number      | Volume              | Path                             |
| ------------------- | ------------------- | -------------------------------- |
| test-000001         | Import Test 01      | IMAGES/001/test-000001.tif       |
| test-000002         | Import Test 01      | IMAGES/001/test-000002.tif       |
| test-000003         | Import Test 01      | IMAGES/001/test-000003.tif       |

For the purpose of simplicity on the second task we can assume that the contents of the archive have already been extracted to a local filesystem. You don't need to know anything about ZIP or TAR for this exercise.

## Deliverables

This assignment is designed to evaluate how well you can work with an existing codebase and how you approach software engineering tasks given a set of constraints. There are no right or wrong answers in this exercise; you are leading this project. We are evaluating how you think about and implement solutions to problems. We are looking for candidates that not only code efficiently but also engage well with our team on the tradeoffs and architectural dilemmas of our craft.

Overall, you will be evaluated on the following metrics, **all of which should be addressed by sections in your pull request description**:

* _Completeness_

   Does the candidate complete the tasks to specification? Does the candidate demonstrate their solution meets specification through tests? Take a moment in this section to explain your process, what you did and did not do in order to keep within the time limit, and any other relevant details of how your scoped out the work involved and approached the task.
   
* _Principles_

   Does the candidate apply good software engineering practices in their implementation and tests? 
   
* _Design Considerations_

   Can the candidate explain why they chose the design submitted and can they explain any alternative solutions considered? A candidate who chooses a functional programming paradigm, for example, should be able to explain why they chose an approach different from the the original proposal, what alternatives they considered, and the performance or practical impacts of their choices. 
   
* _Performance_

   Can the candidate explain the performance characteristics of their solution and explain any trade-offs made when implementing their solution?

* _Usability_

   Can the candidate explain any usability issues with their approach for end users of the product who are submitting these archives and responding to any issues with their processing?    
   
* _Maintainability_

   Can the candidate explain any maintainability issues with their approach for the development team?  In a world where two data formats have been requested, how would the team handle requests for additional formats or validation tests?
   
* _Security_

   Can the candidate explain any security concerns with the existing code and their extension of it?  Without adding additional code, what might they recommend adding to this solution to make it potentially safer? 

You should submit your solution either as a link to a source repository (Github, Gitlab, Bitbucket, etc.) or as a tarball. In either case you should provide all code and documentation that is relevant for us to evaluate your work, including especially additional build instructions if we need them.

## Build and Test

The logikcull-loadfiles project requires Kotlin 1.2 and Gradle 5. It's up to
you how to install these dependencies. You can either work directly from the
shell or use an IDE like Eclipse IntelliJ IDEA; just make sure the Gradle build
is intact. The following instructions assume you're in the shell.

Check out the base project from https://github.com/logikcull/logikcull-loadfiles
or expand the tarball we provide. You should see a directory with the following
structure:

``` shell
$ ls -l

-rw-r--r--  1 user  staff  7779 Jan 31 13:30 README.md
-rw-r--r--  1 user  staff  1168 Jan 31 12:41 build.gradle.kts
drwxr-xr-x  3 user  staff    96 Jan 31 11:24 gradle
-rwxr-xr-x  1 user  staff  5764 Jan 31 11:24 gradlew
-rw-r--r--  1 user  staff  2942 Jan 31 11:24 gradlew.bat
-rw-r--r--  1 user  staff   368 Jan 31 11:25 settings.gradle.kts
drwxr-xr-x  4 user  staff   128 Jan 31 11:25 src
```

The application is invoked with the `gradle run` command, like so:

``` shell
$ gradle run

> Task :run
usage: gradle run --args 'path/to/loadfile1.opt path/to/loadfile2.opt'

BUILD SUCCESSFUL in 779ms
2 actionable tasks: 1 executed, 1 up-to-date
```

You can see a usage message is printed. Try `gradle run --args 'src/test/resources/test.opt'`:

``` shell
$ gradle run --args 'src/test/resources/test.opt'

> Task :run
| Loadfile: src/test/resources/test.opt                                        |
| Control Number      | Volume              | Path                             |
| ------------------- | ------------------- | -------------------------------- |
| test-000001         | Import Test 01      | IMAGES\001\test-000001.tif       |
| test-000002         | Import Test 01      | IMAGES\001\test-000002.tif       |
| test-000003         | Import Test 01      | IMAGES\001\test-000003.tif       |

BUILD SUCCESSFUL in 748ms
2 actionable tasks: 1 executed, 1 up-to-date
```

We recommend testing in a couple of different ways:
  - by invoking `gradle run` and following the instructions there
  - by adding unit tests in the appropriate locations in `src/test` and invoking
    `gradle clean test`. The runner is already configured to dump stdout and
    stderr to your console for easier debugging.

It's up to you how to structure the new functionality and associated tests, but
we strongly recommend you review the existing implementation for .OPT files and
at least initially model your work on that.

We wish you the best of luck! Don't be afraid to reach out if you have questions.

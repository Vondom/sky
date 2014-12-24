Performance Analysis Tool [![Build Status](https://travis-ci.org/Vondom/sky.svg?branch=master)](https://travis-ci.org/Vondom/sky) [![Coverage Status](https://img.shields.io/coveralls/Vondom/sky.svg)](https://coveralls.io/r/Vondom/sky?branch=master)
===
Sky Engine is performance analysis tool based web service. This tool analyze your application performance for GitHub open source developer.
## Key Features
* integrate with GitHub and Travis-CI
* automatically analyze performance for application when commit to repository
* will be supported to ruby, python, javascript(node.js or phantom.js), java
* show the result to analyzing performance within each functions execution time

## To-do
* Refactoring
  * ~~do refactoring service classes~~
  * ~~change jsp files to freemarker~~
  * detach sky pom.xml from spring-boot-parent pom.xml
  * add sky-server unit-test for javascript files using angularjs
  * create distributed sky-worker servers using zookeeper or message queueing
* Bug fixes
  * ~~missing data as profiling log~~
* New features
  * add performance analysis for program written by Ruby language
  * add feature to manage sky-worker server clusters (add, remove, information, etc...)
  * create  bash script for running analysis to sky engine in travis ci servers

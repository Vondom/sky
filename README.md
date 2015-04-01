Performance Analysis Tool [![GPLv3](https://www.gnu.org/graphics/gplv3-88x31.png)](LICENSE) [![Build Status](https://travis-ci.org/Vondom/sky.svg?branch=master)](https://travis-ci.org/Vondom/sky) [![Coverage Status](https://img.shields.io/coveralls/Vondom/sky.svg)](https://coveralls.io/r/Vondom/sky?branch=master)
===
Sky Engine is performance analysis tool based on web services. This tool analyzes your application performance and provide hint performance improvement for opensource developer using at GitHub.
## Key Features
* integrates with GitHub and Travis-CI
* automatically analyze performance and key point with improvement for application when commit to repository
* will be supported to ruby, python, javascript(node.js or phantom.js), java, and another languages
* shows the result to analyzing performance improvement within each functions at runtime

## How To Build
1. Build: execute <code># mvn test package</code> if maven is installed or <code># ./mvnw test package</code> in SkyEngine source root directory
2. Enjoy!

## To-do
* Refactoring
  * ~~do refactoring service classes~~
  * ~~change jsp files to freemarker~~
  * ~~detach sky pom.xml from spring-boot-parent pom.xml~~
  * add sky-server unit-test for javascript files using angularjs
  * ~~create distributed sky-worker servers using zookeeper or message queueing (use apache curator framework)~~
  * ~~network communication using thrift replace with pure RESTFul protocol~~
* Bug fixes
  * ~~missing data as profiling log~~
  * ~~don't show profile result with error while running sky-worker~~
* New features
  * Dashboard
    * create comparison chart between profiled commits
    * create chart with each Profile and Method
  * add line number and filename in profiled Method of database
  * add performance analysis for program written by Ruby language
  * add feature to manage sky-worker server clusters (add, remove, information, etc...)
  * create  bash script for running analysis to sky engine in travis ci servers
* Backlogs
  * how to profile hadoop mapreduce
  * think about simplfy for worker
  * attach module to encryption with jasypt (http://www.jasypt.com)

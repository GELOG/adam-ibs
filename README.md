# Adam IBS
[ ![issues](https://img.shields.io/github/issues/gelog/adam-ibs.svg)  ![Issues in progress](https://badge.waffle.io/gelog/adam-ibs.png?label=in progress&title=In Progress)  ![Issues that needs review](https://badge.waffle.io/gelog/adam-ibs.png?label=needs review&title=Needs Review)  ![Issues planned for v0.1](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.1&title=v0.1)  ![Issues planned for v0.2](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.2&title=v0.2)  ![Issues planned for v0.3](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.3&title=v0.3)  ![Issues planned for v0.4](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.4&title=v0.4)  ![Issues planned for v0.5](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.5&title=v0.5)  ](https://waffle.io/gelog/adam-ibs)

[ ![license](https://img.shields.io/github/license/gelog/adam-ibs.svg) ![Join the chat at https://gitter.im/GELOG/adam-ibs](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/GELOG/adam-ibs?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This project ports the IBS/MDS/IBD functionality of PLink to Spark / ADAM.

## Getting started
Checkout our [wiki](https://github.com/GELOG/adam-ibs/wiki)

## Requirements
JDK 1.7

Scala 2.10.x

## Project organisation
adam-ibs is composed of modules adam-ibs-core and adam-ibs-data.

adam-ibs-data define the used avro data model. This module needs to be builded in order to be used in adam-ibs-core module as a .jar dependence.

adam-ibs-core include logic and lci and uses adam-ibs-data jar dependency.

## Build
The project can be built by executing "mvn clean compile install" from the root folder or in two following steps :

1. In adam-ibs-data folder : maven clean compile install
 * You will obtain both adam-ibs-data-0.1.0.jar which is used as maven dependency in adam-ibs-core.

2. In adam-ibs-code folder : maven clean compile install
 * You will obtain 2 jar files :  
  * adam-ibs-core-0.1.0.jar -> jar file containing adam-ibs-core code
  * adam-ibs-core-0.1.0-jar-with-dependencies.jar -> jar file containing adam-ibs-core code with all dependencies.

## Execute from shell
**From adam-ibs-core folder :**

Help :

    adam-ibs-core$ java -jar target/adam-ibs-core-0.1.0-jar-with-dependencies.jar -h
    21:01:07.849 [main] [INFO ] [c.e.m.c.Main$] : Begin with arguments : -h 
        --file  <name>   Specify .ped + .map filename prefix (default 'plink')
        --genome         Calculate IBS distances between all individuals [needs
                       --file and --out]
        --make-bed       Create a new binary fileset. Specify .ped and .map files [required --file and --out]
        --out  <name>    Specify the output filename
        --show-parquet   Show Schema and data sample stored in a parquet file [required --file]
    -h, --help  <arg>    Show help message

Execute with spark-submit :
    
    spark-1.4.1/bin$ ./spark-submit --master local /home/ikizema/DEV/mgl804/adam-ibs/adam-ibs-core/target/adam-ibs-core-0.1.0-jar-with-dependencies.jar --file /home/ikizema/DEV/mgl804/adam-ibs/DATA/avro/test.makeBed.parquet --show-parquet


## Execute from IDEA IDE
CLI parser is integrated to code.

1. View CLI options (--help) :
![image](./WIKI/img/execute/1-execute_help.png)
![image](./WIKI/img/execute/1-response_help.png)

2. Exemple : Execute --make-bed on --file and get a parquet output file --out
![image](./WIKI/img/execute/2-execute--make-bed.png)

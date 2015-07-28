# Adam IBS
[ ![issues](https://img.shields.io/github/issues/gelog/adam-ibs.svg)  ![Issues in progress](https://badge.waffle.io/gelog/adam-ibs.png?label=in progress&title=In Progress)  ![Issues that needs review](https://badge.waffle.io/gelog/adam-ibs.png?label=needs review&title=Needs Review)  ![Issues planned for v0.1](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.1&title=v0.1)  ![Issues planned for v0.2](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.2&title=v0.2)  ![Issues planned for v0.3](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.3&title=v0.3)  ![Issues planned for v0.4](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.4&title=v0.4)  ![Issues planned for v0.5](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.5&title=v0.5)  ](https://waffle.io/gelog/adam-ibs)

[ ![license](https://img.shields.io/github/license/gelog/adam-ibs.svg) ![Join the chat at https://gitter.im/GELOG/adam-ibs](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/GELOG/adam-ibs?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This project ports the IBS/MDS/IBD functionality of PLink to Spark / ADAM.


## Getting started
Checkout our [wiki](https://github.com/GELOG/adam-ibs/wiki)

## Requis :
Mac : Java 1.6

Linux : Java 1.7

Scala : 2.10

## Begin with code
CLI parser is integrated to code.

1. View CLI options (--help) :

![image](./WIKI/img/execute/1-execute_help.png)

![image](./WIKI/img/execute/1-response_help.png)

Options Implemented :

 * --help
 
 * --file
 
 * --out
 
 * --make-bed


2. Exemple : Execute --make-bed on --file and get a parquet output file --out

![image](./WIKI/img/execute/2-execute--make-bed.png)

Output parquet files are generated in ./DATA/avro :

![image](./WIKI/img/execute/2-result--make-bed.png)


## Logging
Nous utilisons logback pour le logging. Loggback est le successeur du populaire log4j qui n'est plus supporté.
Logback est composé de 3 composants : 

 * logback-core
 
 * logback-classic (qui est une implementation de SLF4J et une amelioaration de log4j)
 
 * logback-access (pour utilisation evelouée)

Pour utiliser logback pour scala dans un projet maven, il faut ajouter la dependance logback-core. Cette dependance incluera les packages suivants : logback-classic, log4j, slf4j

NB : Il faut absoulment exclure le package sl4j du package apache.spark, la raison est que apache.spark utilise slf4j, lequel est utilisé egalement par logback.

Utilisation : 
import org.slf4j.LoggerFactory // importer le package

def logb = LoggerFactory.getLogger(this.getClass()) // creer une instance, pour la classe a loguer
 
logb.info("hello world") // loguer avec level INFO

Il y a 5 level, qui sont dans l'ordre  Trace, Debug, Info, Warn, Error

Il faut creer un fichier config : src/scala/ressources/logback.xml, dans lequel on defint :
 
 * l'appender : la sortie : console, fichier, ou autre
 
 * layout : affichage
 
 * level.
 
Pour le level, on peut definir un niveau minimum a logguer. Exemple, si on décide que le niveau le plus bas est : INFO, donc tous les log de type : Debug, Trace, ne seront pas affichés

On peut egalement, desactivé les log pour un package en particulier

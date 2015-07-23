# Adam IBS
[ ![issues](https://img.shields.io/github/issues/gelog/adam-ibs.svg)  ![Issues in progress](https://badge.waffle.io/gelog/adam-ibs.png?label=in progress&title=In Progress)  ![Issues that needs review](https://badge.waffle.io/gelog/adam-ibs.png?label=needs review&title=Needs Review)  ![Issues planned for v0.1](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.1&title=v0.1)  ![Issues planned for v0.2](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.2&title=v0.2)  ![Issues planned for v0.3](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.3&title=v0.3)  ![Issues planned for v0.4](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.4&title=v0.4)  ![Issues planned for v0.5](https://badge.waffle.io/gelog/adam-ibs.png?label=v0.5&title=v0.5)  ](https://waffle.io/gelog/adam-ibs)

[ ![license](https://img.shields.io/github/license/gelog/adam-ibs.svg) ![Join the chat at https://gitter.im/GELOG/adam-ibs](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/GELOG/adam-ibs?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This project ports the IBS/MDS/IBD functionality of PLink to Spark / ADAM.


## Getting started
Checkout our [wiki](https://github.com/GELOG/adam-ibs/wiki)

## Cloner les branches en locale
Valable pour une machine (Mac|Linux) avec package GIT installé

![image](./WIKI/img/git-clone.png)

Apercu structure des dossiers

![image](./WIKI/img/folders-structure.png)

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

#! /bin/bash

rm -f tasktracker.log
java TM.java start TaskA
java TM.java rename TaskA Task1
java TM.java stop Task1
java TM.java summary

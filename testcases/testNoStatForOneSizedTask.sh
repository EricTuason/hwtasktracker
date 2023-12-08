#! /bin/bash

rm tasktracker.log
java TM.java start Task1
java TM.java size Task1 S
java TM.java stop Task1
java TM.java summary S

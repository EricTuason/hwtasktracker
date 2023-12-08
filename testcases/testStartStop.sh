#! /bin/bash

rm tasktracker.log
java TM.java start Task1
sleep 2
java TM.java stop Task1
java TM.java summary

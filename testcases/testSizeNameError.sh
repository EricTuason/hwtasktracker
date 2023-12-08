#! /bin/bash

rm tasktracker.log
java TM.java start S
java TM.java start M
java TM.java start L
java TM.java start XL

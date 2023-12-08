#! /bin/bash

rm -f tasktracker.log
java TM.java size task1 L
java TM.java size task2 L
java TM.java rename task1 task2


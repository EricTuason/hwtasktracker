#! /bin/bash

rm tasktracker.log
java TM.java start TaskA
sleep 1
java TM.java stop TaskA
java TM.java size TaskA XL
java TM.java size TaskB XL
java TM.java size TaskC XL
java TM.java size TaskErr L
java TM.java describe TaskErr2 "This task shouldn't be printed"
java TM.java summary XL

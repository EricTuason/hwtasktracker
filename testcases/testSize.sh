#! /bin/bash

rm -f tasktracker.log
java TM.java size Task S
java TM.java size Task2 M
java TM.java size Task3 L
java TM.java size Task4 XL
java TM.java summary


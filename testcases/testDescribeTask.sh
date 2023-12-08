#! /bin/bash

rm tasktracker.log
java TM.java describe Task "This section describes Task"
java TM.java describe Task "This is another description of Task" XL
java TM.java describe Task "Yet another description of Task"
java TM.java summary

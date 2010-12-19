#!/bin/sh

cd ../../bin
java -classpath .:../lib/jdom.jar ch.idsia.scenarios.champ.LearningTrack -ag competition.gic2010.learning.sergeykarakovskiy.SergeyKarakovskiy_MLPAgent -rec SergeyKarakovskiy_MLPAgent

-lco off -lb on -le off -lhb off -lg on -ltb on -lhs off -lc on -lde on -ld 5 -ls 33829
-lde on -i on -ld 30 -ls 33434
-lde on -i on -ld 30 -ls 33434 -lhb on

-lla on -le off -lhs on -lde on -ld 5 -ls 332656
-le off -lhs on -lde on -ld 5 -ls 332656 -rec superLevel
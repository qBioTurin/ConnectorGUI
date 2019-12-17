#!/bin/sh
cd /home/user/ConnectorGUI
path=${PWD}/dist

java -Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel -jar "$path/ConnectorGUI.jar" %F

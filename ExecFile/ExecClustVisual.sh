#!/bin/sh

inputfile=$1
outputPlotFolder=$2
feature=$3
mood=$4
title=$5
labelsx=$6
labelsy=$7
output=${8}
path=${PWD}


echo
echo "======================================================="
echo "			  INPUT PARAMETERS"
echo "======================================================="
echo
echo "RData file:                                                   $inputfile"
echo "Output Plot Folder:                                           $outputPlotFolder"
echo "Feature selected:                                             $feature"
echo "Plot info:                                                    $title; $labelsx; $labelsy "
echo "(1) Mean curves, (2) Spline, (3) discriminant :               $mood"
echo "Output folder:                                                $output"
echo 
echo "======================================================="

echo 


echo " Current folder: ${PWD}"
echo " "
echo "Executing R script"

args="R CMD BATCH --no-save --no-restore  '--args   $inputfile $outputPlotFolder $feature $title $labelsx $labelsy $mood ' ./Rscripts/Step3ClusterVisualization.R $output/Routput.Rout"

echo "$args"

eval "$args"

echo
echo
echo "======================================================="
echo "		       END EXECUTION"
echo "======================================================="

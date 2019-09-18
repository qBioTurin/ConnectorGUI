#!/bin/sh

inputfile=$1
outputPlotFolder=$2
TruncTime=$3
feature=$4
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
echo "RData file:               $inputfile"
echo "Output Plot Folder:       $outputPlotFolder"
echo "Truncation time:          $outputPlotFolder"
echo "Feature selected:         $TruncTime"
echo "Plot info:                $title; $labelsx; $labelsy "
echo "Output folder:            $output"
echo 
echo "======================================================="

echo 


echo " Current folder: ${PWD}"
echo " "
echo "Executing R script"

args="R CMD BATCH --no-save --no-restore  '--args   $inputfile $outputPlotFolder $TruncTime $feature $title $labelsx $labelsy ' ./Rscripts/Step1Truncation.R $output/Routput.Rout"

echo "$args"

eval "$args"

echo
echo
echo "======================================================="
echo "		       END EXECUTION"
echo "======================================================="

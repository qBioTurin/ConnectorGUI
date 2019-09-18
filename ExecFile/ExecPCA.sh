#!/bin/sh

inputfile=$1
outputPlotFolder=$2
p=$3
output=${4}
path=${PWD}


echo
echo "======================================================="
echo "			  INPUT PARAMETERS"
echo "======================================================="
echo
echo "RData file:               $inputfile"
echo "Output Plot Folder:       $outputPlotFolder"
echo "p value:                  $p"
echo "Output folder:            $output"
echo 
echo "======================================================="

echo 


echo " Current folder: ${PWD}"
echo " "
echo "Executing R script"

args="R CMD BATCH --no-save --no-restore  '--args   $inputfile $outputPlotFolder $p' ./Rscripts/Step2EstimationH.R $output/Routput.Rout"

echo "$args"

eval "$args"

echo
echo
echo "======================================================="
echo "		       END EXECUTION"
echo "======================================================="

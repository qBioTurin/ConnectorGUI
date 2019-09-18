#!/bin/sh

inputfile=$1
outputPlotFolder=$2
pmin=$3
pmax=$4
output=${5}
path=${PWD}


echo
echo "======================================================="
echo "			  INPUT PARAMETERS"
echo "======================================================="
echo
echo "RData file:               $inputfile"
echo "Output Plot Folder:       $outputPlotFolder"
echo "p min value:              $pmin"
echo "p max value:              $pmax"
echo "Output folder:            $output"
echo 
echo "======================================================="

echo 


echo " Current folder: ${PWD}"
echo " "
echo "Executing R script"

args="R CMD BATCH --no-save --no-restore  '--args   $inputfile $outputPlotFolder $pmin $pmax' ./Rscripts/Step2EstimationP.R $output/Routput.Rout"

echo "$args"

eval "$args"

echo
echo
echo "======================================================="
echo "		       END EXECUTION"
echo "======================================================="

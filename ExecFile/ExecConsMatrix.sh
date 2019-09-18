#!/bin/sh

inputfile=$1
outputPlotFolder=$2
k=$3
mood=$4
output=${5}
path=${PWD}


echo
echo "======================================================="
echo "			  INPUT PARAMETERS"
echo "======================================================="
echo
echo "RData file:                                               $inputfile"
echo "Output Plot Folder:                                       $outputPlotFolder"
echo "Number of clusters:                                       $k"
echo "(1) Consensus Matrix; (2) Most probable clustering:       $mood"
echo "Output folder:                                            $output"
echo 
echo "======================================================="

echo 


echo " Current folder: ${PWD}"
echo " "
echo "Executing R script"

args="R CMD BATCH --no-save --no-restore  '--args   $inputfile $outputPlotFolder $k $mood ' ./Rscripts/Step3Extrapolation.R $output/Routput.Rout"

echo "$args"

eval "$args"

echo
echo
echo "======================================================="
echo "		       END EXECUTION"
echo "======================================================="

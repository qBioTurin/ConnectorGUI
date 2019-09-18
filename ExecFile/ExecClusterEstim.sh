#!/bin/sh

inputfile=$1
outputPlotFolder=$2
p=$3
h=$4
kmin=$5
kmax=$6
runs=$7
output=${8}
path=${PWD}


echo
echo "======================================================="
echo "			  INPUT PARAMETERS"
echo "======================================================="
echo
echo "RData file:               $inputfile"
echo "Output Plot Folder:       $outputPlotFolder"
echo "p value:                  $p"
echo "h value:                  $h"
echo "cluster interval values:  $kmin to $kmax"
echo "runs:                     $runs"
echo "Output folder:            $output"
echo 
echo "======================================================="

echo 


echo " Current folder: ${PWD}"
echo " "
echo "Executing R script"

args="R CMD BATCH --no-save --no-restore  '--args   $inputfile $outputPlotFolder $p $h $kmin $kmax $runs ' ./Rscripts/Step3Stability.R $output/Routput.Rout"

echo "$args"

eval "$args"

echo
echo
echo "======================================================="
echo "		       END EXECUTION"
echo "======================================================="

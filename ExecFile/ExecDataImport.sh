#!/bin/sh

group=$1
GrowDataFile=$2
AnnotationFile=$3
outputfolder=$4
output=${5}
path=${PWD}


echo
echo "======================================================="
echo "			  INPUT PARAMETERS"
echo "======================================================="
echo
echo "Group:              $group"
echo "Growth file:              $GrowDataFile"
echo "Features type:            $AnnotationFile"
echo "Output folder:            $outputfolder"
echo "ROutput folder:           $output"
echo 
echo "======================================================="

echo 


echo " Current folder: ${PWD}"
echo " "
echo "Executing R script"

args="R CMD BATCH --no-save --no-restore  '--args   $group $GrowDataFile $AnnotationFile $outputfolder ' ./Rscripts/Step1DataImport.R $output/Routput.Rout"

echo "$args"

eval "$args"

echo
echo
echo "======================================================="
echo "		       END EXECUTION"
echo "======================================================="

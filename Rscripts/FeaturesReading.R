##First read in the arguments listed at the command line
args=(commandArgs(TRUE))

connecList<-args[1]

load(connecList)

  features<-paste(names(CONNECTORList$LabCurv),collapse = ",")


features

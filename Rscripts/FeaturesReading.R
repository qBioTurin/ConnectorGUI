##First read in the arguments listed at the command line
args=(commandArgs(TRUE))

connecList<-args[1]
clust <-args[2] # TRUE if  we need the clusters ; FALSE if we don't need the clusters

load(connecList)

if( clust )
{
  if(!exists("CONNECTORList.FCM") )
  {
    features <- 0
  }else{
    features<-paste(names(CONNECTORList$LabCurv),collapse = ",")
  }
  
}else
{
  features<-paste(names(CONNECTORList$LabCurv),collapse = ",")
}

features

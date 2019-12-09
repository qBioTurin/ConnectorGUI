##First read in the arguments listed at the command line
args=(commandArgs(TRUE))

connecList<-args[1]
clust <-args[2] # TRUE if  we need the clusters ; FALSE if we don't need the clusters

load(connecList)

if( clust )
{
  if(!exists("CONNECTORList.FCM") )
  {
    TimeGrid <- 0
  }else{
    TimeGrid<- CONNECTORList.FCM$TimeGrid
  }
  
}else
{
  TimeGrid<-CONNECTORList$TimeGrid
}

paste0(",",as.integer(min(TimeGrid)),",",as.integer(max(TimeGrid)),",",as.integer(median(TimeGrid)),",")
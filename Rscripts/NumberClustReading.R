##First read in the arguments listed at the command line
args=(commandArgs(TRUE))

connecList<-args[1]

load(connecList)

if(!exists("CONNECTORList.FCM") )
{
  cl.numb<-0.01
  
}else if(is.null(CONNECTORList.FCM$ConsensusInfo[[1]]) )
  {
  
    if(!is.null(CONNECTORList.FCM$FCM) ) # check if it is the connector list most probable
       {
          
          cl.numb<-length(CONNECTORList.FCM$FCM$prediction$meancurves[1,])
      
    }else{
      
      cl.numb<-0.01
    }
 
}else{
  
  nmCL<-names(CONNECTORList.FCM$ConsensusInfo[[1]])
  cl.numb<-as.numeric(gsub("G= ", "", nmCL))
  
}


cl.numb

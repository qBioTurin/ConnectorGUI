##First read in the arguments listed at the command line
args=(commandArgs(TRUE))

connecList<-args[1]

load(connecList)

if(!exists("CONNECTORList.FCM")  )
{
  output<-0
  
}else
{
  h<-length(CONNECTORList.FCM$FCM$fit$parameters$alpha[1,]) 
  
  if(! h %in% 1:2) output<-0
  else output<-1
  
}

output


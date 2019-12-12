##First read in the arguments listed at the command line
args=(commandArgs(TRUE))

connecList<-args[1]

## mood = 1 check if it is an RData with multiple clustering
## mood = 2 check if it is an RData with the most probable clustering

mood <-args[2] 

load(connecList)

output = "Select the mood!!"

if(mood == 1)
{
  
  if(!exists("CONNECTORList.FCM")  )
  {
    output<-0
  }else{ 
    output<-1
  }
  
}else if(mood == 2)
{
  if(!is.null(CONNECTORList.FCM$FCM)  )
  {
    output<-0
  }else{ 
    output<-1
  }
}


output

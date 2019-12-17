##First read in the arguments listed at the command line

#### Output meaning:
# 0 -> list was found
# 1 -> list was not found
# 4 -> no mood selected

args=(commandArgs(TRUE))

connecList<-args[1]
mood <-args[2]
path <-args[3] 

## mood = 1 check if it is an RData with multiple clustering + reading of the clusters
## mood = 2 check if it is an RData with the most probable clustering
## mood = 3 reading the features
## mood = 4 reading the features and the time grid
## mood = 5 h checking for discriminant plot

##### Output matrix definition:
## mood = 1 than out needs 2 rows for the number of clusters and the checking list 
## mood = 2 than out needs 2 rows the checking list, features
## mood = 3 than out needs 2 rows for the features and the checking list
## mood = 4 than out needs 3 rows for the features, the checking list and time grid
## mood = 5 than out needs 3 rows the checking list, features and  h checking

if(mood %in% 1:3 )
{
  out<-matrix(0,2,1)
  
}else out<-matrix(0,3,1)

load(connecList)

## initial values:
output = 4 
output.h = 1
cl.numb = "no"
features= 4

#######

if(mood == 1)
{
  
  if(!exists("CONNECTORList.FCM")  )
  {
    output<-0
  }else{  
    if( is.null(CONNECTORList.FCM$ConsensusInfo)  )
    {
      output<-0
    }else{ 
      output<-1
      
      nmCL<-names(CONNECTORList.FCM$ConsensusInfo[[1]])
      cl.numb<-gsub("G= ", "", nmCL)
    }
    
    out[2,]=paste("Clust, ",paste(cl.numb,collapse = ", ") )   
    
  }
}else if(mood == 2)
{
  if(!exists("CONNECTORList.FCM")  )
  {
    output<-0
  }else{ 
    
    if( is.null(CONNECTORList.FCM$FCM)  )
    {
      output<-0
    }else{ 
      output<-1
    }
    
    if(!exists("CONNECTORList")  )
    {
      output<-2
    }else{ 
      output<-1
      features<-paste(names(CONNECTORList$LabCurv),collapse = ",")
    }
    
    out[2,]=paste("Feat, ",features)
  }
}else if(mood == 3){
  if(!exists("CONNECTORList")  )
  {
    output<-2
  }else{ 
    output<-1
    features<-paste(names(CONNECTORList$LabCurv),collapse = ",")
  }
  
  out[2,]=paste("Feat, ",features)
}else if(mood == 4){
  if(!exists("CONNECTORList")  )
  {
    output<-2
  }else{ 
    output<-1
    features<-paste(names(CONNECTORList$LabCurv),collapse = ",")
    TimeGrid<-CONNECTORList$TimeGrid
  }
  
  out[2,]= paste("Feat, ",features)
  out[3,]= paste("Timegrid, ",paste0(as.integer(min(TimeGrid)),",",as.integer(max(TimeGrid)),",",as.integer(median(TimeGrid)),",") )
  
}else if(mood == 5)
{
  if(!exists("CONNECTORList.FCM")  )
  {
    output<-0
  }else{ 
    
    if( is.null(CONNECTORList.FCM$FCM)  )
    {
      output<-0
    }else{ 
      output<-1
      h<-length(CONNECTORList.FCM$FCM$fit$parameters$alpha[1,])
      if(! h %in% 1:2) output.h<-0
      
    }
    
    if(!exists("CONNECTORList")  )
    {
      output<-2
    }else{ 
      output<-1
      features<-paste(names(CONNECTORList$LabCurv),collapse = ",")
    }
    
    out[2,]=paste("Feat, ",features)
    out[3,]=paste("Check.h, ",output.h)
  }
}


out[1,]=paste("CheckList, ",output)

write.table(out,file=paste(path,"out_tmp.txt",sep="/"),row.names = F, col.names = F)

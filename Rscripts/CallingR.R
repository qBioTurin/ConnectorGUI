##First read in the arguments listed at the command line

#### Output meaning:
# 0 -> list was found
# 1 -> list was not found
# 4 -> no mood selected

args=(commandArgs(TRUE))

connecList<-args[1]
mood <-args[2]
path <-args[3] 

## mood = 1 check if it is an RData with multiple clustering
## mood = 2 check if it is an RData with the most probable clustering
## mood = 3 reading the features


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
            cl.numb<-as.numeric(gsub("G= ", "", nmCL))
          }
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
        h<-length(CONNECTORList.FCM$FCM$fit$parameters$alpha[1,]) 
        
        if(! h %in% 1:2) output.h<-0
        
      }
  }
}else if(mood == 3){
  if(!exists("CONNECTORList")  )
  {
    output<-0
  }else{ 
    output<-1
  features<-paste(names(CONNECTORList$LabCurv),collapse = ",")
  }
}

out<-matrix(0,ncol=1,nrow = 4)
out[1,]=paste("CheckList, ",output)
out[2,]=paste("Feat, ",features)
out[3,]=paste("Check.h, ",output.h)
out[4,]=paste("Clust, ",paste(cl.numb,collapse = ", ") )

write.table(out,file=paste(path,"out_tmp.txt",sep="/"),row.names = F, col.names = F)

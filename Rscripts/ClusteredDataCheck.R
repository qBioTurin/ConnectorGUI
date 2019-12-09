##First read in the arguments listed at the command line
args=(commandArgs(TRUE))

connecList<-args[1]

load(connecList)

if(!exists("CONNECTORList.FCM")  )
  {
    output<-0
}else{ 
    output<-1
  }

output

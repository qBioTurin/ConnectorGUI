##First read in the arguments listed at the command line
args=(commandArgs(TRUE))


##args is now a list of character vectors
## First check to see if arguments are passed.
## Then cycle through each element of the list and evaluate the expressions.
if(length(args)==0) {
  print("No arguments supplied.")
  ##supply default values
  
} else {
  for(i in 1:length(args)) {
    eval(parse(text=args[[i]]))
  }
}

### Rscripts loading
source("Rscripts/runDocker.R")
source("Rscripts/DockerTest.R")

###################################

docker.Truncation<-function(input.file,output.PlotFolder,Trunc.Time,feature,title=NULL,labels.x=NULL,labels.y=NULL)
{
  #running time 1
  ptm <- proc.time()
  
  #obtaining absolute paths
  output.PlotFolder <- normalizePath(output.PlotFolder)
  input.file<-normalizePath(input.file)
  
  data.folder <- output.PlotFolder
  
  #storing the position of the home folder
  home <- getwd()
  setwd(data.folder)
  
  #initialize status
  system("echo 0 > ExitStatusFile 2>&1")
  
  ## check if file exists
  if(!file.exists(input.file) || !file.exists(output.PlotFolder) )  {
    system("echo 10 > ExitStatusFile 2>&1")
    return(10) 
  }
  
  #testing if docker is running
  test <- dockerTest()
  if(!test){
    cat("\nERROR: Docker seems not to be installed in your system\n")
    system("echo 10 > ExitStatusFile 2>&1")
    #setwd(home)
    return(10)
  }
  

  #executing the docker job
  params <- paste("--cidfile", paste0(data.folder, "/dockerID"),
                  "-v", paste0( input.file, ":/Data/input.file"),
                   "-v", paste0(output.PlotFolder, ":/Data/out"),
                   "qbioturin/connector Rscript /Scripts/Truncation.R",feature,Trunc.Time,labels.x,labels.y,title)
  
  resultRun <- runDocker(params=params)
  
  #waiting for the end of the container work
  if(resultRun==0){
    cat("\nThe data uploading is finished\n")
    
    #running time 2
    ptm <- proc.time() - ptm
    dir <- dir(data.folder)
    dir <- dir[grep("run.info",dir)]
    if(length(dir)>0) {
      con <- file("run.info", "r")
      tmp.run <- readLines(con)
      close(con)
      tmp.run[length(tmp.run)+1] <- paste("user run time mins ",ptm[1]/60, sep="")
      tmp.run[length(tmp.run)+1] <- paste("system run time mins ",ptm[2]/60, sep="")
      tmp.run[length(tmp.run)+1] <- paste("elapsed run time mins ",ptm[3]/60, sep="")
      writeLines(tmp.run,"run.info")
    } else {
      tmp.run <- NULL
      tmp.run[1] <- paste("run time mins ",ptm[1]/60, sep="")
      tmp.run[length(tmp.run)+1] <- paste("system run time mins ",ptm[2]/60, sep="")
      tmp.run[length(tmp.run)+1] <- paste("elapsed run time mins ",ptm[3]/60, sep="")
      
      writeLines(tmp.run,"run.info")
    }
    
    #saving log and removing docker container
    container.id <- readLines(paste(data.folder,"/dockerID", sep=""), warn = FALSE)
    system(paste("docker logs ", substr(container.id,1,12), " &> ",data.folder,"/", "datavisual_", substr(container.id,1,12),".log", sep=""))
    system(paste("docker rm ", container.id, sep=""))
    # removing temporary files
    cat("\n\nRemoving the temporary file ....\n")
    system("rm -fR out.info")
    system("rm -fR dockerID")
    #system(paste("cp ",paste(path.package(package="docker4seq"),"containers/containers.txt",sep="/")," ",data.folder, sep=""))
    setwd(home)
    
  }
  
  system("echo 0 > ExitStatusFile 2>&1")
  
}


###################################

Trunc.Time<-as.numeric(Trunc.Time)
cat("------",Trunc.Time,"\n")

docker.Truncation(input.file=input.file,output.PlotFolder=output.PlotFolder,Trunc.Time=Trunc.Time,feature=feature,title=title,labels.x=labels.x,labels.y=labels.y)

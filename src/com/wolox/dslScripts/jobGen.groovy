println "JPM jobs generator v0.1"
//multibranchWorkflowJob
//removeAction('DELETE')  
parentPath="devops/sandbox/"
String[] pBranch=["develop","deploy/test","deploy/stage","deploy/prodna"]
String[] pFolder=["/datica-dev","/datica-test","/datica-staging","/datica-prodna"]
String[] mbJobBranch=['bugfix/* feature/* hotfix/* merge/*']
String[] mbJobFolder=['/predev']
println appBitbucketUrl
println appSpace
println appType
println appName
println appBitbucketUrl
println bitbucketCreds
subFolder='/'

if (dryRun) {
  println "========================================================"
  println "                  THIS IS A DRY RUN                     "
  println "              Job will not be generated                 "
  println "========================================================"
}

switch (appType) {
   case 'Android': subFolder = '/android/' ; break;
   case 'iOS'    : subFolder = '/iOS/'     ; break;
   case 'Datica' : subFolder = '/'         ; break;
   default       : subFolder = '/' ;
}

println "Adjusted appSpace= ${appSpace}"


//=================================================================
//            Generate the predev multibrnach jobs
//=================================================================
println "Generate the predev multibrnach jobs"
for (i = 0; i < mbJobBranch.length; i++) {
  cBranch=mbJobBranch[i]
  appPath="devops/"+appSpace+mbJobFolder[i]+subFolder+appName
  println "creating multibranch job for app: ${appName} branch: ${cBranch} in ${appPath}"
  if (dryRun)continue
  multibranchPipelineJob(appPath) {
    branchSources {
      git {
        remote(appBitbucketUrl)
        credentialsId(bitbucketCreds)
        includes(cBranch)
      }
    }
    orphanedItemStrategy {
      discardOldItems {
        numToKeep(10)
      }
    }
  }
}


//=================================================================
//              Generate the pipeline jobs
//=================================================================
println "Generate the pipeline jobs"
for (i = 0; i < pBranch.length; i++) {
  cBranch=pBranch[i]
  appPath="devops/"+appSpace+pFolder[i]+subFolder+appName
  println "creating job for app: ${appName} branch: ${cBranch} in ${appPath}"
  if (dryRun)continue
  pipelineJob (appPath) {
    logRotator (10, -1, -1, -1)
    description ("Job Created by Moshe Suberri JPM job generator")
    definition {
      cpsScm {
        scm {
          git {
            remote { url(appBitbucketUrl) 
                   credentials(bitbucketCreds)
                 }
            branches("${it}")
            scriptPath('Jenkinsfile')
            extensions { }  // required as otherwise it may try to tag the repo, which you may not want
          }
        }
      }
    } 
  }
}
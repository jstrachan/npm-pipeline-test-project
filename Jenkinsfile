#!/usr/bin/groovy
@Library('github.com/rawlingsj/fabric8-pipeline-library@master')
def utils = new io.fabric8.Utils()
def org = 'rawlingsj'
def repo = 'ngx-login-client'
fabric8UITemplate{
  clientsNode{
    ws {
      git "https://github.com/${org}/${repo}.git"
      
      readTrusted 'release.groovy'
      def pipeline = load 'release.groovy'

      if (utils.isCI()){
        container('ui'){
          pipeline.ci()
        }
      } else if (utils.isCD()){
        def branch
        container('clients'){
            branch = utils.getBranch()
        }
        
        container('ui'){
          pipeline.cd(branch)
        }

        def releaseVersion
        container('clients'){
            releaseVersion = utils.getLatestVersionFromTag()
        }

        pipeline.updateDownstreamProjects(releaseVersion)
      }
    }
  }
}


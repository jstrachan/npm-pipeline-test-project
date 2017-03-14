#!/usr/bin/groovy
@Library('github.com/rawlingsj/fabric8-pipeline-library@master')
def utils = new io.fabric8.Utils()
def org = 'fabric8-ui'
def repo = 'npm-pipeline-test-project'
fabric8UITemplate{
  clientsNode{
    ws {
      git "https://github.com/${org}/${repo}.git"
      readTrusted 'release.groovy'
      sh "git remote set-url origin git@github.com:${org}/${repo}.git"
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
          echo "p ${pipeline}"
          echo "b ${branch}"
          pipeline.cd(branch)
        }

        def releaseVersion
        container('clients'){
            releaseVersion = utils.getLatestVersionFromTag()
        }

        // pipeline.updateDownstreamProjects(releaseVersion)
      }
    }
  }
}


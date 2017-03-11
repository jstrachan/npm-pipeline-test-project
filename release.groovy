#!/usr/bin/groovy
def ci (){
    stage('build'){
        sh 'npm install'
    }
    stage('unit test'){
        sh './run_unit_tests.sh'
    }
    stage('functional test'){
        sh './run_functional_tests.sh'
    }
}

def cd (b){
    stage('fix git repo'){
        sh './fix-git-repo.sh'
    }

    stage('build'){
        sh 'npm install'
        sh 'npm run build'
    }

    stage('unit test'){
        sh './run_unit_tests.sh'
    }

    stage('functional test'){
        sh './run_functional_tests.sh'
    }

    stage('release'){
        sh 'npm version patch'
        sh "GIT_BRANCH=${b} npm run semantic-release"
    }
}

def updateDownstreamProjects(v){
    pushPackageJSONChangePR{
        propertyName = 'ngx-login-client'
        projects = [
                'fabric8-ui/ngx-fabric8-wit',
                'fabric8io/fabric8-ui'
        ]
        version = v
    }
}
return this
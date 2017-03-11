#!/usr/bin/groovy

def ci (){
    stage('build'){
        sh 'npm install'
    }
    stage('unit test'){
        sh 'npm test'
    }
}

def cd (b){
    // stage('fix git repo'){
    //     sh './fix-git-repo.sh'
    // }

    stage('build'){
        sh 'npm install'
        sh 'gulp scripts'
    }

    stage('unit test'){
        sh 'npm test'
    }

    stage('release'){
        sh "GIT_BRANCH=${b} npm run semantic-release"
    }
}

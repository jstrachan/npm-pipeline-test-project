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
        sh "git config user.email fabric8-admin@googlegroups.com"
        sh "git config user.name fabric8-release"
        sh 'chmod 600 /root/.ssh-git/ssh-key'
        sh 'chmod 600 /root/.ssh-git/ssh-key.pub'
        sh 'chmod 700 /root/.ssh-git'

        String npmToken = readFile '/home/jenkins/.npm-token/token'
        String ghToken = readFile '/home/jenkins/.apitoken/hub'
        wrap([$class: 'MaskPasswordsBuildWrapper', varPasswordPairs: [
            [password: npmToken, var: 'NPM_PASSWORD'],
            [password: ghToken, var: 'GH_PASSWORD']]]) {
        
            try {
                sh """
                export NPM_TOKEN=${npmToken} 
                export GITHUB_TOKEN=${ghToken} 
                export GIT_BRANCH=${b}
                npm run semantic-release
                """
            } catch (err) {
                echo "${err}"
                input id: 'Proceed', message: "ok"
            }
        }
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
#!groovy

node('build-slave') {
    currentBuild.result = "SUCCESS"

    try {
        stage('Checkout'){
            checkout scm
        }

        stage('Build'){
            env.NODE_ENV = "build"
            print "Environment will be : ${env.NODE_ENV}"
            dir('./adminutil') {
                sh('./build.sh')
            }
        }

        stage('Publish'){
            echo 'Push to Repo'
            dir('./adminutil') {
                sh './src/metadata.sh > metadata.json'
                sh 'ARTIFACT_LABEL=bronze ./dockerPushToRepo.sh'
                sh 'cat metadata.json'
                archive includes: "metadata.json"
            }
       }
    }
    catch (err) {
        currentBuild.result = "FAILURE"
        throw err
    }

}

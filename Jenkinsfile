pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'pwd'
        sh 'ls -la'
        sh "chmod -R 777 ${env.WORKSPACE}"
        sh './gradlew assembleDebug'
      }
    }

  }
}
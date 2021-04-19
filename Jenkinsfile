pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh 'pwd'
        sh 'ls -la'
        sh "chmod +x -R ${env.WORKSPACE}"
        sh './gradlew assembleDebug'
      }
    }

  }
}
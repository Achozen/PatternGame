pipeline {
  agent any
  stages {
    stage('build') {
      steps {
        sh "chmod +x -R ${env.WORKSPACE}"
        sh './gradlew assembleDebug'
      }
    }

  }
}
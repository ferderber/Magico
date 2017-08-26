pipeline {
  agent any
  stages {
    stage('Build') {
      steps {
        sh './gradlew build'
      }
    }
    stage('Report') {
      steps {
        archiveArtifacts 'build/libs/*magico*.jar'
      }
    }
  }
}
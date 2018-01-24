pipeline {
  agent none
  stages {
    stage('prep'){
      steps {
        node('master') {
          deleteDir()
          checkout scm
          stash 'code'
        }
      }
    }
    
	stage('build and create war file') {
      steps {
          'flask docker image':{
            node('master') {
              deleteDir()
              sh 'echo "HELLO"'
            }
          }
      }
    }
    stage('build docker images') {
      steps {
          'flask docker image':{
            node('master') {
              deleteDir()
              sh 'echo "HELLO"'
            }
          }
      }
    }
	stage('deploy docker images to aws instance') {
      steps {
          'flask docker image':{
            node('master') {
              deleteDir()
              sh 'echo "HELLO"'
            }
          }
      }
    }

  }
  
}

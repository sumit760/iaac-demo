pipeline {

  agent none
  stages {
    stage('Checkout Code'){
      steps {
        node('master') {
          deleteDir()
          checkout scm
          stash 'code'
        }
      }
    }
	stage('Create EC2 Instance') {
      steps {
        parallel(
          'flask docker image':{
            node('master') {
              deleteDir()
              sh 'echo "HELLO"'
            }
          }
        )
      }
    }
    stage('Build Application') {
      steps {
        parallel(
          // REST API stuff
          'tests unit': {
            node('master') {
              deleteDir()
              unstash 'code'
              sh 'echo "HELLO"'
              
            }
          },
           // TODO: make the coverage report be built-into the unit/acceptance test steps
       
          'tests acceptance': {
            node('master') {
              deleteDir()
              unstash 'code'
             sh 'echo "HELLO"'
            }
           },         
        )
      }
    }
    stage('build docker images') {
      steps {
        parallel(
          'flask docker image':{
            node('master') {
              deleteDir()
              sh 'echo "HELLO"'
            }
          }
        )
      }
    }
	stage('Deploy Containerized App on EC2') {
      steps {
        parallel(
          'flask docker image':{
            node('master') {
              deleteDir()
              sh 'echo "HELLO"'
            }
          }
        )
      }
    }
	stage('Testing') {
      steps {
        parallel(
          'flask docker image':{
            node('master') {
              deleteDir()
              sh 'echo "HELLO"'
            }
          }
        )
      }
    }

  }

   }

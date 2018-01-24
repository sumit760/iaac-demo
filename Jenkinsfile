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
          'Create EC2 Instance':{
            node('master') {
              deleteDir()
	      unstash 'code'
              //sh 'cd ${WORKSPACE}'
	      //sh 'chmod +x wrapper.sh'
	      sh '${WORKSPACE}/wrapper.sh'
	      sh 'sleep 60'
		
            }
          }
        )
      }
    }
    stage('Build Application') {
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

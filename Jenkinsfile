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
	      //sh '${WORKSPACE}/wrapper.sh'
	      //sh 'sleep 60'
		
            }
          }
        )
      }
    }
    stage('Build Application') {
      steps {
        parallel(
          'Build Application':{
            node('master') {
	       withMaven(maven: 'mvn3.2.5') {
		     deleteDir()
	     	     unstash 'code'
              	     sh 'mvn clean install'
		     archiveArtifacts 'target/*.war'
		    // stash name:'war_file', includes: '*.war'
		}
		
            }
          }
        )
      }
    }
    stage('build docker images') {
      steps {
        parallel(
          'build docker image':{
            node('master') {
              deleteDir()
	      unstash 'code'
              sh 'echo "HELLO"'
	      sh '${WORKSPACE}/docker_create_deploy.sh'
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

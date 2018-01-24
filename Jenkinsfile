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
    stage('build and testing') {
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
     
         ,

          'build sqlite3 database': {
            node('master') {
              deleteDir()
              unstash 'code'
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

  }

   }

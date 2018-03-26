pipeline {
  agent {
    docker {
      image 'maven:3.5.2-jdk-8'
    }
  }
  environment {
      tomcatCredentials = credentials('TOMCATCREDENTIALS')
      developmentHost = credentials('DEVELOPMENTHOST')
    }
  stages {
    stage('Build') {
      steps {
        sh 'mvn clean -Daccession.service=file-accession install -DskipTests'
      }
    }
    stage('Deploy') {
     steps {
        sh "curl --upload-file target/*.war 'http://'${tomcatCredentials}'@'${developmentHost}':8080/manager/text/deploy?path=/t2d&update=true' | grep 'OK - Deployed application at context path /t2d'"
     }
  }
}
}

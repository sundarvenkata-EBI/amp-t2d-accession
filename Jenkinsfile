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
        sh 'mvn clean -Daccession.service=file-accession install -DskipTests -DbuildDirectory=file/target'
        sh 'mvn clean -Daccession.service=study-accession install -DskipTests -DbuildDirectory=study/target'
      }
    }
    stage('Deploy') {
     steps {
        sh "curl --upload-file file/target/*.war 'http://'${tomcatCredentials}'@'${developmentHost}':8080/manager/text
        /deploy?path=/hash&update=true' | grep 'OK - Deployed application at context path'"
         sh "curl --upload-file study/target/*.war 'http://'${tomcatCredentials}'@'${developmentHost}':8080/manager/text
                /deploy?path=/monotonic&update=true' | grep 'OK - Deployed application at context path'"
     }
  }
}
}

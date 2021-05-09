pipeline {
  agent any

  environment {
    APP = "app"
    BUILD = "${JOB_NAME.replace('/', '-')}-${BUILD_NUMBER}"
  }

  stages {
    stage('Build Server') {
      steps {
        sh "docker build --target server-build -t ${BUILD}_server ."
      }
    }

    stage('Test Server') {
      steps {
        sh "docker run ${BUILD}_server ./mvnw test"
      }
    }

    stage('Build final') {
      when {
        branch 'main'
      }
      steps {
        sh "docker build --target final -t ${APP}_${APP} ."
      }
    }

    stage('Deploy') {
      when {
        branch 'main'
      }
      steps {
        sh "docker-compose -f docker-compose.yml -f docker-compose.override.yml -p ${APP} up -d --remove-orphans"
        script {
          def startLogs = sh script: "sleep 5 && docker logs ${APP}_${APP}_1 | grep -B50 -A1 'listening on:'", returnStdout: true
          println(startLogs)
        }
      }
    }

  }
}
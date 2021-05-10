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
        sh "docker tag ${APP}_${APP} registry.heroku.com/github-scanner/web"
        sh "docker login --username=${HEROKU_USER} --password=${HEROKU_KEY} registry.heroku.com"
        sh "docker image push registry.heroku.com/github-scanner/web:latest"
        sh "./heroku-container-release.sh"
      }
    }

  }
}
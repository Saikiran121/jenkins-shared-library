def call() {

    def config = readYaml file: 'frontend-service-config.yaml'

    stage('Build') {
        sh config.buildCommand
    }

    stage('Test') {
        if (config.testCommand) {
            sh config.testCommand
        }
    }

    stage('Docker Build & Push') {
        sh """
          docker build -t ${config.dockerImage}:${BUILD_NUMBER} .
          docker push ${config.dockerImage}:${BUILD_NUMBER}
        """
    }
}


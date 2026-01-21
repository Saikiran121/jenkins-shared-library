def call() {
    
    echo "Running Service Pipeline"

    def config = readYaml file: 'frontend-service/frontend-service-config.yaml'

    stage('Build') {
        sh config.buildCommand
    }

    stage('Test') {
        if (config.testCommand) {
            sh config.testCommand
        }
    }

}


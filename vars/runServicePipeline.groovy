def call() {

    echo "Running Service Pipeline"

    def config = readYaml file: "${env.WORKSPACE}/frontend-service/frontend-service-config.yaml"

    dir(config.serviceDir) {

        stage('Install Dependencies') {
            echo "Installing dependencies"
            sh config.installCommand
        }

        stage('Build') {
            echo "Building application"
            sh config.buildCommand
        }

        stage('Test') {
            echo "Running tests"
            sh config.testCommand
        }
    }
}

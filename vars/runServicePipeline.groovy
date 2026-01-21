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
	    if (config.buildCommand?.trim()) {
	    	sh config.buildCommand
	    } else {
	 	echo "No build setp defined for this service"
	    }
        }

        stage('Test') {
            if (config.testCommand?.trim()) {
                sh config.testCommand
            } else {
                echo "No tests defined"
            }

        }
    }
}

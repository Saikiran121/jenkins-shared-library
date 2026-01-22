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
       
        stage('Sonarqube Analysis') {
          
          def scannerHome = tool 'sonar-scanner'

          withSonarQubeEnv('sonarqube') {
            sh """
               sonar-scanner \
               -Dsonar.projectKey=${config.sonarProjectKey} \
               -Dsonar.projectName=${config.sonarProjectName} \
               -Dsonar.sources=. \
               -Dsonar.host.url=https://sonar.saikbiradar.in \
            """
          }
        }

        stage('Quality Gate') {
            timeout(time: 5, unit: 'MINUTES') {
                waitForQualityGate abortPipeline: true
            }
        }
    }
}

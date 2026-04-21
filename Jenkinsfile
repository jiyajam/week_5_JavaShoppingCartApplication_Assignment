pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17' // Match your pom.xml compiler version
    }

    environment {
        DOCKER_HUB_USER = "jiyak"
        DOCKERHUB_CREDENTIALS = 'Docker_Hub'
        IMAGE_NAME = "shopping-cart-app"
        // Use 'latest' for the final push so it's easier to run manually
        DOCKER_IMAGE = "${DOCKER_HUB_USER}/${IMAGE_NAME}:latest"
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/jiyajam/week_5_JavaShoppingCartApplication_Assignment.git'
            }
        }

        stage('Build & Test') {
            steps {
                // Generates the JaCoCo coverage report file
                sh 'mvn clean verify'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    // Must match the name in Jenkins Global Tool Configuration
                    def scannerHome = tool 'SonarScanner'

                    withSonarQubeEnv('SonarQubeServer') {
                        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                            sh """
                            ${scannerHome}/bin/sonar-scanner \
                            -Dsonar.projectKey=JavaShoppingCart \
                            -Dsonar.projectName="Java Shopping Cart" \
                            -Dsonar.host.url=http://localhost:9000 \
                            -Dsonar.token=${SONAR_TOKEN} \
                            -Dsonar.sources=src/main/java \
                            -Dsonar.tests=src/test/java \
                            -Dsonar.java.binaries=target/classes \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                            -Dsonar.exclusions=**/Main.java,**/MainController.java
                            """
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // Note: On Mac, you might need /usr/local/bin/docker if 'docker' isn't in path
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: "${DOCKERHUB_CREDENTIALS}",
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline finished for build #${BUILD_NUMBER}"
        }
        success {
            echo "Deployment successful! Check http://localhost:9000 for coverage ✅"
        }
    }
}
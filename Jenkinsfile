pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/jiyajam/week_5_JavaShoppingCartApplication_Assignment.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t yourdockerhub/shopping-cart .'
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([string(credentialsId: 'dockerhub-pass', variable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u yourdockerhub --password-stdin'
                }
                sh 'docker push yourdockerhub/shopping-cart'
            }
        }
    }
}

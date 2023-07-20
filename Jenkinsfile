pipeline {
    agent any
    options {
        timeout(time: 1, unit: 'HOURS') // set timeout 1 hour
    }
    environment {
        TIME_ZONE = 'Asia/Seoul'
        PROFILE = 'local'

        REPOSITORY_CREDENTIAL_ID = 'github-key'
        REPOSITORY_URL = 'https://ghp_b5K2a2WTA34Qg|Vuu3EQDm9f×E2Ta624w65D@github.com/SWM-Cupid/jikting-backend.git'
        TARGET_BRANCH = 'feat/SP-40'

        CONTAINER_NAME = 'cupid-backend'

        AWS_CREDENTIAL_NAME = 'aws-key'
        ECR_PATH = '191509918438.dkr.ecr.ap-northeast-2.amazonaws.com'
        IMAGE_NAME = '191509918438.dkr.ecr.ap-northeast-2.amazonaws.com/backend'
        REGION = 'ap-northeast-2'
    }
    stages{
        stage('init') {
            steps {
                echo 'init stage'
                deleteDir()
            }
            post {
                success {
                    echo 'success init in pipeline'
                }
                failure {
                    error 'fail init in pipeline'
                }
            }
        }
        stage('clone project') {
            steps {
                git url: "$REPOSITORY_URL",
                    branch: "$TARGET_BRANCH",
                    credentialsId: "$REPOSITORY_CREDENTIAL_ID"
                sh "ls -al"
            }
            post {
                success {
                    echo 'success clone project'
                }
                failure {
                    error 'fail clone project' // exit pipeline
                }
            }
        }
        stage('build project') {
            steps {
                sh '''
        		 ./gradlew bootJar
        		 '''
            }
            post {
                success {
                    echo 'success build project'
                }
                failure {
                    error 'fail build project' // exit pipeline
                }
            }
        }
        stage('dockerizing project by dockerfile') {
            steps {
                sh '''
        		 docker build -t $IMAGE_NAME:$BUILD_NUMBER .
        		 docker tag $IMAGE_NAME:$BUILD_NUMBER $IMAGE_NAME:latest

        		 '''
            }
            post {
                success {
                    echo 'success dockerizing project'
                }
                failure {
                    error 'fail dockerizing project' // exit pipeline
                }
            }
        }
        stage('upload aws ECR') {
            steps {
                script{
                    // cleanup current user docker credentials
                    sh 'rm -f ~/.dockercfg ~/.docker/config.json || true'


                    docker.withRegistry("https://${ECR_PATH}", "ecr:${REGION}:${AWS_CREDENTIAL_NAME}") {
                      docker.image("${IMAGE_NAME}:${BUILD_NUMBER}").push()
                      docker.image("${IMAGE_NAME}:latest").push()
                    }

                }
            }
            post {
                success {
                    echo 'success upload image'
                }
                failure {
                    error 'fail upload image' // exit pipeline
                }
            }
        }
    }
}
def uat_ecr = "<UAT_ECR_LINK>"
def prod_ecr = "<PROD_ECR_LINK>"

def uat_cluster = "<UAT_ECS_CLUSTER>"
def prod_cluster = "<PROD_ECS_CLUSTER>"

def uat_service = "<UAT_SERVICE_NAME>"
def prod_service = "<PROD_SERVICE_NAME>"

def ecr = ""
def ecs_cluster = ""
def ecs_service = ""

pipeline {
    agent any
    stages {
        stage('Assign pointers for environment') {
            steps{
                script {
                    if(params.env_name == 'uat'){
                        ecr = uat_ecr
                        ecs_cluster = uat_cluster
                        ecs_service = uat_service
                    }
                    else if(params.env_name == 'prod'){
                        ecr = prod_ecr
                        ecs_cluster = prod_cluster
                        ecs_service = prod_service
                    }
                    if (ecr == ""){
                        script {
                            echo 'Exiting as env is not known'
                            currentBuild.getRawBuild().getExecutor().interrupt(Result.FAILURE)
                            sleep(1)
                        }
                    }
                }
            }
        }

        stage('Build and Push Docker Image') {
            steps {
                withAWS(region: 'ap-south-1', credentials: '') {
                    script {
                        echo 'Build docker image'
                        echo 'ECR = ' + ecr
                        echo 'ECS cluster = ' + ecs_cluster
                        echo 'ECS service = ' + ecs_service
                        sh 'pwd'
                        sh 'ls -la'
                        def dockerImage = docker.build(ecr, "-f dockerfile .")
                        echo 'Login into ECR'
                        sh 'aws ecr get-login-password --region ap-south-1 | docker login --username AWS --password-stdin <ECR_LINK>'
                        echo 'Push image to ECR'
                        echo 'docker push ' + ecr
                        sh 'docker push ' + ecr
                    }
                }
            }
        }
        stage('Deploy to ECS') {
            steps {
                withAWS(region: 'ap-south-1', credentials: '') {
                    script {
                        echo 'Deploying to ECS'
                        sh "aws ecs update-service --cluster " + ecs_cluster + " --service " + ecs_service + ' --force-new-deployment --no-cli-pager'
                    }
                }
            }
        }

        stage('Deployment Status Fetch') {
            steps {
                withAWS(region: 'ap-south-1', credentials: '') {
                    script {
                        echo "Waiting for ECS deployment to complete..."
                        sh "aws ecs wait services-stable --cluster " + ecs_cluster + " --service " + ecs_service
                    }
                }
            }
        }
    }
}
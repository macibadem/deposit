pipeline {
    agent any
    environment {
        GIT_REPO = "${env.LOCAL_REPO_PATH}"
        BRANCH = 'master'
        DOCKER_NETWORK = 'deposit_network'
    }
    stages {
        stage('Checkout') {
            steps {
                dir("${GIT_REPO}") {
                    script {
                        if (isUnix()) {
                            sh """
                            git fetch
                            git reset --hard origin/master
                            """
                        } else {
                            bat """
                            git fetch
                            git reset --hard origin/master
                            """
                        }
                    }
                }
            }
        }
        stage('Determine Changed Modules') {
            steps {
                script {
                    def changedFiles
                    if (isUnix()) {
                        changedFiles = sh(script: "git diff --name-only HEAD~1", returnStdout: true).trim()
                    } else {
                        changedFiles = bat(script: "git diff --name-only HEAD~1", returnStdout: true).trim()
                    }

                    def modulesToBuild = []
                    if (changedFiles.contains("account-service")) {
                        modulesToBuild.add("account-service")
                    }
                    if (changedFiles.contains("customer-service")) {
                        modulesToBuild.add("customer-service")
                    }
                    if (changedFiles.contains("query-service")) {
                        modulesToBuild.add("query-service")
                    }
                    if (changedFiles.contains("transaction-service")) {
                        modulesToBuild.add("transaction-service")
                    }
                    if (changedFiles.contains("api-gateway")) {
                        modulesToBuild.add("api-gateway")
                    }
                    env.MODULES_TO_BUILD = modulesToBuild.join(',')
                    echo "Modules to build: ${env.MODULES_TO_BUILD}"
                }
            }
        }
        stage('Build and Redeploy Modules') {
            steps {
                script {
                    def modules = env.MODULES_TO_BUILD.split(',')
                    for (module in modules) {
                        echo "Building and redeploying ${module}"
                        if (isUnix()) {
                            sh """
                            docker-compose build ${module}
                            docker-compose up -d ${module}
                            """
                        } else {
                            bat """
                            docker-compose build ${module}
                            docker-compose up -d ${module}
                            """
                        }
                    }
                }
            }
        }
    }
}

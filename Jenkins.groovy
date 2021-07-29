CODE_CHANGES = GetgitChanges()
def gv
pipeline {
    agent any
    tools {
        maven 'Maven'
    }
    parameters {
        choice(name: 'VERSION', choices: ['1.1.0', '1.2.0, 1.3.0'], description: '')
        booleanParam(name: 'executeTest', defaultValue: true, description: '')
    }
    environment {
        NEW_VERSION = '1.3.0'
        SERVER_CREDENTIALS = credentials('server-credential')
    }

    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"            
        
                }
        }
        stage("build") {
            steps {
                script {
                    gv.buildApp
                }
            }
            when {
                expression {
                    prams.executeTest == true
                    // BRANCH_NAME == 'dev' && CODE_CHANGES == true
                }
                sh "mvn install"
            }
                }
                echo 'building the application....'
                //echo "building version ${NEW_VERSION}"
            }
        }
        stage("test") {
            when {
                expression {
                    BRANCH_NAME == 'dev' || BRANCH_NAME == 'main'
                }
            }
            steps {
                script {
                    gv.testApp
                }
            steps {
                echo 'testing the application....'
            }
        }
        stage("deploy") {
            steps {
                echo 'deploying the application....'
                echo "deploying version ${VERSION}"
                sh "${SERVER_CREDENTIALS"}"
                // or with wrapper
                // withCredentials([
                //     usernamePassword(credentials: 'server-credential', usernameVariable: USER, passwordVariable: PWD)
                // ]) {
                //     sh "some script ${USER} ${PWD}"
                //    }
            }
        }

    }
    post {
        always {
            // building in progress
        }
        success {
            // building, testing and deploying completed

        }
        failure {
            // one of the deployment stagaes failed
        }
    }
}

pipeline {
    agent any
    
    environment {
        //Conexi�n ssh
        SSH_USER = 'eperez'
        SSH_IP = '185.185.82.98'
        //Ruta de scripts a ejecutar
        PATH_SCRIPTS = '/home/eperez/intech/bo-scripts/docker'
        SCRIPT = 'pro-ribera-app.sh'
        //Argumentos para crear contenedor
        BRANCH = 'develop'
        PORT = '10010'
    }

    stages {
        
        stage('Run SSH Commands') {
            steps {
                script {
                    def remoteCommands = [
                        "${PATH_SCRIPTS}/${SCRIPT} --action build --branch ${BRANCH}",
                        "${PATH_SCRIPTS}/${SCRIPT} --action stop --branch ${BRANCH}",
                        "${PATH_SCRIPTS}/${SCRIPT} --action run --branch ${BRANCH}"
                    ]
                    
                    sshagent(['ssh-agent']) {
                        for (def command in remoteCommands) {
                            echo "Executing command: $command"
                            def sshOutput = sh(
                                script: "ssh -o StrictHostKeyChecking=no ${SSH_USER}@${SSH_IP} '$command'",
                                returnStdout: true
                            )
                            echo "Command output:\n$sshOutput"
                            def errorTypes = ['error', 'exception', 'failure', 'failed', 'fatal', 'conflict', 'timeout']
                            def errorFound = false
                            
                            for (def errorType in errorTypes) {
                                def regex = "(?i)\\b${errorType}\\b"
                                if (sshOutput =~ regex) {
                                    error("Command failed with error:\n$sshOutput")
                                    errorFound = true
                                }
                            }
                            
                            if (errorFound) {
                                currentBuild.result = 'FAILURE'
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
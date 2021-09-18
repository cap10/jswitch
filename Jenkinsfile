node {

  environment {}
  stage('Scm checkout') {
    def gitexec = tool name: 'Default', type: 'git'
    git branch: 'master', credentialsId: 'cap10-github', url: 'https://github.com/cap10/jswitch'
  }
  stage('Build') {
    def mvnHome = "/usr/share/maven"
    def mvnCMD = "${mvnHome}/bin/mvn"
    sh "${mvnCMD} -B -DskipTests clean package"
  }

  stage('Build docker image') {
    sh 'docker-compose build'
  }


  stage('Push new image') {
   withCredentials([string(credentialsId: 'docker-password-new', variable: 'dockerHubPwd')]) {
   sh "docker login -u cap10 -p ${dockerHubPwd}"
           }
           sh 'docker push cap10/myrepository:metbank-cashmet-postillion-service'
   }

}

node {

  environment {}
  stage('Scm checkout') {
    def gitexec = tool name: 'Default', type: 'git'
    git branch: 'master', credentialsId: 'cap10-github', url: 'https://github.com/jugaad-zw/mypolad.git'
  }
  stage('Build') {
    def mvnHome = "/usr/share/maven"
    def mvnCMD = "${mvnHome}/bin/mvn"
    sh "${mvnCMD} -B -DskipTests clean package"
  }

  stage('Build docker image') {
    sh 'docker-compose build api-service'
  }
 /*
  stage('Github Packages Login') {
    sh "cat /var/lib/jenkins/INVENICO_TOKEN.txt | docker login docker.pkg.github.com -u cap10 --password-stdin"
  }

  stage('tagging image') {

    sh 'docker tag cap10/myrepository:polad-nhaka-dev docker.pkg.github.com/jugaad-zw/mypolad/polad-nhaka-dev:latest'

  }*/

  stage('Push new image') {
   withCredentials([string(credentialsId: 'docker-password-new', variable: 'dockerHubPwd')]) {
   sh "docker login -u cap10 -p ${dockerHubPwd}"
           }
           sh 'docker push cap10/myrepository:polad-nhaka-dev'
   }

}

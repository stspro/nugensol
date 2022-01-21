node{
  
  cleanWs()
  stage("Read Configuration"){    
     sh "ls -ltr"
     git branch: 'main',    url: 'https://github.com/stspro/nugensol.git'
     sh "ls -ltr"
     def configfile = readYaml file: 'dev-ci-cd/dev/services/config.yml'
     println configfile
     def application_git_url =  configfile.git_url
     def mvn_version = configfile.mvn_version
    def package_name = "mkyong.war"
    println $pipelines_env
    println "this is env: ${pipelines_env}"
     println configfile.jenkins_environment
  }
  stage("checkout Application"){
     git url: application_git_url  
     sh "ls -ltr"
     sh "java --version"        
  }  
 stage("build automation"){
   sh "ls -ltr"
   dir ("web-thymeleaf-war"){
      sh "mvn package "
   }
 }  
 stage("unit testing"){
   dir ("web-thymeleaf-war"){
      sh "mvn test "
   }
 }
  
  stage("code analysis"){
  }
  
  stage("build management"){
  //artifactory upload
  dir ("web-thymeleaf-war/target"){
   archiveArtifacts "mkyong.war"
  }
    
  }
  stage("build image"){
    sh "ls -ltr"
    sh "mkdir -p dockerimage"
    sh "cp dev-ci-cd/dev/services/Dockerfile dockerimage/."
    sh "cp web-thymeleaf-war/target/mkyong.war dockerimage/."
    dir ("dockerimage")
     sh "sudo docker build -t spring-boot:1.0 ."
     sh "docker scan"
  }
  stage("upload image to ecr"){
    sh "ls -ltr"

  }
  
  stage("deployment"){
    sh "docker ps"
    sh "docker run -d -p 8080:8080 spring-boot:1.0"
    sh  "docker stop spring-boot:1.0"


   // dir ("web-thymeleaf-war/target"){
      //bat "copy mkyong.war C:\\DevOps\\apache-tomcat-9.0.43\\webapps"
    // }
    //copy web-thymeleaf-war/target/mkyong.war $TOMCAT/webapps/
   //start Tomcat
  //C:\DevOps\apache-tomcat-9.0.43\webapps
}


}
  

























